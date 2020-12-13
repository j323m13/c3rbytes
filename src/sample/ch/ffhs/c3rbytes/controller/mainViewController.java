package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;
import sample.ch.ffhs.c3rbytes.utils.UrlOpener;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import static java.lang.String.valueOf;

/**
 * This is the mainView Controller. It controls the main view and interacts
 */

public class mainViewController implements Initializable, IController {
    @FXML private  Button openUrlButton;
    @FXML private  Button copyButton;
    @FXML private  Button modifyButton;
    @FXML private TextField searchField;
    @FXML private Label foundLabel;
    @FXML private javafx.scene.control.TableView<DatabaseEntry> profileTable;
    @FXML private TableColumn<DatabaseEntry, String> idColumn;
    @FXML private TableColumn<DatabaseEntry, String> categoryColumn;
    @FXML private TableColumn<DatabaseEntry, String> userNameColumn;
    @FXML private TableColumn<DatabaseEntry, String> passwordColumn;
    @FXML private TableColumn<DatabaseEntry, String> urlColumn;
    @FXML private TableColumn<DatabaseEntry, String> updateColumn;
    @FXML private TableColumn<DatabaseEntry, String> noteColumn;
    @FXML private Button logoutButton;
    @FXML private Button searchButton;
    @FXML private Button deleteButton;
    @FXML private final ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
    @FXML private Button addButton;


    FXMLLoader loader = null;
    DatabaseEntryDao mainViewDao = new DatabaseEntryDao();

    /**
     * Copy an entry (a row) from the tableview
     * @return the copied entry
     */
    private DatabaseEntry copyClickedEntry() {
        DatabaseEntry tmp = new DatabaseEntry(
                profileTable.getSelectionModel().getSelectedItem().getId(),
                profileTable.getSelectionModel().getSelectedItem().getDummyId(),
                profileTable.getSelectionModel().getSelectedItem().getUsername(),
                profileTable.getSelectionModel().getSelectedItem().getDescription(),
                profileTable.getSelectionModel().getSelectedItem().getUrl(),
                profileTable.getSelectionModel().getSelectedItem().getPassword(),
                profileTable.getSelectionModel().getSelectedItem().getCreationDate(),
                profileTable.getSelectionModel().getSelectedItem().getLastUpdate(),
                profileTable.getSelectionModel().getSelectedItem().getNote());
        //System.out.println("id from profiletable :"+profileTable.getSelectionModel().getSelectedItem().getDummyId());
        System.out.println("the object: "+tmp.getDummyId()+","+tmp.getId()+", "+tmp.getUsername() + ", "+ tmp.getDescription()+", " + tmp.getPassword() + ", " + tmp.getUrl() + ", " + tmp.getHiddenPasswordTrick()
                +", "+tmp.getNote()+", "+tmp.getCreationDate()+", "+tmp.getLastUpdate());
        return tmp;
    }


    /**
     * Start a listener for mouse click in the mainview.
     */
    private void startMouseClicks() {
        //Methode to listen to mouse clicks
        profileTable.setOnMousePressed(mouseEvent -> {
            if ((mouseEvent.getClickCount() == 2)) {
                try {
                    startOpenSelectedItemsToView(copyClickedEntry());
                } catch (Exception e) {
                    System.out.println("the row is empty "+e.getMessage());
                }
            }
        });
    }



    /**
     * methode to create a right click menu.
     */
    private void startContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addItemOption = new MenuItem("Add item");
        MenuItem modifyItemOption = new MenuItem("Modify item");
        MenuItem openURLOption = new MenuItem("Open url");
        MenuItem copyURLOption = new MenuItem("Copy url");
        MenuItem copyPasswordOption = new MenuItem("copy password");
        MenuItem deleteItemOption = new MenuItem("Delete item");


        addItemOption.setOnAction((actionEvent -> addButton.fire()));

        modifyItemOption.setOnAction(actionEvent -> modifyButton.fire());

        openURLOption.setOnAction((event) -> openUrlButton.fire());

        copyURLOption.setOnAction((event) -> copyURL());

        copyPasswordOption.setOnAction((event) -> copyButton.fire());

        deleteItemOption.setOnAction((event) -> deleteButton.fire());

        //set the menu
        contextMenu.getItems().addAll(addItemOption, modifyItemOption,openURLOption, copyURLOption, copyPasswordOption, deleteItemOption);
        //add the menu to the view
        profileTable.setContextMenu(contextMenu);


    }

    /**
     * Initialize the mainview
     * @param url JavaFx parameter
     * @param resourceBundle JavaFx parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //if table and schema are already set, then it throws an exception and continues.
        try {
            mainViewDao.setup();
        } catch (SQLException | ClassNotFoundException | InterruptedException e){
            System.out.println("everything is already set.");
        }
        startMouseClicks();
        startContextMenu();
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();

        //set columns of the tableview
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("dummyId")
        );

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        userNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );

        passwordColumn.setCellValueFactory(
                new PropertyValueFactory<>("hiddenPasswordTrick")
        );

        urlColumn.setCellValueFactory(
                new PropertyValueFactory<>("url")
        );
        updateColumn.setCellValueFactory(
                new PropertyValueFactory<>("creationDate")
        );
        noteColumn.setCellValueFactory(
                new PropertyValueFactory<>("note")
        );

        loadDatabaseEntries(databaseEntries);
    }

    /**
     * reload the mainview
     */
    @FXML
    private void reloadMainView(){
        loadDatabaseEntries(databaseEntries);
    }

    private void loadDatabaseEntries(ObservableList<DatabaseEntry> databaseEntries) {
        try {
            ObservableList<DatabaseEntry> entries = mainViewDao.getAll();
            databaseEntries.clear();
            databaseEntries.addAll(entries);
            populateTableView(databaseEntries);
            foundLabel.setText(valueOf(databaseEntries.size()));
        } catch (SQLException | ClassNotFoundException | InterruptedException throwables) {
            System.out.println(throwables);
        }
    }

    /**
     * populate the table view with the results from the database
     * @param entries (ObservableList<DatabaseEntry>)
     */
    @FXML
    private void populateTableView(ObservableList<DatabaseEntry> entries) {
        profileTable.setItems(entries);


    }

    /**
     * Add new item in the database. open a new view (add_new_item_view.fxml)
     */
    public void addNewItemAction(){
        try {
            FXMLLoader loader;
            URL url = getClass().getClassLoader().getResource("add_new_item_view.fxml");
            loader = new FXMLLoader(url);
            Parent addItemParent = loader.load();
            addNewItemController controller = loader.getController();
            controller.createCombox();
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.getIcons().add(new Image("logo3.png"));
            stage.setScene(new Scene(addItemParent, 600, 400));
            stage.showAndWait();
            reloadMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logout Methode
     * @throws SQLException if database queries was not successful
     */
    public void logoutAction() throws SQLException {
        System.out.println("Logout Action");
        //TODO: Add necessary methods to clear/reencrypt/delete before closing the app.
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        //shutdown the db before closing. db will be encrypted again.
        mainViewDao.shutdown();
        stage.close();
        System.exit(0);
    }


    /**
     * Given a string, it searches an element in the database.
     * if nothing is to be found, it throws an information.
     * @throws IOException a problem has occurred.
     */

    public void searchAction() throws IOException {
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.WAIT);
        String searchElement = searchField.getText();
        DatabaseEntryDao searchDao = new DatabaseEntryDao();
        databaseEntries.clear();
        ObservableList<DatabaseEntry> resultSearch = FXCollections.observableArrayList();
        resultSearch = searchDao.searchElement(searchElement,databaseEntries);
        if(resultSearch.size() == 0){
            String alertMessage = "no luck. \n Our algorithm (Jérémie) did not find a result for you.";
            startAlert(alertMessage, Alert.AlertType.INFORMATION, "Information");
        }else{
            populateTableView(resultSearch);
            //set the number of items found ("found item: #number)
            foundLabel.setText(valueOf(resultSearch.size()));
        }

        System.out.println("Search Action");
        scene.setCursor(Cursor.DEFAULT);


    }


    /**
     * copy password in computer memory.
     * @param dbEntry
     */
    private void copyPassword(DatabaseEntry dbEntry) {
        try{
            String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

            // get password of the selected row
            String encryptedAccountPassword = dbEntry.getPassword();

            // decrypt account password
            PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();
            String decryptedAccountPassword = passwordEncrypterDecrypter.decrypt(encryptedAccountPassword,
                    passwordDecrypterPassword);

            System.out.println("decryptedAccountPassword: " + decryptedAccountPassword);

            // send plain text password to clipboard
            ClipboardHandler clipboardHandler = new ClipboardHandler();
            clipboardHandler.copyPasswordToClipboard(decryptedAccountPassword);
        }catch (Exception ignored){

        }
    }

    /**
     * Copy the url in memory.
     */
    private void copyURL(){
        ClipboardHandler clipboardHandler = new ClipboardHandler();
        try {
            String url = profileTable.getSelectionModel().getSelectedItem().getUrl();
            if(url!=null){
                clipboardHandler.copyPasswordToClipboard(url);
            }
        }catch (Exception e){
            System.out.println("url is null "+e);

        }

    }

    /**
     *Button to copy the password in memory
     */
    public void copyPasswordAction() {
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");
        try{
            DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
            if(dbEntry != null){
                copyPassword(dbEntry);
            }
        }catch (Exception e){
            System.out.println("password is null "+e);
        }

    }

    /**
     * methode to delete an item in the database (and the tableview)
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void deleteProfileAction() throws IOException {
        DatabaseEntryDao deleter = new DatabaseEntryDao();
        Optional<ButtonType> confirm = null;
        try{
            if(copyClickedEntry() != null){
                String alertMessage = "The entry will be deleted. \nAre you sure ?";
                confirm = startAlert(alertMessage, Alert.AlertType.CONFIRMATION, "Confirmation" );
            }
            if(confirm.get() == ButtonType.OK) {
                try {
                    deleter.delete(copyClickedEntry());
                    System.out.println("entry has been deleted");
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("delete not working");
                }
            }
        }catch (Exception e){
            String alertMessage = "there is no entry in the Tableview. \n" +
                    "Nothing to delete!";
            confirm = startAlert(alertMessage, Alert.AlertType.INFORMATION, "Information" );
        }
        reloadMainView();

    }

    /**
     * Methode to delete the account of the user.
     * it deletes:
     * - all the entries
     * - the derby.log file
     * - the database folder
     * - the c3r.c3r file
     *
     * this action is final. no coming back from it. only tears.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void deleteAccountAction() throws IOException, SQLException, InterruptedException {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
        String alertText = "Are you sure you want to delete your account? \n" +
                "There is no way back to that.";
        Optional<ButtonType> confirm = startAlert(alertText, Alert.AlertType.CONFIRMATION, "Confirmation");
        if(confirm.get() == ButtonType.OK){
            OSBasedAction helper = new OSBasedAction();
            DatabaseEntryDao deleteDao = new DatabaseEntryDao();
            deleteDao.deleteAccount();
            helper.deleteDatabaseFolder(helper.getPath("derby.log"));
            System.exit(0);

        }else {
            System.out.println("discard.");
        }
    }

    /**
     * Methode to open the selected items in the table view.
     * it open the selected item in a view item mode.
     * the user can modify the elements (like the password)
     * @param dbentry (a DatabaseEntry object)
     * @throws Exception if the selected entry are empty when opened, an exception is raised.
     */
    private void startOpenSelectedItemsToView(DatabaseEntry dbentry) throws Exception {
        URL url = getClass().getClassLoader().getResource("add_new_item_view.fxml");
        loader = new FXMLLoader(url);
        Parent viewItemControllerParent = loader.load();
        addNewItemController controller = loader.getController();
        controller.fillIn(copyClickedEntry());
        controller.updateTextField();
        Stage stage = new Stage();
        stage.setTitle("View item");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(viewItemControllerParent, 600,400));
        stage.showAndWait();
        reloadMainView();
    }


    /**
     * Methode to change the Master Password
     * @see changePasswordController
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void changeMasterAction() throws IOException{

        System.out.println("Change Master Password Action");
            Stage stage = new Stage();
            changePasswordController changePassword = new changePasswordController();
            changePassword.getView(stage);
            stage.show();
    }


    /**
     * Methode to call the change master Passphrase action
     */
    public void changeMasterPPAction(){
        //TODO: here we have to open login_view_masterpassphrase.fxml and aks for the passphrase or something similar

        //TODO delete ?
        Parent changePassphrase;

        try {

            //TODO: then here we have to call the set_master_mpp_view.fxml and ask for the new passphrase

            Stage stage = new Stage();
            changePassphraseController changePP = new changePassphraseController();
            changePP.getView(stage);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * open an url in the browser
     * if the url is written with http, https, then it will work correctly.
     * @param url the url which will be opened.
     */
    private void openUrl(String url) {
        // open the url
            UrlOpener urlOpener = new UrlOpener();
            urlOpener.openURL(url);
    }

    /**
     * methode to action the opening of the url
     */
    public void onOpenUrl() {
        // first get url form selected row
        try{
            DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
            String url = dbEntry.getUrl();
            if(url != null){
                openUrl(url);
            }
        }catch (Exception open){
            System.out.println("url is null "+open);
        }

    }

    /**
     * Reload the main view when a button is pressed (or from the right click menu)
     */
    public void reload() {
        reloadMainView();

    }

    /**
     * modify an entry when a button if pressed (or from the right click menu)
     */
    public void onModifyProfile() {
        try {
            startOpenSelectedItemsToView(copyClickedEntry());
        }catch (Exception ex){
            System.out.println("Entry is null "+ex);

        }
    }

    /**
     * Methode to display an alert to the user.
     * @param alertText the text to be displayed
     * @param TYPE the type of the alert
     * @param confirmation a string of characters to accompany the type of alert.
     * @return resultConfirm (Optional<ButtonType>) a ButtonType to hold the result of the alert.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    private Optional<ButtonType> startAlert(String alertText, Alert.AlertType TYPE, String confirmation) throws IOException {
        URL url = getClass().getClassLoader().getResource("alert_view.fxml");
        loader = new FXMLLoader(url);
        Parent alertViewParent = loader.load();
        alertViewController controller = loader.getController();
        Optional<ButtonType> resultConfirm = controller.startAlertWindows(alertText, TYPE, confirmation);
        return resultConfirm;
    }

    /**
     * methode to capture the view of mainView
     * @param stage the stage of the view
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("main_view_2.fxml");
        loader = new FXMLLoader(url);
        Parent mainView = loader.load();
        //stage stage = new Stage();
        stage.setTitle("C3rBytes");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(mainView, 1020, 600));
    }

    /**
     * Methode to call the controller of the view
     * @return the controller of the view
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    /**
     * Methode to manage the keys input in this view.
     * @param keyEvent key strokes
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public void manageInput(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            searchAction();
        }

    }
}

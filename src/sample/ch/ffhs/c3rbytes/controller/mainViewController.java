package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;
import sample.ch.ffhs.c3rbytes.utils.UrlOpener;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.valueOf;


public class mainViewController implements Initializable, IController {
    @FXML private TextField searchField;
    @FXML
    private Label foundLabel;
    @FXML
    private javafx.scene.control.TableView<DatabaseEntry> profileTable;
    @FXML
    private TableColumn<DatabaseEntry, String> idColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> categoryColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> userNameColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> passwordColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> urlColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> updateColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> noteColumn;
    @FXML
    private Button reloaddata;
    @FXML private Button searchButton;
    @FXML private Button deleteButton;
    @FXML private ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
    @FXML private Button addButton;

    public static final String FILENAME = "c3r.c3r";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    @FXML
    private Button deleteAccountButton;
    @FXML private javafx.scene.control.Button changeMasterPPButton;

    FXMLLoader loader = null;

    DatabaseEntryDao mainViewDao = new DatabaseEntryDao();


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
        System.out.print("the object: "+tmp.getDummyId()+","+tmp.getId()+", "+tmp.getUsername() + ", " + tmp.getPassword() + ", " + tmp.getUrl() + ", " + tmp.getHiddenPasswordTrick()
                +", "+tmp.getNote()+", "+tmp.getCreationDate()+", "+tmp.getLastUpdate());
        return tmp;
    }


    private void startMouseClicks() {
        //Methode to listen to mouse clicks
        profileTable.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if ((mouseEvent.getClickCount() == 2)) {
                    try {
                        startOpenSelectedItemsToView(copyClickedEntry());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //TODO open view of the selected item
                }
            }
        });
    }

    private void startContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addItemOption = new MenuItem("Add item");
        MenuItem modifyItemOption = new MenuItem("Modify item");
        MenuItem openURLOption = new MenuItem("Open url");
        MenuItem copyURLOption = new MenuItem("Copy url");
        MenuItem copyPasswordOption = new MenuItem("copy password");
        MenuItem deleteItemOption = new MenuItem("Delete item");


        addItemOption.setOnAction((actionEvent -> {
            addButton.fire();
        }));

        modifyItemOption.setOnAction(actionEvent -> {
            try {
                startOpenSelectedItemsToView(profileTable.getSelectionModel().getSelectedItem());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        openURLOption.setOnAction((event) -> {
            //copyClickedEntry();
            String url = profileTable.getSelectionModel().getSelectedItem().getUrl();
            openUrl(url);
        });

        copyURLOption.setOnAction((event) -> {
            //copyClickedEntry();
            String url = profileTable.getSelectionModel().getSelectedItem().getUrl();
            // send plain text password to clipboard
            ClipboardHandler clipboardHandler = new ClipboardHandler();
            clipboardHandler.copyPasswordToClipboard(url);
        });

        copyPasswordOption.setOnAction((event) -> {
            DatabaseEntry entry = profileTable.getSelectionModel().getSelectedItem();
            System.out.println(entry.getPassword());
            //copyClickedEntry();
            try {
                copyPassword(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        deleteItemOption.setOnAction((event) -> {
            //copyClickedEntry();
            deleteButton.fire();
        });


        contextMenu.getItems().addAll(addItemOption, modifyItemOption,openURLOption, copyURLOption, copyPasswordOption, deleteItemOption);
        profileTable.setContextMenu(contextMenu);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //debugging set onNewStartup = true for setup process and after the 1st launch as false
        // change databaseName or delete cerbytesdb file on your computer
        try {
            mainViewDao.setup();
        } catch (SQLException | ClassNotFoundException | InterruptedException e){
            System.out.println("everything is already set.");
        }
        startMouseClicks();
        startContextMenu();
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();


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

    @FXML
    private void reloadMainView(){
        loadDatabaseEntries(databaseEntries);
    }

    private void loadDatabaseEntries(ObservableList<DatabaseEntry> databaseEntries) {
        try {
            ObservableList<DatabaseEntry> entries = DatabaseEntryDao.getAll();
            databaseEntries.clear();
            databaseEntries.addAll(entries);
            populateTableView(databaseEntries);
            foundLabel.setText(valueOf(databaseEntries.size()));


        } catch (SQLException | ClassNotFoundException | InterruptedException throwables) {

        }
    }

    @FXML
    private void populateTableView(ObservableList<DatabaseEntry> entries) throws SQLException, ClassNotFoundException {
        profileTable.setItems(entries);


    }


    public void addNewItemAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((getClass().getResource("../gui/add_new_item_view.fxml")));
            Parent addItemParent = loader.load();
            addNewItemController controller = loader.getController();
            controller.createCombox();
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.setScene(new Scene(addItemParent, 600, 400));
            stage.showAndWait();
            reloadMainView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML private Button logoutButton;
    public void logoutAction(ActionEvent event) throws SQLException {
        System.out.println("Logout Action");
        //TODO: Add necessary methods to clear/reencrypt/delete before closing the app.
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        mainViewDao.shutdown();
        stage.close();
        System.exit(0);
    }

    /*
    * Given a string, it search a element corresponding in the entries.
    * if nothing is to be found, it throws an information.
     */

    public void searchAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
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
            foundLabel.setText(valueOf(resultSearch.size()));
        }

        System.out.println("Search Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.WAIT);

    }

    /*
     * copy password in computer memory.
     */
    private void copyPassword(DatabaseEntry dbEntry) throws Exception {
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
    }
    public void copyPasswordAction(ActionEvent event) throws Exception {
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
        copyPassword(dbEntry);

    }


    public void deleteProfileAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        DatabaseEntryDao deleter = new DatabaseEntryDao();
        String alertMessage = "The entry will be deleted. \nAre you sure ?";
        Optional<ButtonType> confirm = startAlert(alertMessage, Alert.AlertType.CONFIRMATION, "Confirmation" );
        if(confirm.get() == ButtonType.OK){
            try {
                deleter.delete(copyClickedEntry());
                System.out.println("entry has been deleted");
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("delete not working");
                throw e;
            }
        }
        reloadMainView();

    }

    public void deleteAccountAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
        String alertText = "Are you sure you want to delete your account? \n" +
                "There is no way back to that.";
        Optional<ButtonType> confirm = startAlert(alertText, Alert.AlertType.CONFIRMATION, "Confirmation");
        if(confirm.get() == ButtonType.OK){
            DatabaseEntryDao deleteDao = new DatabaseEntryDao();
            deleteDao.deleteAccount();
            logoutButton.fire();
        }else {
            System.out.println("discard.");
        }
    }

    private void startOpenSelectedItemsToView(DatabaseEntry dbentry) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../gui/add_new_item_view.fxml"));
        Parent viewItemControllerParent = loader.load();
        addNewItemController controller = loader.getController();
        controller.fillIn(copyClickedEntry());
        Stage stage = new Stage();
        stage.setTitle("View item");
        stage.setScene(new Scene(viewItemControllerParent, 600,400));
        stage.show();
    }



    public void changeMasterAction(ActionEvent actionEvent) throws IOException{

        System.out.println("Change Master Password Action");
            Stage stage = new Stage();
            changePasswordController changePassword = new changePasswordController();
            changePassword.getView(stage);
            stage.show();


        /*//TODO: Change password action
        Parent changePassword;
        try {
            changePassword = FXMLLoader.load(getClass().getResource("../gui/change_password_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Change Masterpassword");
            stage.setScene(new Scene(changePassword,600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Change Master Password Action");*/
    }



    public void changeMasterPPAction(ActionEvent actionEvent){

        //TODO: here we have to open login_view_masterpassphrase.fxml and aks for the passphrase or something similar
        //String oldPassPhrase = "password123";

        Parent changePassphrase;

        try {
            /*
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(FILENAME, oldPassPhrase);
            String originalContent = new String(decryptedText, UTF_8);

             */

            //TODO: then here we have to call the set_master_mpp_view.fxml and ask for the new passphrase

            Stage stage = new Stage();
            changePassphraseController changePP = new changePassphraseController();
            changePP.getView(stage);
            stage.show();

            /*changePassphrase = FXMLLoader.load(getClass().getResource("../gui/change_passphrase_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Change Passphrase");
            stage.setScene(new Scene(changePassphrase,600, 400));
            stage.show();*/


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void openUrl(String url){
        // open the url
        UrlOpener urlOpener = new UrlOpener();
        urlOpener.openURL(url);
    }

    public void onOpenUrl(ActionEvent actionEvent) {
        // first get url form selected row
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
        String url = dbEntry.getUrl();
        openUrl(url);


    }

    public void reload(ActionEvent actionEvent) {
        reloadMainView();

    }

    public void onModifyProfile(ActionEvent actionEvent) throws IOException {
        startOpenSelectedItemsToView(copyClickedEntry());
    }

    private Optional<ButtonType> startAlert(String alertText, Alert.AlertType TYPE, String confirmation) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../gui/alert_view.fxml"));
        Parent alertViewParent = loader.load();
        alertViewController controller = loader.getController();
        Optional<ButtonType> resultConfirm = controller.startAlertWindows(alertText, TYPE, confirmation);
        return resultConfirm;
    }

    @Override
    public void getView(Stage stage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/main_view_2.fxml"));
        Parent mainView = loader.load();
        //stage stage = new Stage();
        stage.setTitle("C3rBytes");
        stage.setScene(new Scene(mainView, 1020, 600));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }


}

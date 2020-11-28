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
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;
import sample.ch.ffhs.c3rbytes.utils.UrlOpener;

import javax.crypto.AEADBadTagException;
import javax.swing.text.TableView;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.valueOf;


public class mainViewController implements Initializable {
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
    public Button reloaddata;

    private boolean fromMainView = true;
    private boolean start = true;
    public static boolean reload = false;
    DatabaseEntryDao mainViewDao = new DatabaseEntryDao();

    @FXML
    public ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();

    public static final String FILENAME = "c3r.c3r";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;


    public DatabaseEntry copyClickedEntry() {
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


    public void startMouseClicks() {
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

    public void startContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUrlOption = new MenuItem("Copy url");
        MenuItem copyPasswordOption = new MenuItem("copy password");
        MenuItem deleteItemOption = new MenuItem("Delete item");

        copyUrlOption.setOnAction((event) -> {
            //copyClickedEntry();
            String url = profileTable.getSelectionModel().getSelectedItem().getUrl();
            openUrl(url);
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
            reload = true;
            //TODO call delete Mehtode;
            //TODO print alert when deleting entries
        });


        contextMenu.getItems().addAll(copyUrlOption, copyPasswordOption, deleteItemOption);
        profileTable.setContextMenu(contextMenu);


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    void reloadMainView(){
        loadDatabaseEntries(databaseEntries);
    }

    private void loadDatabaseEntries(ObservableList<DatabaseEntry> databaseEntries) {
        try {
            ObservableList<DatabaseEntry> entries = DatabaseEntryDao.getAll();
            databaseEntries.clear();
            databaseEntries.addAll(entries);
            populateTableView(databaseEntries);
            foundLabel.setText(valueOf(databaseEntries.size()));
            mainViewDao.setupEncryption();

        } catch (SQLException | ClassNotFoundException | InterruptedException throwables) {

        }
    }



    @FXML
    private void populateTableView(ObservableList<DatabaseEntry> entries) throws SQLException, ClassNotFoundException {
        profileTable.setItems(entries);


    }


    @FXML private Button addButton;
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML private Button logoutButton;
    public void logoutAction(ActionEvent event) throws SQLException {
        System.out.println("Logout Action");
        //TODO: Add necessary methods to clear/reencrypt/delete before closing the app.
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }


    @FXML private Button searchButton;
    public void searchAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        //String searchElement = searchField.getText();
        String searchElement="Mersid";
        DatabaseEntryDao searchDao = new DatabaseEntryDao();
        databaseEntries.clear();
        ObservableList<DatabaseEntry> resultSearch = FXCollections.observableArrayList();
        resultSearch = searchDao.searchElement(searchElement,databaseEntries);
        populateTableView(resultSearch);
        foundLabel.setText(valueOf(resultSearch.size()));
        System.out.println("Search Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.WAIT);

    }

    /*
     * copy password in computer memory.
     */
    public void copyPassword(DatabaseEntry dbEntry) throws Exception {
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

    @FXML private Button deleteButton;
    public void deleteProfileAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        showAlert();
        DatabaseEntryDao deleter = new DatabaseEntryDao();
        try {
            deleter.delete(copyClickedEntry());
            System.out.println("entry has been deleted");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("delete not working");
            throw e;
        }
        reload = true;

        /*
        TODO Implement alert windows: https://www.geeksforgeeks.org/javafx-alert-with-examples/
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../gui/alert_view.fxml"));
        Parent alertViewControllerParent = loader.load();
        alertViewController controller = loader.getController();
        controller.passTheData(copyClickedEntry());
        Stage stage = new Stage();
        stage.setTitle("Alert");
        stage.setScene(new Scene(alertViewControllerParent, 400,400));
        stage.show();
         */

    }

    public void showAlert(){
        Button b = new Button("Confirmation alert");
        Alert a = new Alert(Alert.AlertType.NONE);
        // action event
        EventHandler<ActionEvent> event = new
                EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        // set alert type
                        a.setAlertType(Alert.AlertType.CONFIRMATION);

                        // show the dialog
                        a.show();
                    }
                };
    }
    public void deleteAccountAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
        DatabaseEntryDao deleteDao = new DatabaseEntryDao();
        deleteDao.deleteAccount();
    }

    public void startOpenSelectedItemsToView(DatabaseEntry dbentry) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../gui/add_new_item_view.fxml"));
        Parent viewItemControllerParent = loader.load();
        addNewItemController controller = loader.getController();
        controller.fillIn(copyClickedEntry(),fromMainView);
        Stage stage = new Stage();
        stage.setTitle("View item");
        stage.setScene(new Scene(viewItemControllerParent, 600,400));
        stage.show();
    }


    @FXML private javafx.scene.control.Button changeMasterButton;
    public void changeMasterAction(ActionEvent actionEvent) {
        //TODO: Change password action
        System.out.println("Change Master Password Action");
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
        System.out.println("Change Master Password Action");
    }


    @FXML private javafx.scene.control.Button changeMasterPPButton;
    public void changeMasterPPAction(ActionEvent actionEvent){

        //TODO: here we have to open login_view_masterpassphrase.fxml and aks for the passphrase or something similar
        String oldPassPhrase = "leer";

        Parent changePassphrase;

        try {
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(FILENAME, oldPassPhrase);
            String originalContent = new String(decryptedText, UTF_8);


            //TODO: then here we have to call the set_master_mpp_view.fxml and ask for the new passphrase
            changePassphrase = FXMLLoader.load(getClass().getResource("../gui/change_passphrase_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Change Passphrase");
            stage.setScene(new Scene(changePassphrase,600, 400));
            stage.show();

            String newPassPhrase = "password123";
            fileEncrypterDecrypter.encryptFile(originalContent, FILENAME, newPassPhrase);


        } catch(AEADBadTagException e){
            System.out.println("PassPhrase change denied");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void openUrl(String url){
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


    public void getRow(MouseEvent actionEvent) {
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
        System.out.print(dbEntry.getId());
    }


    public void reload(ActionEvent actionEvent) {
        reloadMainView();

    }

    public void onModifyProfile(ActionEvent actionEvent) throws IOException {
        startOpenSelectedItemsToView(copyClickedEntry());
    }
}

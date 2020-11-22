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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
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
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.valueOf;


public class mainViewController implements Initializable {


    private static boolean isNew;
    @FXML
    private TextField entryIdText;
    @FXML
    private TextField entryCategoryText;
    @FXML
    private TextField entryUserNameText;
    @FXML
    private TextField entryPasswordText;
    @FXML
    private TextField entryUrlText;
    //TODO EntryDateCreationText and EntryLastUpdate

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
    public ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();

    public static final String FILENAME = "c3r.c3r";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    public void refresh() throws SQLException, ClassNotFoundException {
        profileTable.refresh();
        profileTable.setItems(populateTableViews(databaseEntries));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();

        //Methode to listen to mouse clicks
        profileTable.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if ((mouseEvent.getClickCount() == 2)) {
                    DatabaseEntry tmp = new DatabaseEntry(
                            profileTable.getSelectionModel().getSelectedItem().getId(),
                            profileTable.getSelectionModel().getSelectedItem().getUsername(),
                            profileTable.getSelectionModel().getSelectedItem().getDescription(),
                            profileTable.getSelectionModel().getSelectedItem().getUrl(),
                            profileTable.getSelectionModel().getSelectedItem().getPassword(),
                            profileTable.getSelectionModel().getSelectedItem().getCreationDate(),
                            profileTable.getSelectionModel().getSelectedItem().getLastUpdate());
                    System.out.println(profileTable.getSelectionModel().getSelectedItem().getId());
                    System.out.print(tmp.getUsername()+", "+tmp.getPassword()+", "+tmp.getUrl());
                    try {
                        startOpenSelectedItemsToView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //TODO open view of the selected item
                }
            }
            });


        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        userNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );

        passwordColumn.setCellValueFactory(
                new PropertyValueFactory<>("password")
        );

        urlColumn.setCellValueFactory(
                new PropertyValueFactory<>("url")
        );





        /*
        try {
            Connection connection = connectionFactory.getConnection();
            DatabaseEntryDao newDao = new DatabaseEntryDao();
            DatabaseEntryDao.getAll();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    */

        try {
            profileTable.setItems(populateTableViews(databaseEntries));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }





    }

    private static ObservableList populateTableViews(ObservableList entries) throws SQLException, ClassNotFoundException {
        entries.clear();
        DatabaseEntryDao.getAll(entries);
        return entries;
    }


    /*
    @FXML
    private void populateTableView(DatabaseEntryDao entry) throws ClassNotFoundException, SQLException {
        ObservableList<DatabaseEntryDao> entriesData = (ObservableList<DatabaseEntryDao>) DatabaseEntryDao.getAll();
        entriesData.add(entry);
        //populate TableView profileTable
        profileTable.setItems(entriesData);
    }
     */

    @FXML
    private void populateTableView(DatabaseEntry result) throws SQLException, ClassNotFoundException {

        /*
        ObservableList<DatabaseEntry> entriesData = FXCollections.observableArrayList();
        entriesData.add(result);
        //populate TableView profileTable
        profileTable.setItems(entriesData);
         */

    }

    @FXML
    private void populateTableViewAndDisplay(DatabaseEntry result) throws ClassNotFoundException, SQLException {
        if(result != null){
            populateTableView(result);

        }

    }

    @FXML private Button addButton;
    public void addNewItemAction(ActionEvent event){
        Parent addItem;
        try {
            addItem = FXMLLoader.load(getClass().getResource("../gui/add_new_item_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.setScene(new Scene(addItem, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML private Button logoutButton;
    public void logoutAction(ActionEvent event){
        System.out.println("Logout Action");
        //TODO: Add necessary methods to clear/reencrypt/delete before closing the app.
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void modifyProfileAction(ActionEvent event){
        //TODO: Open view_item.fxml and edit the entry.
        System.out.println("Modify Profile Action");

    }

    @FXML private Button searchButton;
    public void searchAction(ActionEvent event){
        //TODO: Search entries based on parameter.
        //TODO: Define how search works.
        System.out.println("Search Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.WAIT);
    }


    public void copyPasswordAction(ActionEvent event) throws Exception {
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");

        String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

        // get password of the selected row
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
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
    @FXML private Button deleteButton;
    public void deleteProfileAction(ActionEvent event){
        //TODO: Delete profile. Pop up alert.

        // Test to change cursor appearence to default. Its purpose is for after a search or load function to get back to default curser
        System.out.println("Delete Profile Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.DEFAULT);

    }

    public void changeMasterAction(ActionEvent actionEvent) {
        //TODO: Change password action
        System.out.println("Change Master Password Action");
    }

    public void deleteAccountAction(ActionEvent actionEvent) {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
    }
    //TODO: to delete?
    private TableView tableView;
    public static void startOpenSelectedItemsToView() throws IOException {
        isNew = false;
        FXMLLoader loader = new FXMLLoader(loginViewMasterpassphraseController.class.getResource("../gui/add_new_item_view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("C3rBytes Main");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    public void changeMasterPPAction(ActionEvent actionEvent){

        //TODO: here we have to open login_view_masterpassphrase.fxml and aks for the passphrase
        String oldPassPhrase = "das ist ein test";

        try {
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(FILENAME, oldPassPhrase);
            String originalContent = new String(decryptedText, UTF_8);


            //TODO: then here we have to call the set_master_mpp_view.fxml and ask for the new passphrase

            String newPassPhrase = "leer";
            fileEncrypterDecrypter.encryptFile(originalContent, FILENAME, newPassPhrase);


        } catch(AEADBadTagException e){
            System.out.println("PassPhrase change denied");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onOpenUrl(ActionEvent actionEvent) {
        // first get url form selected row
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
        String url = dbEntry.getUrl();

        // open the url
        UrlOpener urlOpener = new UrlOpener();
        urlOpener.openURL(url);
    }

    @FXML
    public void getRow(MouseEvent actionEvent) {
        DatabaseEntry dbEntry = profileTable.getSelectionModel().getSelectedItem();
        System.out.print(dbEntry.getId());
    }


}

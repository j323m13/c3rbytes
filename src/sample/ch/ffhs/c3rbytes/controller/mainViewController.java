package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;

import javax.swing.text.TableView;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.valueOf;


public class mainViewController implements Initializable {

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


    public static final String FILENAME = "c3r.c3r";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
        /*
        final ObservableList<DatabaseEntry> data = FXCollections.observableArrayList(
                new DatabaseEntry(1, "Jérémie", "Facebook", "www.facebook.com", "12345!!"),
                new DatabaseEntry(2,"Jérémie", "Facebook", "www.facebook.com", "12345!!")


        );
*/

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
            profileTable.setItems(DatabaseEntryDao.getAll(databaseEntries));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }





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

    @FXML private javafx.scene.control.Button addButton;
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


    @FXML private javafx.scene.control.Button logoutButton;
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

    @FXML private javafx.scene.control.Button searchButton;
    public void searchAction(ActionEvent event){
        //TODO: Search entries based on parameter.
        //TODO: Define how search works.
        System.out.println("Search Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.WAIT);
    }


    public void copyPasswordAction(ActionEvent event){
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");

        ClipboardHandler clipboardHandler = new ClipboardHandler();
        clipboardHandler.copyPasswordToClipboard("Test");

    }
    @FXML private javafx.scene.control.Button deleteButton;
    public void deleteProfileAction(ActionEvent event){
        //TODO: Delete profile. Pop up alert.
        System.out.println("Delete Profile Action");
        Scene scene = searchButton.getScene();
        scene.setCursor(Cursor.DEFAULT);
        // Code to delete Clipboardentry's
        //ClipboardHandler clipboardHandler = new ClipboardHandler();
        //clipboardHandler.clearClipboard();

    }

    public void changeMasterAction(ActionEvent actionEvent) {
        //TODO: Change password action
        System.out.println("Change Master Password Action");
    }

    public void deleteAccountAction(ActionEvent actionEvent) {
        //TODO: Define deleting account
        System.out.println("Delete Account Action");
    }

    private TableView tableView;


    public void changeMasterPPAction(ActionEvent actionEvent){

        // here we have to open login_view_masterpassphrase.fxml and aks for the passphrase
        String oldPassPhrase = "das ist ein test";

        try {
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(FILENAME, oldPassPhrase);
            String originalContent = new String(decryptedText, UTF_8);


            // then here we have to call the set_master_mpp_view.fxml and ask for the new passphrase

            String newPassPhrase = "leer";
            fileEncrypterDecrypter.encryptFile(originalContent, FILENAME, newPassPhrase);


        } catch(javax.crypto.AEADBadTagException e){
            System.out.println("PassPhrase change denied");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

package sample.ch.ffhs.c3bytes.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.ch.ffhs.c3bytes.dao.Dao;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3bytes.utils.ClipboardHandler;

import javax.swing.*;
import javax.swing.text.TableView;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class mainViewController {

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
    private TableColumn<DatabaseEntry, Integer> idColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> categoryColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> usernameColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> passwordColumn;
    @FXML
    private TableColumn<DatabaseEntry, String> urlColumn;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {

        //idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIdAsString()));
        //categoryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DatabaseEntry.getDescription()));
        //usernameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DatabaseEntry.getUsername()));
        //passwordColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DatabaseEntry.getPassword()));
        //urlColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DatabaseEntry.getUrl()));


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


        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../gui/add_new_item_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Parent root;
        try {

            ///FXMLLoader fxmlLoader = new FXMLLoader();
            root = FXMLLoader.load(getClass().getResource("../gui/add_new_item_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/

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

        /*
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> clipboardHandler.clearClipboard());
            }
        };

        System.out.println("Current time" + System.currentTimeMillis());
        timer.schedule(task,0, 5000);
        System.out.println("Current time" + System.currentTimeMillis());

        //clipboardHandler.clearClipboard();
*/
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
}

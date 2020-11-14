package sample.ch.ffhs.c3bytes.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ch.ffhs.c3bytes.dao.Dao;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntryDao;

import javax.swing.text.TableView;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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


    public void addNewItemAction(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("../gui/add_new_item_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add new Item");
            stage.setScene(new Scene(root, 400, 400));
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
    }

    public void modifyProfileAction(ActionEvent event){
        //TODO: Open view_item.fxml and edit the entry.
        System.out.println("Modify Profile Action");

    }


    public void searchAction(ActionEvent event){
        //TODO: Search entries based on parameter.
        //TODO: Define how search works.
        System.out.println("Search Action");
    }

    public void copyPasswordAction(ActionEvent event){
        //TODO: Copy password -> get password text field content
        System.out.println("Copy Password Action");
    }

    public void deleteProfileAction(ActionEvent event){
        //TODO: Delete profile. Pop up alert.
        System.out.println("Delete Profile Action");
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

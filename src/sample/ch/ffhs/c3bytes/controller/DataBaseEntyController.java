package sample.ch.ffhs.c3bytes.controller;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


import sample.ch.ffhs.c3bytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntryDao;

public class DataBaseEntyController {

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
    private TableView<DatabaseEntry> profileTable;

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
    private void initialize(){

        //idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIdAsString()));
        categoryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        usernameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPassword()));
        urlColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUrl()));

    }
    @FXML
    private void populateTableView(DatabaseEntry entry) throws ClassNotFoundException{
        ObservableList<DatabaseEntry> entriesData = FXCollections.observableArrayList();
        entriesData.add(entry);
        //populate TableView profileTable
        profileTable.setItems(entriesData);
    }

    @FXML
    private void populateTableViewAndDisplay(DatabaseEntry entries) throws ClassNotFoundException{
        if(entries != null){
            populateTableView(entries);

        }

    }




}

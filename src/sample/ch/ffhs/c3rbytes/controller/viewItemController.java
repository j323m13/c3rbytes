package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.sql.SQLException;

public class viewItemController {


        @FXML javafx.scene.control.TextField viewUserNameField;
        @FXML javafx.scene.control.PasswordField viewPasswordField;
        @FXML javafx.scene.control.TextField viewTypeField;
        @FXML javafx.scene.control.TextField viewUrlField;
        @FXML javafx.scene.control.TextArea viewNotesField;
        @FXML javafx.scene.control.Label viewLastUpdateLabel;
        @FXML javafx.scene.control.Button viewDiscardButton;
        private boolean update = false;




    public void fillIn(DatabaseEntry dbentry, boolean fromMainView){
        viewUserNameField.setText(dbentry.getUsername());
        viewPasswordField.setText(dbentry.getPassword());
        viewTypeField.setText(dbentry.getId());
        viewUrlField.setText(dbentry.getUrl());
        viewNotesField.setText(dbentry.getDescription());
        viewLastUpdateLabel.setText(dbentry.getLastUpdate());
        update = fromMainView;

    }

    private void insertDatabaseEntry(DatabaseEntry item) throws SQLException, ClassNotFoundException {
        //TODO: implement DatabaseEntryDao not static ??
        //DatabaseEntryDao.insertDatabaseEntry(item);
    }

    public void saveButton() throws SQLException, ClassNotFoundException {
        String userName = viewUserNameField.getText();
        String password = viewPasswordField.getText();
        String type = viewTypeField.getText();
        String url = viewUrlField.getText();
        String notes = viewNotesField.getText();
        DatabaseEntry updatedEntry = new DatabaseEntry(userName, password, type, url);
        DatabaseEntryDao updateDao = new DatabaseEntryDao();
        if(update){
            try {
                updateDao.update(updatedEntry);
            }catch (SQLException e){
                System.out.println(e);
            }

        }else{
            try {
                updateDao.insertDatabaseEntry(updatedEntry);
            }catch (SQLException e){
                System.out.print(e);
            }
        }



        discardButton();

    }

    public void discardButton(){
        Stage stage = (Stage)viewDiscardButton.getScene().getWindow();
        stage.close();
    }

}

package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;

public class viewItemController {

        @FXML javafx.scene.control.TextField viewUserNameField;
        @FXML javafx.scene.control.PasswordField viewPasswordField;
        @FXML javafx.scene.control.TextField viewTypeField;
        @FXML javafx.scene.control.TextField viewUrlField;
        @FXML javafx.scene.control.TextArea viewNotesField;
        @FXML javafx.scene.control.Label viewLastUpdateLabel;
        @FXML javafx.scene.control.Button viewDiscardButton;

    public void fillIn(DatabaseEntry dbentry){
        viewUserNameField.setText(dbentry.getUsername());
        viewPasswordField.setText(dbentry.getPassword());
        viewTypeField.setText(dbentry.getId());
        viewUrlField.setText(dbentry.getUrl());
        viewNotesField.setText(dbentry.getDescription());
    }

    public void saveButton(){
        String userName = viewUserNameField.getText();
        String password = viewPasswordField.getText();
        String type = viewTypeField.getText();
        String url = viewUrlField.getText();
        String notes = viewNotesField.getText();

        //TODO: @jeremie save to db

        viewLastUpdateLabel.setText("");

        discardButton();

    }

    public void discardButton(){
        Stage stage = (Stage)viewDiscardButton.getScene().getWindow();
        stage.close();
    }

}

package sample.ch.ffhs.c3bytes.controller;

import com.sun.javafx.css.parser.Token;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntryDao;

import java.awt.*;
import java.sql.SQLException;


public class addNewItemController {

    @FXML
    TextInputControl userNameField;
    @FXML
    TextInputControl passwordField;
    @FXML
    TextInputControl typeField;
    @FXML
    TextInputControl urlField;


    public void getValueFromTextField(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String username = userNameField.getText();
        String password = passwordField.getText();
        String description = typeField.getText();
        String url = urlField.getText();
        System.out.println(username);
        System.out.println(password);
        System.out.println(description);
        System.out.println(url);
        DatabaseEntry item = new DatabaseEntry(username, description, url, password);
        try {
            insertDatabaseEntry(item);
        } catch (Exception e) {
            System.out.print(e);
        }
    }


    private void insertDatabaseEntry(DatabaseEntry item) throws SQLException, ClassNotFoundException {
        DatabaseEntryDao.insertDatabaseEntry(item);
    }

}

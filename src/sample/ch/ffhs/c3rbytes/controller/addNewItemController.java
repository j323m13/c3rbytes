package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextInputControl;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.sql.SQLException;


public class addNewItemController {

    @FXML private javafx.scene.control.Button generatePasswordButton;
    public void generatePassword(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/password_generator_view.fxml"));
            Parent root = loader.load();

            passwordGeneratorController pwGenCon = loader.getController();
            pwGenCon.getpwdOutputTextField(passwordField);

            Stage stage = new Stage();
            stage.setTitle("Password-Generator");
            stage.setScene(new Scene(root, 545, 420));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public TextField passwordField;
    @FXML public TextField userNameField;
    public void fillPasswordField(String password){
        System.out.println("HEEH");
        passwordField.setText("hro");
        userNameField.setText("done");

    }

    public void write(String Te){
        System.out.println(Te);
    }

    @FXML private javafx.scene.control.Button showPasswordButton;
    public void showPassword(ActionEvent event){
        passwordField.setText("Test");
    }

    /*
    @FXML
    TextInputControl userNameField;
    @FXML
    TextInputControl passwordField;


     */
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

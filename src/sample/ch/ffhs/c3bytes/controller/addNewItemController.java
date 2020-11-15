package sample.ch.ffhs.c3bytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class addNewItemController {

    @FXML private javafx.scene.control.Button generatePasswordButton;
    public void generatePassword(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../gui/password_generator_view.fxml"));
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

}

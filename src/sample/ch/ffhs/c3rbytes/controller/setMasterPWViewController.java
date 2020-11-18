package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;

import java.io.IOException;
import java.sql.SQLException;

public class setMasterPWViewController {

    @FXML javafx.scene.control.PasswordField setMPViewPasswordField;
    @FXML javafx.scene.control.Button setMPViewloginButton;

    // password to encrypt DB
    public void saveMPAction(ActionEvent actionEvent) {
        String dBpassword = setMPViewPasswordField.getText();
        //DBConnection.bootPassword = dBpassword;
        System.out.println(dBpassword);

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/set_master_mpp_view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Welcome to C3rBytes");
            stage.setScene(new Scene(root, 552, 371));
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) setMPViewloginButton.getScene().getWindow();
        stage.close();
    }

    public void abordMPAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}

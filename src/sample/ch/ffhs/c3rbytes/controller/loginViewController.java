package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class loginViewController {
    @FXML private javafx.scene.control.TextField masterPasswordField;
    @FXML private javafx.scene.control.Button loginButton;
    @FXML private javafx.scene.control.Button logoutButton;

    public void loginAction() throws IOException, SQLException, ClassNotFoundException {
        System.out.println("LoginAction");
        // Here comes the db check, if mp is correct --> successful login if correct populate
        // to test set dbLogincorrect to true

        boolean dbLogincorrect = true;

        // password to forward to the db
        String mpTextField = masterPasswordField.getText();
        Connection connection = DBConnection.getConnection();


        if (dbLogincorrect){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
                Parent root = loader.load();
                //passwordGeneratorController pwGenCon = loader.getController();
                //pwGenCon.getpwdOutputTextField(passwordField);
                Stage stage = new Stage();
                stage.setTitle("C3rBytes Login Masterpassphrase");
                stage.setScene(new Scene(root, 552, 371));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage =  (Stage) loginButton.getScene().getWindow();
            stage.close();


        }
        else{
            System.out.println("Login failed");
        }

    }

    public void logoutAction(){
        System.out.println("LogoutAction");
        System.exit(0);
    }

}

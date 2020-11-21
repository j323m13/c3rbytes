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
    //@FXML private javafx.scene.control.TextField masterPasswordField;
    @FXML private javafx.scene.control.PasswordField loginViewPasswordField;
    @FXML private javafx.scene.control.Button loginButton;
    @FXML private javafx.scene.control.Button logoutButton;

    public void loginAction() throws IOException, SQLException, ClassNotFoundException {
        //TODO: Correct login authentication with dB
        System.out.println("LoginAction");
        // Here comes the db check, if mp is correct --> successful login if correct populate
        // to test set dbLogincorrect to true

        boolean dbLogincorrect = true;

        // password to forward to the db
        String mpTextField = loginViewPasswordField.getText();
        System.out.println("masterpassword: " + mpTextField);


        String bootPassword = "12345secureAF";
        DBConnection.bootPassword = bootPassword;

        // To test if an error is catched
        //public static String passwordDB = "1234";
        DBConnection.passwordDB = mpTextField;

        System.out.println("DBConnPW: " + DBConnection.passwordDB);

        try {

            DBConnection.getConnection();

            System.out.println("Access to DB granted");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
            Parent root = loader.load();
            //passwordGeneratorController pwGenCon = loader.getController();
            //pwGenCon.getpwdOutputTextField(passwordField);
            Stage stage = new Stage();
            stage.setTitle("C3rBytes Login Masterpassphrase");
            stage.setScene(new Scene(root, 552, 371));
            stage.show();

        }catch ( SQLException e){
            System.out.println("Access to DB denied");
        }catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage =  (Stage) loginButton.getScene().getWindow();
        stage.close();

    }

    public void logoutAction(){
        System.out.println("LogoutAction");
        System.exit(0);
    }

}

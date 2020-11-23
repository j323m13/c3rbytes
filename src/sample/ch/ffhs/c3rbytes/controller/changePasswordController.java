package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class changePasswordController {

    @FXML javafx.scene.control.TextField oldMasterPasswordField;
    @FXML javafx.scene.control.TextField newPasswordField;
    @FXML javafx.scene.control.TextField newPasswordConfirmField;
    @FXML javafx.scene.control.Label oldPasswordErrorLabel;
    @FXML javafx.scene.control.Label passwordMatchErrorLabel;

    private final String HASHALGORITHM = "SHA3-512";

    public void changePasswordAction(ActionEvent actionEvent) throws SQLException {
        String oldMasterpassword = oldMasterPasswordField.getText();
        String newMasterpassword = newPasswordField.getText();
        String newMasterpasswordConfirmed = newPasswordConfirmField.getText();
        boolean isFilledOut = true;

        oldPasswordErrorLabel.setText("");
        passwordMatchErrorLabel.setText("");

        if (oldMasterpassword.equals("") || oldMasterpassword.length() == 0){
            oldPasswordErrorLabel.setText("Please fill out old masterpassword");
            isFilledOut = false;
            System.out.println("fill out masterpassword");
        }

        if (newMasterpassword.equals("") || newMasterpassword.length() == 0){
            oldPasswordErrorLabel.setText("Please fill out new masterpassword");
            isFilledOut = false;
            System.out.println("fill out masterpassword");
        }

        if (newMasterpasswordConfirmed.equals("") || newMasterpasswordConfirmed.length() == 0){
            oldPasswordErrorLabel.setText("Please fill out confirmation for the new masterpassword");
            isFilledOut = false;
            System.out.println("fill out masterpassword");
        }

        if (isFilledOut && newMasterpassword.equals(newMasterpasswordConfirmed)) {
            try {
                StringHasher stringHasher = new StringHasher();
                String hashedOldMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, oldMasterpassword);
                String hashedNewMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, newMasterpassword).substring(0,24);

                //DBConnection.changebootPasswordAndEncryptDBWithNewBootPassword(hashedOldMasterpassword, hashedNewMasterpassword);
                DBConnection.changeBootPassword(oldMasterpassword, hashedNewMasterpassword);
                System.out.println(hashedNewMasterpassword);
                discardPasswordAction(null);
            }catch (Exception e){
                System.out.println("Password incorrect");
                oldPasswordErrorLabel.setText("Password incorrect");
                e.printStackTrace();
            }
        } else{
            passwordMatchErrorLabel.setText("New password does not match");
            System.out.println("New password does not match");
        }


    }

    @FXML javafx.scene.control.Button discardPasswordButton;
    public void discardPasswordAction(ActionEvent actionEvent) {
        Stage stage = (Stage) discardPasswordButton.getScene().getWindow();
        stage.close();
    }
}

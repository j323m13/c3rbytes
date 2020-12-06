package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class changePasswordController implements IController {

    @FXML javafx.scene.control.TextField oldMasterPasswordText;
    @FXML javafx.scene.control.TextField newPasswordText;
    @FXML javafx.scene.control.TextField newPasswordConfirmText;
    @FXML javafx.scene.control.Label oldPasswordErrorLabel;
    @FXML javafx.scene.control.Label passwordMatchErrorLabel;
    @FXML javafx.scene.control.PasswordField oldMasterPasswordField;
    @FXML javafx.scene.control.PasswordField newPasswordField;
    @FXML javafx.scene.control.PasswordField newPasswordConfirmField;
    private boolean isHidingOld = true;
    private boolean isHidingNew = true;
    private boolean isHidingConfirm = true;
    FXMLLoader loader = null;

    private final String HASHALGORITHM = "SHA3-512";

    public void changePasswordAction(ActionEvent actionEvent) throws SQLException {
        String oldMasterpassword = oldMasterPasswordText.getText();
        String newMasterpassword = newPasswordText.getText();
        String newMasterpasswordConfirmed = newPasswordConfirmText.getText();
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
                String hashedNewMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, newMasterpassword);
                String hashedNewPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM, hashedNewMasterpassword).substring(32,64);
                DatabaseEntryDao setup = new DatabaseEntryDao();
                setup.changeBootPassword(hashedNewMasterpassword,hashedNewPasswordDB);

                //DBConnection.changebootPasswordAndEncryptDBWithNewBootPassword(hashedOldMasterpassword, hashedNewMasterpassword);
                //DBConnection.changeBootPassword(oldMasterpassword, newMasterpassword);
                System.out.println(newMasterpassword);
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

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("change_password_view.fxml");
        loader = new FXMLLoader(url);
        Parent addNewItemView = loader.load();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(addNewItemView, 497, 313));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }


    public void showPasswordOld(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(oldMasterPasswordField, oldMasterPasswordText, isHidingOld);
        isHidingOld =! isHidingOld;
    }

    public void showPasswordNew(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(newPasswordField, newPasswordText, isHidingNew);
        isHidingNew =! isHidingNew;
    }

    public void showPasswordConfirm(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(newPasswordConfirmField, newPasswordConfirmText, isHidingConfirm);
        isHidingConfirm =! isHidingConfirm;
    }
}

package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;
import sample.ch.ffhs.c3rbytes.utils.PasswordValidator;
import java.io.IOException;
import java.net.URL;


/**
 * This class controls the changePWView
 *
 */
public class changePasswordController implements IController {

    @FXML javafx.scene.control.TextField oldMasterPasswordText;
    @FXML javafx.scene.control.TextField newPasswordText;
    @FXML javafx.scene.control.TextField newPasswordConfirmText;
    @FXML javafx.scene.control.Label oldPasswordErrorLabel;
    @FXML javafx.scene.control.Label passwordMatchErrorLabel;
    @FXML javafx.scene.control.PasswordField oldMasterPasswordField;
    @FXML javafx.scene.control.PasswordField newPasswordField;
    @FXML javafx.scene.control.PasswordField newPasswordConfirmField;
    @FXML javafx.scene.control.Button discardPasswordButton;
    private boolean isHidingOld = true;
    private boolean isHidingNew = true;
    private boolean isHidingConfirm = true;
    FXMLLoader loader = null;
    private final String HASHALGORITHM = "SHA3-512";
    DatabaseEntryDao setup = new DatabaseEntryDao();

    /**
     * This method compares the old with the new masterpassword. If correct it triggers the change
     * otherwise the aciton will be cancelled
     */
    public void changePasswordAction() {
        String oldMasterpassword = oldMasterPasswordField.getText();
        String newMasterpassword = newPasswordField.getText();
        String newMasterpasswordConfirmed = newPasswordConfirmField.getText();
        StringHasher stringHasher = new StringHasher();
        String oldMasterpasswordHashed = stringHasher.encryptSHA3(HASHALGORITHM, oldMasterpassword);

        System.out.println(oldMasterpassword + " " + newMasterpassword + " " + newMasterpasswordConfirmed);

        oldPasswordErrorLabel.setText("");
        passwordMatchErrorLabel.setText("");

        resetStyle(oldMasterPasswordField, oldMasterPasswordText);
        resetStyle(newPasswordField, newPasswordText);
        resetStyle(newPasswordConfirmField, newPasswordConfirmText);

        oldPasswordErrorLabel.setText("");
        passwordMatchErrorLabel.setText("");
        System.out.println(oldMasterpassword);

        PasswordValidator passwordValidator = new PasswordValidator();
        boolean oldPw = passwordValidator.checkFillOut(oldMasterpassword);
        boolean newPw = passwordValidator.checkFillOut(newMasterpassword);
        boolean newPwConfirmed = passwordValidator.checkFillOut(newMasterpasswordConfirmed);
        boolean isEqualNewPw = passwordValidator.isEqual(newMasterpassword, newMasterpasswordConfirmed);
        boolean oldPWisCorrect = passwordValidator.isEqual(oldMasterpasswordHashed, setup.getBootPasswordDAO());

        if (!oldPw){
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldMasterPasswordText.setText("Please fill out old master password");
            System.out.println("fill out masterpassphrase");
        }
        if (oldPw){
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldMasterPasswordText.setText("Please fill out old master password");
            System.out.println("fill out masterpassphrase");

        }
        if (!oldPWisCorrect){
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldPasswordErrorLabel.setText("old Master Password is not correct.");
            System.out.println("Enter the correct Master Password.");
        }
        if (!newPw){
            setStyle(newPasswordField, newPasswordText);
            passwordMatchErrorLabel.setText("Please fill out new master password");
            System.out.println("fill out masterpassphrase");
        }

        if (!newPwConfirmed){
            setStyle(newPasswordConfirmField, newPasswordConfirmText);
            passwordMatchErrorLabel.setText("Please confirm new master password");
            System.out.println("fill out masterpassphrase");
        }

        if (!isEqualNewPw){
            setStyle(newPasswordField, newPasswordText);
            setStyle(newPasswordConfirmField, newPasswordConfirmText);
            passwordMatchErrorLabel.setText("New password not correctly confirmed");
            System.out.println("fill out masterpassphrase");
        }
        if (oldPw && newPw && newPwConfirmed && isEqualNewPw && oldPWisCorrect){
            changePassword(oldMasterpassword, newMasterpasswordConfirmed);
        } else{
            passwordMatchErrorLabel.setText("Password change not possible");
            System.out.println("New password does not match");
        }
    }

    /**
     * This method sets the style of the passwordfild if input is invalid
     * @param passwordField The passwordfield
     * @param textField The textfield
     */
    private void setStyle(PasswordField passwordField, TextField textField){
        passwordField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
        textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
    }

    /**
     * This method resets the Fields
     * @param passwordField PasswordField
     * @param textField TextField
     */
    private void resetStyle(PasswordField passwordField, TextField textField){
        passwordField.setStyle(null);
        textField.setStyle(null);
    }

    /**
     * This method encrypts the db with the new masterpassword
     * @param oldMasterpassword String. The old masterpassword
     * @param newMasterpassword String. The new masterpassword
     */
    private void changePassword(String oldMasterpassword, String newMasterpassword) {
        try {
            StringHasher stringHasher = new StringHasher();
            String hashedOldMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, oldMasterpassword);
            String hashedNewMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, newMasterpassword);
            String hashedNewPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM, hashedNewMasterpassword).substring(32, 64);

            if(hashedOldMasterpassword.equals(setup.getBootPasswordDAO())){
                System.out.println("new bootPassword "+hashedNewMasterpassword);
                System.out.println("New passwordDB "+hashedNewPasswordDB);
                setup.changeBootPassword(hashedNewMasterpassword, hashedNewPasswordDB);
            }

            System.out.println(newMasterpassword);
            discardPasswordAction();
        } catch (Exception e) {
            System.out.println("Password incorrect");
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldPasswordErrorLabel.setText("Password incorrect");
            e.printStackTrace();
        }
    }


    public void discardPasswordAction() {
        Stage stage = (Stage) discardPasswordButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("change_password_view.fxml");
        loader = new FXMLLoader(url);
        Parent addNewItemView = loader.load();
        stage.setTitle("Change Password");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(addNewItemView, 497, 313));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }


    public void showPasswordOld() {
        new PasswordRevealer().passwordReveal(oldMasterPasswordField, oldMasterPasswordText, isHidingOld);
        isHidingOld =! isHidingOld;
    }

    public void showPasswordNew() {
        new PasswordRevealer().passwordReveal(newPasswordField, newPasswordText, isHidingNew);
        isHidingNew =! isHidingNew;
    }

    public void showPasswordConfirm() {
        new PasswordRevealer().passwordReveal(newPasswordConfirmField, newPasswordConfirmText, isHidingConfirm);
        isHidingConfirm =! isHidingConfirm;
    }

    public void updateOldPWPasswordField() {
        oldMasterPasswordField.setText(oldMasterPasswordText.getText());
    }

    public void updateNewPWPasswordField() {
        newPasswordField.setText(newPasswordText.getText());
    }

    public void updateConfirmedPWPasswordField() {
        newPasswordConfirmField.setText(newPasswordConfirmText.getText());
    }

    public void updateOldPWTextField() {
        oldMasterPasswordText.setText(oldMasterPasswordField.getText());
    }

    public void updateNewPWTextField() {
        newPasswordText.setText(newPasswordField.getText());
    }

    public void updateConfirmedPWTextField() {
        newPasswordConfirmText.setText(newPasswordConfirmField.getText());
    }
}

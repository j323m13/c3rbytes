package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;
import sample.ch.ffhs.c3rbytes.utils.PasswordValidator;

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
        String oldMasterpassword = oldMasterPasswordField.getText();
        String newMasterpassword = newPasswordField.getText();
        String newMasterpasswordConfirmed = newPasswordConfirmField.getText();
        boolean isFilledOut = true;

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

        if (oldPw && newPw && newPwConfirmed && isEqualNewPw){
            changePassword(oldMasterpassword, newMasterpasswordConfirmed);
        } else{
            passwordMatchErrorLabel.setText("Password change not possible");
            System.out.println("New password does not match");
        }

        if (!oldPw){
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldMasterPasswordText.setText("Please fill out old master password");
            System.out.println("fill out masterpassphrase");
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



        /*
        if (oldMasterpassphrase.equals("") || oldMasterpassphrase.length() == 0){
            oldPassphraseErrorLabel.setText("Please fill out old master pass phrase");
            isFilledOut = false;
            System.out.println("fill out masterpassphrase");
        }

        if (newMasterpassphrase.equals("") || newMasterpassphrase.length() == 0){
            passphraseMatchErrorLabel.setText("Please fill out new master pass phrase");
            isFilledOut = false;
            System.out.println("fill out masterpassphrase");
        }

        if (newMasterpassphraseConfirmed.equals("") || newMasterpassphraseConfirmed.length() == 0){
            passphraseMatchErrorLabel.setText("Please fill out confirmation for the new master pass phrase");
            isFilledOut = false;
            System.out.println("fill out masterpassphrase");
        }

        if (isFilledOut && newMasterpassphrase.equals(newMasterpassphraseConfirmed)) {
            try {
                FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
                byte[] decryptedText = fileEncrypterDecrypter.decryptFile(FILENAME, oldMasterpassphrase);
                String originalContent = new String(decryptedText, UTF_8);


                //String newPassPhrase = "password123";
                //String newPassPhrase = newPassphraseConfirmField.getText();
                fileEncrypterDecrypter.encryptFile(originalContent, FILENAME, newMasterpassphraseConfirmed);


                passphraseMatchErrorLabel.setText("Master pass phrase successful. Please discard the window");

            } catch(
                AEADBadTagException e){
                    System.out.println("Pass phrase change denied");
                    oldPassphraseErrorLabel.setText("Old pass phrase not correct");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            passphraseMatchErrorLabel.setText("New pass phrase does not match");
            System.out.println("New password does not match");
        }*/
    }

    private void setStyle(PasswordField passwordField, TextField textField){
        passwordField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
        textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
    }

    private void resetStyle(PasswordField passwordField, TextField textField){
        passwordField.setStyle(null);
        textField.setStyle(null);
        /*
        passwordField.getStyleClass().removeIf(style -> style.equals("passwordfieldstyle"));
        textField.getStyleClass().removeIf(style -> style.equals("textfieldstyle"));

         */
    }
        /*
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

         */


    private void changePassword(String oldMasterpassword, String newMasterpassword) {
        try {
            StringHasher stringHasher = new StringHasher();
            String hashedOldMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, oldMasterpassword);
            String hashedNewMasterpassword = stringHasher.encryptSHA3(HASHALGORITHM, newMasterpassword);
            String hashedNewPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM, hashedNewMasterpassword).substring(32, 64);
            DatabaseEntryDao setup = new DatabaseEntryDao();
            setup.changeBootPassword(hashedNewMasterpassword, hashedNewPasswordDB);

            //DBConnection.changebootPasswordAndEncryptDBWithNewBootPassword(hashedOldMasterpassword, hashedNewMasterpassword);
            //DBConnection.changeBootPassword(oldMasterpassword, newMasterpassword);
            System.out.println(newMasterpassword);
            discardPasswordAction(null);
        } catch (Exception e) {
            System.out.println("Password incorrect");
            setStyle(oldMasterPasswordField, oldMasterPasswordText);
            oldPasswordErrorLabel.setText("Password incorrect");
            e.printStackTrace();
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

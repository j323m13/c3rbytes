package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;
import sample.ch.ffhs.c3rbytes.utils.PasswordValidator;

import javax.crypto.AEADBadTagException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class changePassphraseController implements IController{

    public static final String FILENAME = "c3r.c3r";
    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    @FXML javafx.scene.control.TextField oldMasterPassphraseText;
    @FXML javafx.scene.control.TextField newPassphraseText;
    @FXML javafx.scene.control.TextField newPassphraseConfirmText;
    @FXML javafx.scene.control.PasswordField oldMasterPassphraseField;
    @FXML javafx.scene.control.PasswordField newPassphraseField;
    @FXML javafx.scene.control.PasswordField newPassphraseConfirmField;
    @FXML javafx.scene.control.Label oldPassphraseErrorLabel;
    @FXML javafx.scene.control.Label passphraseMatchErrorLabel;
    @FXML javafx.scene.control.Button savePassPhraseButton;
    private boolean isHidingOld = true;
    private boolean isHidingNew = true;
    private boolean isHidingConfirm = true;
    FXMLLoader loader = null;


    public void changePassphraseAction(ActionEvent actionEvent) {
        //String oldPassPhrase = "password123";
        String oldMasterpassphrase = oldMasterPassphraseField.getText();
        String newMasterpassphrase = newPassphraseField.getText();
        String newMasterpassphraseConfirmed = newPassphraseConfirmField.getText();

        resetStyle(oldMasterPassphraseField, oldMasterPassphraseText);
        resetStyle(newPassphraseField, newPassphraseText);
        resetStyle(newPassphraseConfirmField, newPassphraseConfirmText);


        oldPassphraseErrorLabel.setText("");
        passphraseMatchErrorLabel.setText("");
        System.out.println(oldMasterpassphrase);

        PasswordValidator passwordValidator = new PasswordValidator();
        boolean oldPw = passwordValidator.checkFillOut(oldMasterpassphrase);
        boolean newPw = passwordValidator.checkFillOut(newMasterpassphrase);
        boolean newPwConfirmed = passwordValidator.checkFillOut(newMasterpassphraseConfirmed);
        boolean isEqualNewPw = passwordValidator.isEqual(newMasterpassphrase, newMasterpassphraseConfirmed);

        if (oldPw && newPw && newPwConfirmed && isEqualNewPw){
            changePassPhrase(oldMasterpassphrase, newMasterpassphraseConfirmed);
        } else{
            passphraseMatchErrorLabel.setText("Pass phrase change not possible");
            System.out.println("New password does not match");
        }

        if (!oldPw){
            setStyle(oldMasterPassphraseField, oldMasterPassphraseText);
            oldPassphraseErrorLabel.setText("Please fill out old master pass phrase");
            System.out.println("fill out masterpassphrase");
        }

        if (!newPw){
            setStyle(newPassphraseField, newPassphraseText);
            passphraseMatchErrorLabel.setText("Please fill out new master pass phrase");
            System.out.println("fill out masterpassphrase");
        }

        if (!newPwConfirmed){
            setStyle(newPassphraseConfirmField, newPassphraseConfirmText);
            passphraseMatchErrorLabel.setText("Please confirm new master pass phrase");
            System.out.println("fill out masterpassphrase");
        }

        if (!isEqualNewPw){
            setStyle(newPassphraseField, newPassphraseText);
            setStyle(newPassphraseConfirmField, newPassphraseConfirmText);
            passphraseMatchErrorLabel.setText("New pass phrase not correctly confirmed");
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
    }

    private void changePassPhrase(String oldMasterpassphrase, String newMasterpassphraseConfirmed){
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
                setStyle(oldMasterPassphraseField, oldMasterPassphraseText);
                oldPassphraseErrorLabel.setText("Old pass phrase not correct");
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @FXML javafx.scene.control.Button discardPassphraseButton;
    public void discardPassphraseAction(ActionEvent actionEvent) {
        Stage stage = (Stage) discardPassphraseButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("change_passphrase_view.fxml");
        loader = new FXMLLoader(url);
        Parent changePassPhrase = loader.load();
        stage.setTitle("Change Passphrase");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(changePassPhrase, 493, 313));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    public void showPasswordOld(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(oldMasterPassphraseField, oldMasterPassphraseText, isHidingOld);
        isHidingOld =! isHidingOld;
    }

    public void showPasswordNew(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(newPassphraseField, newPassphraseText, isHidingNew);
        isHidingNew =! isHidingNew;
    }

    public void showPasswordConfirm(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(newPassphraseConfirmField, newPassphraseConfirmText, isHidingConfirm);
        isHidingConfirm =! isHidingConfirm;
    }

    public void updateOldPPPasswordText(KeyEvent keyEvent) {
        oldMasterPassphraseText.setText(oldMasterPassphraseField.getText());
    }

    public void updateOldPPPasswordField(KeyEvent keyEvent) {
        oldMasterPassphraseField.setText(oldMasterPassphraseText.getText());
    }

    public void updateNewPPPasswordField(KeyEvent keyEvent) {
        newPassphraseField.setText(newPassphraseText.getText());
    }

    public void updateNewPPTextField(KeyEvent keyEvent) {
        newPassphraseText.setText(newPassphraseField.getText());
    }

    public void updatePPConfPasswordField(KeyEvent keyEvent) {
        newPassphraseConfirmField.setText(newPassphraseConfirmText.getText());
    }

    public void updatePPConfTextField(KeyEvent keyEvent) {
        newPassphraseConfirmText.setText(newPassphraseConfirmField.getText());
    }


}

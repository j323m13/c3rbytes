package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import javax.crypto.AEADBadTagException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class changePassphraseController implements IController{

    public static final String FILENAME = ".c3r.c3r";
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
        String oldMasterpassphrase = oldMasterPassphraseText.getText();
        String newMasterpassphrase = newPassphraseText.getText();
        String newMasterpassphraseConfirmed = newPassphraseConfirmText.getText();
        boolean isFilledOut = true;

        oldPassphraseErrorLabel.setText("");
        passphraseMatchErrorLabel.setText("");
        System.out.println(oldMasterpassphrase);

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
}

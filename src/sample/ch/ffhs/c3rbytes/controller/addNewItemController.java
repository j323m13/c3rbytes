package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextInputControl;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;


public class addNewItemController {
    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    @FXML private javafx.scene.control.Button saveButton;
    @FXML private javafx.scene.control.Button generatePasswordButton;
    public void generatePassword(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/password_generator_view.fxml"));
            Parent root = loader.load();

            passwordGeneratorController pwGenCon = loader.getController();
            pwGenCon.getpwdOutputTextField(passwordField);

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

    /*
    @FXML
    TextInputControl userNameField;
    @FXML
    TextInputControl passwordField;


     */
    @FXML
    TextInputControl typeField;
    @FXML
    TextInputControl urlField;


    public void getValueFromTextField(ActionEvent actionEvent) throws Exception {

        String username = userNameField.getText();
        String password = passwordField.getText();
        String description = typeField.getText();
        String url = urlField.getText();
        System.out.println(username);
        System.out.println(password);
        System.out.println(description);
        System.out.println(url);

        ////TODO: !!!!!!! we have to find the passPhrase to decrypt the Key inside the c3r.c3r file
        //String passPhrase = "password123";

        String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

        System.out.println("passphrase: "+ passwordDecrypterPassword);

        /*
        // decrypt File
        FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
        // extract secretKey
        byte[] byteDecryptedKey = fileEncrypterDecrypter.decryptFile("c3r.c3r", passPhrase);


        System.out.println("decryptedKey: " + new String(byteDecryptedKey, UTF_8));
        String decryptedKey = new String(byteDecryptedKey, UTF_8);
        */

        // for passwordEncrypterDecrypter the password of the account is the plainText
        byte[] bytePassword = password.getBytes(UTF_8);

        // Encrypt Account-passwort with secretKey from File
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();
        String encryptedAccountPassword = passwordEncrypterDecrypter.encrypt(bytePassword, passwordDecrypterPassword);

        System.out.println("encryptedAccountPassword: " + encryptedAccountPassword);


        // decryption test
        String decryptedAccountPassword = passwordEncrypterDecrypter.decrypt(encryptedAccountPassword, passwordDecrypterPassword);

        System.out.println("decryptedAccountPassword: " + decryptedAccountPassword);


        DatabaseEntry item = new DatabaseEntry(null, username, description, url, encryptedAccountPassword, null, null);
        try {
            insertDatabaseEntry(item);
        } catch (Exception e) {
            System.out.print(e);
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        // !!!!we have to reload the table

    }


    private void insertDatabaseEntry(DatabaseEntry item) throws SQLException, ClassNotFoundException {
        DatabaseEntryDao.insertDatabaseEntry(item);
    }

    @FXML javafx.scene.control.Button discardButton;
    public void discardAction(ActionEvent actionEvent) {
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }
}

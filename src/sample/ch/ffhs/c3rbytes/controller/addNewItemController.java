package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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


public class addNewItemController implements IController {
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    @FXML public TextField passwordField;
    @FXML public TextField userNameField;
    @FXML private javafx.scene.control.Label viewLastUpdateLabel;
    private boolean update = false;
    @FXML private javafx.scene.control.Button saveButton;
    @FXML private javafx.scene.control.Button showPasswordButton;
    @FXML TextInputControl typeField;
    @FXML TextInputControl urlField;
    @FXML javafx.scene.control.Button discardButton;

    //@FXML private javafx.scene.control.Button generatePasswordButton;
    public void generatePassword(ActionEvent event){
        //TODO: for all views: find a better approach to handle stage changes
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

    public void write(String Te){
        System.out.println(Te);
    }
    public void showPassword(ActionEvent event){
        passwordField.setText("Test");
    }
    public void fillIn(DatabaseEntry dbentry, boolean fromMainView){
        userNameField.setText(dbentry.getUsername());
        passwordField.setText(dbentry.getPassword());
        urlField.setText(dbentry.getUrl());
        //viewNotesField.setText(dbentry.getDescription());
        viewLastUpdateLabel.setText(dbentry.getLastUpdate());

    }

    public void onDiscardButton(ActionEvent actionEvent) {
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }
    public void onSaveButton(ActionEvent actionEvent) throws Exception {
        String username = userNameField.getText();
        String password = passwordField.getText();
        String description = typeField.getText();
        String url = urlField.getText();
        System.out.println(username);
        System.out.println(password);
        System.out.println(description);
        System.out.println(url);
        DatabaseEntryDao updateDao = new DatabaseEntryDao();

        // get secretKey of the file .c3r.c3r
        String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

        // debugging
        System.out.println("passphrase: "+ passwordDecrypterPassword);

        // for passwordEncrypterDecrypter the password of the account is the plainText
        byte[] bytePassword = password.getBytes(UTF_8);

        // Encrypt Account-passwort with secretKey from File
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();
        String encryptedAccountPassword = passwordEncrypterDecrypter.encrypt(bytePassword, passwordDecrypterPassword);

        // debugging decryption test
        System.out.println("encryptedAccountPassword: " + encryptedAccountPassword);
        String decryptedAccountPassword = passwordEncrypterDecrypter.decrypt(encryptedAccountPassword, passwordDecrypterPassword);
        System.out.println("decryptedAccountPassword: " + decryptedAccountPassword);

        // save to DB
        if(update){
            try {
                updateDao.update(updateDao.createSimple(username, password, description, url));
                update = false;
            }catch (SQLException | ClassNotFoundException e){
                System.out.println(e);
            }

        }else{
            try {
                updateDao.insertDatabaseEntry(updateDao.createSimple(username, password, description, url));
            }catch (SQLException | ClassNotFoundException e){
                System.out.print(e);
            }
        }

        // close the window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        //reload mainview
        reloadMainView();

    }

    private void reloadMainView() {
        //TODO create relaod methode in main view and call it here.
    }

    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return null;
    }
}


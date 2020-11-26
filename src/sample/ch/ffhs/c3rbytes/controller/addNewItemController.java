package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import javax.xml.crypto.Data;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;


public class addNewItemController {
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    @FXML public TextField passwordField;
    @FXML public TextField userNameField;
    @FXML private TextField notesField;
    @FXML private javafx.scene.control.Label viewLastUpdateLabel;
    @FXML private javafx.scene.control.Button saveButton;
    @FXML private javafx.scene.control.Button showPasswordButton;
    @FXML TextField typeField;
    @FXML TextField urlField;
    private String id;
    private String creation;
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

    //@olaf delete?
    public void showPassword(ActionEvent event){
        passwordField.setText("Test");
    }

    /*
    methode to populate the field of for add new Item
     */
    public void fillIn(DatabaseEntry dbentry, boolean fromMainView){
        try{
            id = dbentry.getId();
            creation = dbentry.getCreationDate();
            userNameField.setText(dbentry.getUsername());
            passwordField.setText(dbentry.getPassword());
            urlField.setText(dbentry.getUrl());
            typeField.setText(dbentry.getDescription());
            notesField.setText(dbentry.getNote());
            viewLastUpdateLabel.setText(dbentry.getLastUpdate().toString());
        }catch (Exception e){
            System.out.println("a field is null.");
        }

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
        String notes = notesField.getText();


        //debugging
        System.out.println(id);
        System.out.println(username);
        System.out.println(password);
        System.out.println(description);
        System.out.println(url);
        System.out.println("creation date "+creation);
        System.out.println(notes);

        DatabaseEntryDao newDao = new DatabaseEntryDao();

        // get secretKey of the file c3r.c3r
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
        DatabaseEntry tmp = new DatabaseEntry();
        tmp.setUsername(username);
        tmp.setPassword(password);
        tmp.setDescription(description);
        tmp.setUrl(url);
        //debuging
        tmp.setNote(notes);
        //tmp.setNote(noteField.getText());
        if(tmp.getCreationDate()==null){
            tmp.setCreationDate(DatabaseEntry.getDateTime());
        }
        tmp.setLastUpdate(DatabaseEntry.getDateTime());
        tmp.setCreationDate(creation);
        if(id != null){
            tmp.setId(id);
        }

        // save to DB
        boolean reload;
        if(id!=null){
            try {
                System.out.println("update() -->");
                newDao.update(tmp);
                reload =true;
                reloadMainView(reload);
            }catch (SQLException | ClassNotFoundException e){
                System.out.println(e);
            }

        }else{
            try {
                System.out.println("save() -->");
                newDao.save(tmp);
                reload =true;
                reloadMainView(reload);
            }catch (SQLException | ClassNotFoundException e){
                System.out.print(e);
            }
        }

        // close the window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

    }
    /*
    * Methode to allow a reload from the MainView after updating, adding an item
    * @param reload which is boolean. default is false.
     */
    private void reloadMainView(boolean reload) throws IOException {
        mainViewController.reload = reload;
    }
}


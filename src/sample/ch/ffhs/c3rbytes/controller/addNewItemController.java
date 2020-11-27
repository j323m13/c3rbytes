package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
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
    @FXML private Label passwordFieldLabelError;
    @FXML private Label usernameFieldLabelError;
    @FXML private Label typeFieldLabelError;
    @FXML ChoiceBox<String> typeField;
    @FXML TextField urlField;
    private String id;
    private String creation;
    private boolean emptyField;
    @FXML javafx.scene.control.Button discardButton;

    public ObservableList<String> options = FXCollections.observableArrayList(
            "Social","Business", "Shopping", "Productivity", "Entertainment", "Family", "Health", "Other"
            );

    public void createCombox(){
        typeField.setItems(options);
        typeField.setValue(options.get(1).toString());
    }

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
        createCombox();
    }

    //@olaf delete?
    public void showPassword(ActionEvent event){
        passwordField.setText("Test");
    }

    /*
    methode to populate the field of for add new Item
     */
    public void fillIn(DatabaseEntry dbentry, boolean fromMainView){
        usernameFieldLabelError.setVisible(false);
        passwordFieldLabelError.setVisible(false);
        typeFieldLabelError.setVisible(false);
        try{
            id = dbentry.getId();
            System.out.println("id form fillIn() :"+id);
            creation = dbentry.getCreationDate();
            userNameField.setText(dbentry.getUsername());
            passwordField.setText(dbentry.getPassword());
            urlField.setText(dbentry.getUrl());
            //set Combox options
            typeField.setItems(options);
            //set option index from the option which was saved last time. return an index(int)
            typeField.setValue(options.get(options.indexOf(dbentry.getDescription())));
            notesField.setText(dbentry.getNote());
            viewLastUpdateLabel.setText(dbentry.getLastUpdate().toString());
        }catch (Exception e){
            System.out.println("a field is null.");
        }

    }

    public boolean controlFieldInput(){
        usernameFieldLabelError.setText("");
        passwordFieldLabelError.setText("");
        typeFieldLabelError.setText("");
        boolean isFilledOut = true;
        String type;
        try{
            type = typeField.getValue();
        }catch (Exception e){
            type = "";
        }

    if(userNameField.getText().equals("") || userNameField.getText().length()==0){
           usernameFieldLabelError.setVisible(true);
           usernameFieldLabelError.setText("Please enter a value.");
           userNameField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
           isFilledOut = false;
        }
    if (passwordField.getText().equals("") || passwordField.getText().length()==0){
            passwordFieldLabelError.setVisible(true);
            passwordFieldLabelError.setText("Please enter a password");
            passwordField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            isFilledOut = false;
        }
    if(type.equals("") || type.length()==0){
            typeFieldLabelError.setVisible(true);
            typeFieldLabelError.setText("Please choose a category");
            typeField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            isFilledOut = false;
        }
        return isFilledOut;
    }

    public void onDiscardButton(ActionEvent actionEvent) {
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }
    public void onSaveButton(ActionEvent actionEvent) throws Exception {
        if(controlFieldInput()){
            String username = userNameField.getText();
            String password = passwordField.getText();
            try{
                String description = typeField.getValue();
            }catch (Exception e){
                System.out.print("typefield is empty");
            }
            String url = urlField.getText();
            String notes = notesField.getText();


            //debugging
            System.out.println("id :"+id);
            System.out.println(username);
            System.out.println(password);
            //System.out.println(description);
            System.out.println(url);
            System.out.println("creation date "+creation);
            System.out.println(notes);
            System.out.print(typeField.getSelectionModel().getSelectedItem());

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
            try {
                tmp.setDescription(typeField.getValue());
            }catch (Exception e){
                System.out.print(e);
            }
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

    }
    /*
    * Methode to allow a reload from the MainView after updating, adding an item
    * @param reload which is boolean. default is false.
     */
    private void reloadMainView(boolean reload) throws IOException {
        mainViewController.reload = reload;
    }
}


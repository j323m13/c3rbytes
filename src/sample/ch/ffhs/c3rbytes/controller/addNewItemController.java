package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;


public class addNewItemController {
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    @FXML public PasswordField passwordField;
    @FXML private Label viewCreationDateLabel;
    private boolean isHidingPassword = true;
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
    @FXML private TextField showPasswordTextField;
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

    //show or hide passwordField
    public void showPassword(ActionEvent event){
        if (isHidingPassword) {
            String password = passwordField.getText();
            showPasswordTextField.setText(password);
            passwordField.setVisible(false);
            showPasswordTextField.setVisible(true);
            isHidingPassword = false;
        } else{
            String password = showPasswordTextField.getText();
            passwordField.setText(password);
            showPasswordTextField.setVisible(false);
            passwordField.setVisible(true);
            isHidingPassword = true;
        }

    }

    /*
    methode to populate the field of for add new Item
     */
    public void fillIn(DatabaseEntry dbentry){
        usernameFieldLabelError.setVisible(false);
        passwordFieldLabelError.setVisible(false);
        typeFieldLabelError.setVisible(false);
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();
        String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

        try{
            id = dbentry.getId();
            System.out.println("id form fillIn() :"+id);
            creation = dbentry.getCreationDate();
            userNameField.setText(dbentry.getUsername());
            String decryptedAccountPassword = passwordEncrypterDecrypter.decrypt(dbentry.getPassword(),
                    passwordDecrypterPassword);
            passwordField.setText(decryptedAccountPassword);
            urlField.setText(dbentry.getUrl());
            //set Combox options
            typeField.setItems(options);
            //set option index from the option which was saved last time. return an index(int)
            typeField.setValue(options.get(options.indexOf(dbentry.getDescription())));
            notesField.setText(dbentry.getNote());
            viewCreationDateLabel.setText("created on: "+dbentry.getCreationDate());
            viewLastUpdateLabel.setText("Last update: "+dbentry.getLastUpdate().toString());
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
            tmp.setPassword(encryptedAccountPassword);
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
            if(id!=null){
                try {
                    System.out.println("update() -->");
                    newDao.update(tmp);
                }catch (SQLException | ClassNotFoundException e){
                    System.out.println(e);
                }

            }else{
                try {
                    System.out.println("save() -->");
                    newDao.save(tmp);
                }catch (SQLException | ClassNotFoundException e){
                    System.out.print(e);
                }
            }

            // close the window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        }

    }

    public void copyPassword(ActionEvent actionEvent) {
        // get password from passwordField
        String accountPassword = passwordField.getText();

        System.out.println("decryptedAccountPassword: " + accountPassword);

        // send plain text password to clipboard
        ClipboardHandler clipboardHandler = new ClipboardHandler();
        clipboardHandler.copyPasswordToClipboard(accountPassword);

    }
}


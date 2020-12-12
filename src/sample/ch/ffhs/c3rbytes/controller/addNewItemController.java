package sample.ch.ffhs.c3rbytes.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;
import java.io.IOException;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * This class controls the addNewItemView and interacts with the model and crypto *
 */

public class addNewItemController implements IController {

    @FXML public PasswordField passwordField;
    @FXML private Label viewCreationDateLabel;
    @FXML public TextField userNameField;
    @FXML private TextField notesField;
    @FXML private javafx.scene.control.Label viewLastUpdateLabel;
    @FXML private javafx.scene.control.Button saveButton;
    @FXML private Label passwordFieldLabelError;
    @FXML private Label usernameFieldLabelError;
    @FXML private Label typeFieldLabelError;
    @FXML ChoiceBox<String> typeField;
    @FXML TextField urlField;
    @FXML private TextField showPasswordTextField;
    @FXML javafx.scene.control.Button discardButton;
    private String id;
    private String creation;
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    private boolean isHidingPassword = true;
    FXMLLoader loader = null;


    public ObservableList<String> options = FXCollections.observableArrayList(
            "Social","Business", "Shopping", "Productivity", "Entertainment", "Family", "Health", "Other"
    );

    /**
     * Create a comebox menu
     */
    public void createCombox(){
        typeField.setItems(options);
        typeField.setValue(options.get(1));
    }

    /**
     * Open a new view with a password generator
     * @throws IOException if password generation fails
     */
    //@FXML private javafx.scene.control.Button generatePasswordButton;
    public void generatePassword() throws IOException{
        Stage stage = new Stage();

        passwordGeneratorController passwordGenerator = new passwordGeneratorController();
        passwordGenerator.getView(stage);
        passwordGeneratorController pwGenController = (passwordGeneratorController)passwordGenerator.getController();
        pwGenController.getpwdOutputTextField(passwordField, showPasswordTextField);

        stage.show();
    }

    /**
     * Show the password in field password (view protected)
     */
    public void showPassword(){
        new PasswordRevealer().passwordReveal(passwordField, showPasswordTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }


    /**
     * methode to populate the field of for add new Item
     * @param dbentry if encryption goes wrong or db querie is invalid
     */
    public void fillIn(DatabaseEntry dbentry) {
        usernameFieldLabelError.setVisible(false);
        passwordFieldLabelError.setVisible(false);
        typeFieldLabelError.setVisible(false);


        try{
            id = dbentry.getId();
            System.out.println("id form fillIn() :"+id);
            creation = dbentry.getCreationDate();
            userNameField.setText(dbentry.getUsername());

            String passwordDecrypterPassword = loginViewMasterpassphraseController.passwordDecrypterPassword;

            // get password of the selected row
            String encryptedAccountPassword = dbentry.getPassword();

            // decrypt account password
            PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();
            String decryptedAccountPassword = passwordEncrypterDecrypter.decrypt(encryptedAccountPassword,
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

    /**
     * control if the mandatory fields are note empty.
     * @return isFilledOut
     */
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

    /**
     * discard action
     */
    public void onDiscardButton() {
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Save action
     * @throws Exception if password encryption is wrong or db querie is invalid
     */
    public void onSaveButton() throws Exception {
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

    /**
     * Copy password in memory
     */
    public void copyPassword() {
        // get password from passwordField
        String accountPassword = passwordField.getText();

        System.out.println("decryptedAccountPassword: " + accountPassword);

        // send plain text password to clipboard
        ClipboardHandler clipboardHandler = new ClipboardHandler();
        clipboardHandler.copyPasswordToClipboard(accountPassword);

    }

    /**
     * Get the view
     * @param stage
     * @throws IOException if loader or stage is null
     */
    @Override
    public void getView(Stage stage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/add_new_item_view.fxml"));
        Parent addNewItemView = loader.load();
        stage.setTitle("Add new item");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(addNewItemView, 586, 342));
    }

    /**
     * get controller
     * @return loader.getController()
     * @throws IOException if controller is not available
     */
    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    /**
     * Update the passwordfield (show vs hide password)
     * @param inputMethodEvent From which element the event comes
     */
    public void update(InputMethodEvent inputMethodEvent) {
        Node n = (Node)inputMethodEvent.getSource();
        String id = n.getId();

        switch (id) {
            case "passwordField":
                updateTextField();
                break;

            case "showPasswordTextField":
                updatePasswordField();
                break;
        }
    }

    /**
     * update the passwordField
     */
    public void updatePasswordField() {
        passwordField.setText(showPasswordTextField.getText());
    }

    /**
     * update the textField (password)
     */
    public void updateTextField() {
        showPasswordTextField.setText(passwordField.getText());
    }
}


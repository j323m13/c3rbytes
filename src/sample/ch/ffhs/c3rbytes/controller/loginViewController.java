package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;


/**
 * This class controls the login View
 */
public class loginViewController implements IController{

    @FXML private javafx.scene.control.PasswordField loginViewPasswordField;
    @FXML private javafx.scene.control.Button loginButton;
    @FXML private javafx.scene.control.Label wrongLoginLabel;
    @FXML private javafx.scene.control.TextField loginViewPasswordTextField;
    private final String HASHALGORITHM = "SHA3-512";
    private int loginCounter = 0;
    public boolean isHidingPassword = true;
    FXMLLoader loader = null;

    /**
     * This method logs in if password is correct
     */
    public void loginAction(){
        System.out.println("LoginAction");

        // password to forward to the db
        String mpTextField = loginViewPasswordField.getText();
        System.out.println("masterpassword: " + mpTextField);

        try {
            //TODO change this line for an tmp line. if not security risk (password in clear text)
            //BootPassword should not be transmitted in clear like this. unnecessary.
            //String tmpBootPassword= mpTextField;
            StringHasher stringHasher = new StringHasher();
            String hashedBootPassword = stringHasher.encryptSHA3(HASHALGORITHM,mpTextField);
            String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,hashedBootPassword).substring(32,64);

            DatabaseEntryDao login = new DatabaseEntryDao();
            login.setBootPasswordDAO(hashedBootPassword);
            login.setPasswordDBDAO(hashedPasswordDB);

            //decryptDB or createdB on first boot
            //if master password is not correct, then an exception is raised. DB will not boot with the wrong password.
            //if there is no DB, then a DB will be created with the master password.
            startDatabaseWithEncryptionBootPassword(login, hashedBootPassword, hashedPasswordDB);
            Stage stage = new Stage();
            loginViewMasterpassphraseController loginPassphrase = new loginViewMasterpassphraseController();
            loginPassphrase.getView(stage);
            stage.show();

            stage =  (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (SQLException | IOException e) {
            setLoginCounter();
            System.out.println("Access to DB denied: "+e);
        }
    }



    private void setLoginCounter() {
        loginCounter++;
        int leftLogins = 3 - loginCounter;
        System.out.println(leftLogins);
        loginViewPasswordField.setText("");
        loginViewPasswordField.requestFocus();
        wrongLoginLabel.setText("Login failed. " + leftLogins + " attempts left");
        if (loginCounter == 3){
            logoutAction();
        }
    }

    public void logoutAction(){
        System.out.println("LogoutAction");
        System.exit(0);
    }

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("login_view.fxml");
        loader = new FXMLLoader(url);
        Parent loginViewMP = loader.load();
        //stage stage = new Stage();
        stage.setTitle("C3rBytes Login Masterpassword");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(loginViewMP, 552, 371));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    /**
     * This method manges the key inputs of the fields
     * @param keyEvent The key event
     */
    public void manageKeyInput(KeyEvent keyEvent) {

        System.out.println(keyEvent.getSource());
        Node n = (Node)keyEvent.getSource();
        String id = n.getId();

        switch (id) {
            case "loginViewPasswordField":
                loginViewPasswordTextField.setText(loginViewPasswordField.getText());
                break;

            case "loginViewPasswordTextField":
                loginViewPasswordField.setText(loginViewPasswordTextField.getText());
                break;
        }

        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            loginAction();
        }
    }

    /**
     * This method shows or hides password
     */
    public void showPassword() {
        new PasswordRevealer().passwordReveal(loginViewPasswordField, loginViewPasswordTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }

    /**
     * Start the encrypted database with the hashed bootPassword
     * save the hashedBootPassword as the bootPassword variable (DBConnection.java)
     * save the hashedPasswordDB as the passwordDB variable (DBConnection.java)
     * @param dao a DatabaseEntryDAO object to initiate the login procedure
     * @param hashedBootPassword the bootPassword value (hash), which the encryption key of the DB.
     * @param hashedPasswordDB the passwordDB value (hash), which is the password of the DB user
     * @throws SQLException if a wrong bootPassword is given, a sql exception is thrown and catch here. the DB cannot
     * start with a wrong password. the setLoginCounter() +1
     */
    private void startDatabaseWithEncryptionBootPassword(DatabaseEntryDao dao, String hashedBootPassword, String hashedPasswordDB) throws SQLException {
        dao.setBootPasswordDAO(hashedBootPassword);
        dao.setPasswordDBDAO(hashedPasswordDB);
        dao.connect();
    }
}

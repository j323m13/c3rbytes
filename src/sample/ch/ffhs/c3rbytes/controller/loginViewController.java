package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.connection.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class loginViewController implements IController{

    //@FXML private javafx.scene.control.TextField masterPasswordField;
    @FXML private javafx.scene.control.PasswordField loginViewPasswordField;
    @FXML private javafx.scene.control.Button loginButton;
    @FXML private javafx.scene.control.Button logoutButton;
    @FXML private javafx.scene.control.Label wrongLoginLabel;
    @FXML private javafx.scene.control.TextField loginViewPasswordTextField;
    private final String HASHALGORITHM = "SHA3-512";
    private int loginCounter = 0;
    public boolean isHidingPassword = true;
    FXMLLoader loader = null;

    public void loginAction(javafx.event.ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        //TODO: Correct login authentication with dB
        System.out.println("LoginAction");
        // Here comes the db check, if mp is correct --> successful login if correct populate
        // to test set dbLogincorrect to true

        //boolean dbLogincorrect = true;


        // password to forward to the db
        String mpTextField = loginViewPasswordField.getText();
        System.out.println("masterpassword: " + mpTextField);

        try {

            DBConnection.bootPassword = mpTextField;
            StringHasher stringHasher = new StringHasher();
            String hashedBootPassword = stringHasher.encryptSHA3(HASHALGORITHM,DBConnection.bootPassword);
            String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,hashedBootPassword).substring(32,64);

            //String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,DBConnection.bootPassword);
            DBConnection.bootPassword = hashedBootPassword;
            DBConnection.passwordDB = hashedPasswordDB;
            DatabaseEntryDao login = new DatabaseEntryDao();
            //decryptDB or createdB on first boot
            login.setupEncryption(hashedBootPassword);


            //DBConnection.bootPassword = hashedPasswordDB;


        //System.out.println("DBConnPW: " + DBConnection.passwordDB);

        //debugging for the url

            Stage stage = new Stage();
            loginViewMasterpassphraseController loginPassphrase = new loginViewMasterpassphraseController();
            loginPassphrase.getView(stage);
            stage.show();


            /*loginViewMasterpassphraseController loginViewMasterpassphraseController = new loginViewMasterpassphraseController();
            loginViewMasterpassphraseController.getLoginViewMasterpassphrase(actionEvent);*/


            /*
            System.out.println("Access to DB granted");
            Parent parent = FXMLLoader.load(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
            Scene loginView = new Scene(parent);

            //This line gets the Stage information
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            window.setScene(loginView);
            window.show();
            */
            stage =  (Stage) loginButton.getScene().getWindow();
            stage.close();

        }catch (SQLException | InterruptedException e){
            loginCounter++;
            int leftLogins = 3 - loginCounter;
            System.out.println(leftLogins);
            loginViewPasswordField.setText("");
            loginViewPasswordField.requestFocus();
            wrongLoginLabel.setText("Login failed. " + leftLogins + " attempts left");
            if (loginCounter == 3){
                logoutAction();
            }

            System.out.println("Access to DB denied");
        }catch (IOException e) {
            e.printStackTrace();
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
        stage.setScene(new Scene(loginViewMP, 552, 371));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    public void manageKeyInput(KeyEvent keyEvent) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("Key released");
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            ActionEvent ae = new ActionEvent(keyEvent.getSource(), keyEvent.getTarget());
            loginAction(ae);
        }
    }

    public void showPassword(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(loginViewPasswordField, loginViewPasswordTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }



    /*
    public void loginAction(javafx.event.ActionEvent actionEvent) {

    }*/
}

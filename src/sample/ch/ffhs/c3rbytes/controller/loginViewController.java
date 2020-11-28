package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.io.IOException;
import java.sql.SQLException;

public class loginViewController implements IController{

    //@FXML private javafx.scene.control.TextField masterPasswordField;
    @FXML private javafx.scene.control.PasswordField loginViewPasswordField;
    @FXML private javafx.scene.control.Button loginButton;
    @FXML private javafx.scene.control.Button logoutButton;
    @FXML private javafx.scene.control.Label wrongLoginLabel;
    private final String HASHALGORITHM = "SHA3-512";
    private int loginCounter = 0;
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

        //TODO:hash masterpw and pass it to bootPassword
        DBConnection.bootPassword = mpTextField;
        StringHasher stringHasher = new StringHasher();
        //String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,DBConnection.bootPassword).substring(0,24);
        //String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,DBConnection.bootPassword);

        //DBConnection.bootPassword = hashedPasswordDB;


        //System.out.println("DBConnPW: " + DBConnection.passwordDB);

        //debugging for the url


        try {
            DatabaseEntryDao login = new DatabaseEntryDao();
            System.out.println(login.getUrl());
            //setup the database on first launch. does not work.
            //login.setup("123456789","654321654321");
            //login.setupUserDBWithPassword();
            //login.setupTable();

            login.connect();

            loginViewMasterpassphraseController loginViewMasterpassphraseController = new loginViewMasterpassphraseController();
            loginViewMasterpassphraseController.getLoginViewMasterpassphrase(actionEvent);

            /*
            System.out.println("Access to DB granted");
            Parent parent = FXMLLoader.load(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
            Scene loginView = new Scene(parent);

            //This line gets the Stage information
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            window.setScene(loginView);
            window.show();
            */


        }catch ( SQLException e){
            loginCounter++;
            int leftLogins = 3 - loginCounter;
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

        //Stage stage =  (Stage) loginButton.getScene().getWindow();
        //stage.close();

    }

    public void logoutAction(){
        System.out.println("LogoutAction");
        System.exit(0);
    }

    @Override
    public void getView(Stage stage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/login_view.fxml"));
        Parent loginViewMP = loader.load();
        //stage stage = new Stage();
        stage.setTitle("C3rBytes Login Masterpassword");
        stage.setScene(new Scene(loginViewMP, 552, 371));
    }



    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }



    /*
    public void loginAction(javafx.event.ActionEvent actionEvent) {

    }*/
}
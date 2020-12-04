package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.main.Main;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

public class loginViewMasterpassphraseController implements IController {
    @FXML private javafx.scene.control.PasswordField masterPassPhraseField;
    @FXML private javafx.scene.control.TextField loginMPPTextField;
    @FXML private javafx.scene.control.Button loginButtonMPP;
    @FXML private javafx.scene.control.Button logoutButtonMPP;
    @FXML private javafx.scene.control.Label wrongLogin;
    @FXML private javafx.scene.control.Label information;
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    private final String filename = ".c3r.c3r";
    public static String passwordDecrypterPassword;
    private int loginCounter = 0;
    private ArrayList<String> holder = new ArrayList<>();
    FXMLLoader loader = null;
    private boolean isHidingPassword = true;
    Stage stage = null;
    DatabaseEntryDao mainViewDao = new DatabaseEntryDao();

    public void loginActionMPP() throws Exception {

        // check pw in .c3r.c3r file and assign to passwordDecrypterpassword
        String masterPassPhrase = masterPassPhraseField.getText();
        DatabaseEntryDao setupEncryptionDao = new DatabaseEntryDao();
        //setupEncryptionDao.setupEncryption();

        try {
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(filename, masterPassPhrase);

            passwordDecrypterPassword = new String(decryptedText, UTF_8);

            // debugging
            System.out.println("passwordDecrypterPassword generated from pwgeneretaor" + passwordDecrypterPassword);
            System.out.println("Access granted");

            // start mainView()

            information.setVisible(true);
            information.setText("We are getting the system ready for. one moment.");
            startMainView();

            // close stage
            Stage stage =  (Stage) loginButtonMPP.getScene().getWindow();
            stage.close();
        }catch(javax.crypto.AEADBadTagException e){
            denied();/*
            //TODO: Here should come a notification to the user --> login failed
            System.out.println("Access denied due passphrase error");
            loginCounter++;
            System.out.println(loginCounter);
            int leftLogins = 3 - loginCounter;
            masterPassPhraseField.setText("");
            masterPassPhraseField.requestFocus();
            wrongLogin.setText("Login failed. " + leftLogins + " attempts left");
            if (loginCounter == 3){
                System.out.println("logoutAciton");
                logoutActionMPP();
            }*/
        }catch (IOException e) {
            denied();
        e.printStackTrace();
        }
    }

    public void denied(){
        //TODO: Here should come a notification to the user --> login failed
        System.out.println("Access denied due passphrase error");
        loginCounter++;
        System.out.println(loginCounter);
        int leftLogins = 3 - loginCounter;
        masterPassPhraseField.setText("");
        masterPassPhraseField.requestFocus();
        wrongLogin.setText("Login failed. " + leftLogins + " attempts left");
        if (loginCounter == 3){
            System.out.println("logoutAciton");
            logoutActionMPP();
        }
    }

    public void logoutActionMPP(){
        System.out.println("System exited");
        System.exit(0);
    }
    public ArrayList<String> hold(String passwordDB, String bootPassword){
        holder.add(passwordDB);
        holder.add(bootPassword);
        return holder;
    }

    public void startMainView() throws IOException{
        stage = new Stage();
        mainViewController mainViewController = new mainViewController();
        mainViewController.getView(stage);

        /*FXMLLoader loader = new FXMLLoader(loginViewMasterpassphraseController.class.getResource("../gui/main_view_2.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("C3rBytes Main");
        stage.setScene(new Scene(root, 1200, 600));*/
        stage.show();
    }

    public void getLoginViewMasterpassphrase(javafx.event.ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
        Parent parent = loader.load();
        Scene loginView = new Scene(parent);
        //Stage stage = new Stage();
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("C3rBytes Login Masterpassword");
        window.setScene(loginView);
        window.show();

        /*
        stage.setTitle("C3rBytes Login Masterpassword");
        stage.setScene(new Scene(loginView, 552, 371));

        /*
        Parent parent = FXMLLoader.load(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
        Scene loginView = new Scene(parent);
*/
        //This line gets the Stage information




    }


    public void showPassword(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(masterPassPhraseField, loginMPPTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }


    /*public void getView(javafx.event.ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
        Parent parent = loader.load();
        Scene loginView = new Scene(parent);
        //Stage stage = new Stage();
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("C3rBytes Login Masterpassword");
        window.setScene(loginView);
        window.show();

    }*/

    @Override
    public void getView(Stage stage) throws IOException {
        //this.stage = stage;
        loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
        Parent loginViewMPP = loader.load();
        //stage stage = new Stage();
        stage.setTitle("C3rBytes Login Master Passphrase");
        stage.setScene(new Scene(loginViewMPP, 552, 371));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }


    public void manageKeyInput(KeyEvent keyEvent) throws Exception {
        System.out.println("Key released");
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            try {
                loginActionMPP();
            } catch (Exception e){
                denied();
            }
        }
    }

/*
    public void closeStage() {
        stage.close();
    }

 */
}

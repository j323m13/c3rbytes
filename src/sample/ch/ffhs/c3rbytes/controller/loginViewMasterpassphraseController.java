package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.main.Main;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class loginViewMasterpassphraseController implements IController {
    @FXML private javafx.scene.control.PasswordField masterPassPhraseField;
    @FXML private javafx.scene.control.Button loginButtonMPP;
    @FXML private javafx.scene.control.Button logoutButtonMPP;
    @FXML private javafx.scene.control.Label wrongLogin;
    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    private final String filename = ".c3r.c3r";
    public static String passwordDecrypterPassword;
    private int loginCounter = 0;
    FXMLLoader loader = null;

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

            startMainView();
        }catch(javax.crypto.AEADBadTagException e){
            //TODO: Here should come a notification to the user --> login failed
            System.out.println("Access denied");
            loginCounter++;
            int leftLogins = 3 - loginCounter;
            masterPassPhraseField.setText("");
            masterPassPhraseField.requestFocus();
            wrongLogin.setText("Login failed. " + leftLogins + " attempts left");
            if (loginCounter == 3){
                logoutActionMPP();
            }
        }catch (IOException e) {
        e.printStackTrace();
    }

        Stage stage =  (Stage) loginButtonMPP.getScene().getWindow();
        stage.close();
    }

    public void logoutActionMPP(){
        System.out.println("System exited");
        System.exit(0);
    }

    public void startMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(loginViewMasterpassphraseController.class.getResource("../gui/main_view_2.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("C3rBytes Main");
        stage.setScene(new Scene(root, 1200, 600));
        stage.show();
    }

    public void getLoginViewMasterpassphrase(javafx.event.ActionEvent actionEvent) throws IOException {
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


    public void getView(javafx.event.ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/login_view_masterpassphrase.fxml"));
        Parent parent = loader.load();
        Scene loginView = new Scene(parent);
        //Stage stage = new Stage();
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("C3rBytes Login Masterpassword");
        window.setScene(loginView);
        window.show();

    }

    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }
}
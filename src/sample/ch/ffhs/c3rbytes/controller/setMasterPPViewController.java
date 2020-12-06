package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordGenerator;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

//import static sample.ch.ffhs.c3rbytes.controller.loginViewMasterpassphraseController.startMainView;

public class setMasterPPViewController implements IController {
    public final String filename = "c3r.c3r";

    @FXML javafx.scene.control.PasswordField setMPPViewPasswordField;
    @FXML javafx.scene.control.TextField setMPPViewTextField;
    private boolean isHidingPassword = true;
    @FXML javafx.scene.control.Button setMPPViewloginButton;
    FXMLLoader loader = null;
    DatabaseEntryDao newStartUp = new DatabaseEntryDao();


    public void saveMPPAction() throws Exception {
        String masterPassPhrase = setMPPViewPasswordField.getText();
        System.out.println(masterPassPhrase);

        ArrayList<Integer> charSet = new ArrayList<>();

        charSet.add(0);
        charSet.add(1);
        charSet.add(2);
        charSet.add(3);

        int passwordLength = 32;

        PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.buildPassword(charSet, passwordLength);
        String passwordDecrypterPassword = passwordGenerator.generatePassword();



        try {
            loginViewMasterpassphraseController.passwordDecrypterPassword = masterPassPhrase;
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            fileEncrypterDecrypter.encryptFile(passwordDecrypterPassword, filename, masterPassPhrase);

            newStartUp.setup();



        } catch(Exception e){
            // Reset Settings by deleting .c3r.c3r
            FileHandler fileHandler = new FileHandler();
            fileHandler.deleteFile(".c3r.c3r");
            newStartUp.deleteAccount();
        }


        /*
        Stage stage = (Stage) setMPPViewloginButton.getScene().getWindow();
        stage.close();
        */

        //Main.entryView(stage);
        Stage stage = new Stage();
        mainViewController mainViewController = new mainViewController();
        mainViewController.getView(stage);
        stage.show();
        stage = (Stage) setMPPViewloginButton.getScene().getWindow();
        stage.close();

        //lvmc.closeStage();
        //abordMPPAction();
        //startMainView();
    }

    public void abordMPPAction() throws SQLException, ClassNotFoundException {
        System.out.println("System exit");
        newStartUp.deleteAccount();
        System.exit(0);
    }

    public void getView() throws IOException {
        URL url = getClass().getClassLoader().getResource("set_master_mpp_view.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.setScene(new Scene(root, 552, 371));
        stage.show();
    }

    @Override
    public void getView(Stage stage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("../gui/set_master_pw_view.fxml"));
        Parent setMasterpasswordView = loader.load();
        //stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.setScene(new Scene(setMasterpasswordView, 552, 371));
        stage.show();
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    public void showPassword(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(setMPPViewPasswordField, setMPPViewTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }

    public void manageKeyInput(KeyEvent keyEvent) throws Exception {
        System.out.println("Key released");
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            try {
                saveMPPAction();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

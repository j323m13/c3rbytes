package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordGenerator;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;

import java.io.IOException;
import java.util.ArrayList;

//import static sample.ch.ffhs.c3rbytes.controller.loginViewMasterpassphraseController.startMainView;

public class setMasterPPViewController implements IController {
    public final String filename = ".c3r.c3r";

    @FXML javafx.scene.control.PasswordField setMPPViewPasswordField;
    @FXML javafx.scene.control.Button setMPPViewloginButton;

    public void saveMPPAction(ActionEvent actionEvent) throws Exception {
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
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            fileEncrypterDecrypter.encryptFile(passwordDecrypterPassword, filename, masterPassPhrase);
            DatabaseEntryDao newStartUp = new DatabaseEntryDao();
            newStartUp.setup();



        } catch(Exception e){
            // Reset Settings by deleting .c3r.c3r
            FileHandler fileHandler = new FileHandler();
            fileHandler.deleteFile(".c3r.c3r");
        }


        /*
        Stage stage = (Stage) setMPPViewloginButton.getScene().getWindow();
        stage.close();
        */

        //Main.entryView(stage);
        loginViewMasterpassphraseController lvmc = new loginViewMasterpassphraseController();
        lvmc.startMainView();
        abordMPPAction();
        //startMainView();
    }

    public void abordMPPAction() {
        System.out.println("System exit");
        System.exit(0);
    }

    public void getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/set_master_mpp_view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.setScene(new Scene(root, 552, 371));
        stage.show();
    }

    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return null;
    }
}

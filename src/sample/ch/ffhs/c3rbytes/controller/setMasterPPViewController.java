package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;
import sample.ch.ffhs.c3rbytes.crypto.PasswordGenerator;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;

import java.util.ArrayList;

//import static sample.ch.ffhs.c3rbytes.controller.loginViewMasterpassphraseController.startMainView;

public class setMasterPPViewController {
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
}

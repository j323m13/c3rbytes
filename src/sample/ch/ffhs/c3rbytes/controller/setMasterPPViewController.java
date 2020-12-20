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

/**
 * This class controls the setMasterPPView and interacts with crypto and model
 */

public class setMasterPPViewController implements IController {

    @FXML javafx.scene.control.PasswordField setMPPViewPasswordField;
    @FXML javafx.scene.control.TextField setMPPViewTextField;
    @FXML javafx.scene.control.Button setMPPViewloginButton;
    private boolean isHidingPassword = true;
    private final String filename = "c3r.c3r";
    FXMLLoader loader = null;
    DatabaseEntryDao newStartUp = new DatabaseEntryDao();

    /**
     * This method sets a new masterpassphrase to encrypt the c3r.c3r file with a new passphrase
     * @throws Exception if something went wrong
     */
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

        System.out.println(passwordDecrypterPassword);


        try {
            //loginViewMasterpassphraseController.passwordDecrypterPassword = masterPassPhrase;
            loginViewMasterpassphraseController.passwordDecrypterPassword = passwordDecrypterPassword;
            FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();
            fileEncrypterDecrypter.encryptFile(passwordDecrypterPassword, filename, masterPassPhrase);

            newStartUp.setup();



        } catch(Exception e){
            // Reset Settings by deleting c3r.c3r
            FileHandler fileHandler = new FileHandler();
            fileHandler.deleteFile(filename);
        }

        Stage stage = new Stage();
        mainViewController mainViewController = new mainViewController();
        mainViewController.getView(stage);
        stage.show();
        stage = (Stage) setMPPViewloginButton.getScene().getWindow();
        stage.close();

    }

    public void abordMPPAction() throws SQLException, InterruptedException {
        System.out.println("System exit");
        //delete db if the user abort during passphrase setup
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
        URL url = getClass().getClassLoader().getResource("set_master_mpp_view.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent setMasterpasswordView = loader.load();
        //stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(setMasterpasswordView, 552, 371));
        stage.show();
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    public void showPassword() {
        new PasswordRevealer().passwordReveal(setMPPViewPasswordField, setMPPViewTextField, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }

    public void manageKeyInput(KeyEvent keyEvent) {
        System.out.println("Key released");
        Node n = (Node)keyEvent.getSource();
        String id = n.getId();

        switch (id) {
            case "setMPPViewPasswordField":
                setMPPViewTextField.setText(setMPPViewPasswordField.getText());
                break;

            case "setMPPViewTextField":
                setMPPViewPasswordField.setText(setMPPViewTextField.getText());
                break;
        }

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

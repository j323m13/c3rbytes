package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
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
import sample.ch.ffhs.c3rbytes.connection.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.PasswordRevealer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * This class handles
 */

public class setMasterPWViewController implements IController {

    @FXML javafx.scene.control.PasswordField setMPViewPasswordField;
    @FXML javafx.scene.control.TextField setMPViewPasswordText;
    @FXML javafx.scene.control.Button setMPViewloginButton;
    @FXML javafx.scene.control.Label errorLabel;
    private boolean isHidingPassword = true;
    FXMLLoader loader = null;


    // password to encrypt DB
    public void saveMPAction() {
        errorLabel.setText("");
        String plainBootPassword = setMPViewPasswordField.getText();
        System.out.println(plainBootPassword);

        if (plainBootPassword.length() < 8) {
            errorLabel.setText("Enter at least 8 characters");
            setMPViewPasswordField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return;
        }

        // Hashing masterpassword
        StringHasher sha = new StringHasher();
        String HASHALGORITHM = "SHA3-512";
        String hashedBootPassword = sha.encryptSHA3(HASHALGORITHM, plainBootPassword);
        String hashedPasswordDB = sha.encryptSHA3(HASHALGORITHM,hashedBootPassword).substring(32,64);


        try {
            //TODO: Hier hakts irgendwo beim call von getView(Stage stage)
            setMasterPPViewController mppvc = new setMasterPPViewController();
            mppvc.getView();

            //String hashedPasswordDB = stringHasher.encryptSHA3(HASHALGORITHM,DBConnection.bootPassword);
            //DBConnection.bootPassword = hashedBootPassword;
            //DBConnection.passwordDB = hashedPasswordDB;
            DatabaseEntryDao login = new DatabaseEntryDao();
            login.setBootPasswordDAO(hashedBootPassword);
            login.setPasswordDBDAO(hashedPasswordDB);
            //decryptDB or createdB on first boot
            login.setupEncryption(hashedBootPassword);

            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/set_master_mpp_view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Welcome to C3rBytes");
            stage.setScene(new Scene(root, 552, 371));
            stage.show();

             */


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) setMPViewloginButton.getScene().getWindow();
        stage.close();
    }

    public void abordMPAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("set_master_pw_view.fxml");
        loader = new FXMLLoader(url);
        Parent setMasterpasswordView = loader.load();
        //stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(setMasterpasswordView, 552, 371));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }

    public void showPassword(ActionEvent actionEvent) {
        new PasswordRevealer().passwordReveal(setMPViewPasswordField, setMPViewPasswordText, isHidingPassword);
        isHidingPassword =! isHidingPassword;
    }


    public void manageKeyInput(KeyEvent keyEvent) throws Exception {
        System.out.println("Key released");

        Node n = (Node)keyEvent.getSource();
        String id = n.getId();

        switch (id) {
            case "setMPViewPasswordField":
                setMPViewPasswordText.setText(setMPViewPasswordField.getText());
                break;

            case "setMPViewPasswordText":
                setMPViewPasswordField.setText(setMPViewPasswordText.getText());
                break;
        }

        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            try {
                saveMPAction();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

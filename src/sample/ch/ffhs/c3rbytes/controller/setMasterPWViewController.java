package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;

import java.io.IOException;
import java.sql.SQLException;

public class setMasterPWViewController implements IController {

    @FXML javafx.scene.control.PasswordField setMPViewPasswordField;
    @FXML javafx.scene.control.Button setMPViewloginButton;

    // password to encrypt DB
    public void saveMPAction(ActionEvent actionEvent) {
        String plainDBpassword = setMPViewPasswordField.getText();
        System.out.println(plainDBpassword);

        // Hashing masterpassword
        StringHasher sha = new StringHasher();
        String HASHALGORITHM = "SHA3-512";
        String hashedDBPassword = sha.encryptSHA3(HASHALGORITHM, plainDBpassword);
        //DBConnection.bootPassword = hashedDBPassword;

        try {

            setMasterPPViewController mppvc = new setMasterPPViewController();
            mppvc.getView();

            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/set_master_mpp_view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Welcome to C3rBytes");
            stage.setScene(new Scene(root, 552, 371));
            stage.show();

             */

        }catch (IOException e) {
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
        Parent setMasterpasswordView = FXMLLoader.load(getClass().getResource("../gui/set_master_pw_view.fxml"));
        //stage stage = new Stage();
        stage.setTitle("Welcome to C3rBytes");
        stage.setScene(new Scene(setMasterpasswordView, 552, 371));
    }

    @Override
    public Object getController() throws IOException {
        return null;
    }
}

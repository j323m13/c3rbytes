package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class loginViewMasterpassphraseController {
    @FXML
    private javafx.scene.control.TextField masterPassPhraseField;
    @FXML private javafx.scene.control.Button loginButtonMPP;
    @FXML private javafx.scene.control.Button logoutButtonMPP;

    public void loginActionMPP(){

        // check pw in c3r.c3r file


        // Assume PW is correct
        boolean filePwcorrect = true;

        if (filePwcorrect){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/main_view_2.fxml"));
                Parent root = loader.load();
                //passwordGeneratorController pwGenCon = loader.getController();
                //pwGenCon.getpwdOutputTextField(passwordField);
                Stage stage = new Stage();
                stage.setTitle("C3rBytes Main");
                stage.setScene(new Scene(root, 1200, 600));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage =  (Stage) loginButtonMPP.getScene().getWindow();
            stage.close();


        }
        else{
            System.out.println("Login failed");
        }


    }

    public void logoutActionMPP(){
        System.out.println("System exited");
        System.exit(0);
    }

}

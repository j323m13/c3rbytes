package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class changePassphraseController {

    @FXML javafx.scene.control.Button savePassPhraseButton;
    public void changePassphraseAction(ActionEvent actionEvent) {

    }

    @FXML javafx.scene.control.Button discardPassphraseButton;
    public void discardPassphraseAction(ActionEvent actionEvent) {
        Stage stage = (Stage) discardPassphraseButton.getScene().getWindow();
        stage.close();
    }
}

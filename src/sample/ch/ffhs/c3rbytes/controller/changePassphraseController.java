package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class changePassphraseController implements IController{

    @FXML javafx.scene.control.Button savePassPhraseButton;
    public void changePassphraseAction(ActionEvent actionEvent) {

    }

    @FXML javafx.scene.control.Button discardPassphraseButton;
    public void discardPassphraseAction(ActionEvent actionEvent) {
        Stage stage = (Stage) discardPassphraseButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return null;
    }
}

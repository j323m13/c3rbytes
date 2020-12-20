package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * This class produces alerts to be displayed to the user.
 * @author Jérémie Equey
 */
public class alertViewController implements IController {
    @FXML
    private TilePane tilePaneAlert;

    /**
     * start an alert
     * @param alertText the text of the alert
     * @param TYPE of the alert
     * @param confirmation the button to confirm or discard the action
     * @return option: the chosen option to operate an action on come back.
     */
    public Optional<ButtonType> startAlertWindows(String alertText, Alert.AlertType TYPE, String confirmation){
        Alert alert = new Alert(TYPE);
        alert.setWidth(300);
        alert.setHeight(250);
        alert.setHeaderText(confirmation);
        System.out.println(alertText);
        Text textAlert = new Text();
        textAlert.setFill(Color.RED);
        textAlert.setFont(Font.font("Arial", FontPosture.REGULAR, 15));
        textAlert.setText(alertText);
        ScrollPane scroll = new ScrollPane();
        tilePaneAlert.getChildren().add(textAlert);
        scroll.setContent(tilePaneAlert);
        alert.getDialogPane().setContent(tilePaneAlert);

        Optional<ButtonType> option = alert.showAndWait();
        return option;
    }


    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return null;
    }


}

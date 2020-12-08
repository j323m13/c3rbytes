package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;

import java.io.IOException;
import java.util.Optional;

public class alertViewController implements IController {
    @FXML
    public Button discardButton;
    @FXML
    public Button confirmButton;
    @FXML
    private TilePane tilePaneAlert;
    @FXML public Text textAlert;

    javafx.scene.control.Button viewDiscardButton;
    DatabaseEntry databaseEntryToDelete;
    public static boolean confirmation = false;

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
        String alertMessage = alertText;
        System.out.println(alertMessage);
        Text textAlert = new Text();
        textAlert.setFill(Color.RED);
        textAlert.setFont(Font.font("Arial", FontPosture.REGULAR, 15));
        textAlert.setText(alertMessage);
        ScrollPane scroll = new ScrollPane();
        tilePaneAlert.getChildren().add(textAlert);
        scroll.setContent(tilePaneAlert);
        alert.getDialogPane().setContent(tilePaneAlert);

        Optional<ButtonType> option = alert.showAndWait();
        return option;
    }


   /*
    public void onConfirm(javafx.event.ActionEvent actionEvent) {
        System.out.print("delete that shit.");
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();


    }

    public void onDiscard(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage)discardButton.getScene().getWindow();
        stage.close();
    }
    */

    @Override
    public void getView(Stage stage) throws IOException {

    }

    @Override
    public Object getController() throws IOException {
        return null;
    }


}

package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import jdk.swing.interop.LightweightFrameWrapper;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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


    public void startAlertWindows(String alertText, Alert.AlertType TYPE, String confirmation){
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
        alert.showAndWait();

        //alertField.setText("be careful");
        //Text textAlert = new Text();
        /*
        textAlert.setText(a);
        textAlert.setFont(Font.font ("Verdana", 20));
        textAlert.setFill(Color.RED);
        textAlert.setTextAlignment(TextAlignment.CENTER);
         */


    }

    public void onConfirm(javafx.event.ActionEvent actionEvent) {
        System.out.print("delete that shit.");
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();


    }

    public void onDiscard(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage)discardButton.getScene().getWindow();
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

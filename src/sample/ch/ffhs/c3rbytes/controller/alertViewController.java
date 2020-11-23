package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class alertViewController {
    @FXML
    javafx.scene.control.Button viewDiscardButton;
    DatabaseEntry databaseEntryToDelete;



    public void passTheData(DatabaseEntry entry) {
        databaseEntryToDelete = entry;
    }

    public void onConfirmDelete(javafx.event.ActionEvent actionEvent) {
        System.out.print("delete that shit.");

    }

    public void onDiscardDelete(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage)viewDiscardButton.getScene().getWindow();
        stage.close();
    }
}

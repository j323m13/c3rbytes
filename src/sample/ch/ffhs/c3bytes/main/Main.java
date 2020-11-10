package sample.ch.ffhs.c3bytes.main;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        primaryStage.setTitle("C3rBytes");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

//        public void addNewItem(){
//        Parent root;
//        try {
//            root = FXMLLoader.load(getClass().getClassLoader().getResource("./gui/add_new_item_view.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("ABCD");
//            stage.setScene(new Scene(root, 640, 341));
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}

package sample.ch.ffhs.c3bytes.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    /*public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        primaryStage.setTitle("C3rBytes");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }*/

    public void start(Stage secondaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/password_generator_view.fxml"));
        secondaryStage.setTitle("C3rBytes");
        secondaryStage.setScene(new Scene(root, 900, 600));
        secondaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

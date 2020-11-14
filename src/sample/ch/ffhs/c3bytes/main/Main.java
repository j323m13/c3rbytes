package sample.ch.ffhs.c3bytes.main;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3bytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3bytes.dao.connectionFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {


    /*
    @override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        primaryStage.setTitle("C3rBytes");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }*/


    public void start(Stage secondaryStage) throws Exception {

        /*
        Connection connection = connectionFactory.getConnection();
        DatabaseEntryDao newDao = new DatabaseEntryDao();
        DatabaseEntryDao.getAll();
        connection.close();
         */

        /*
        Parent root = FXMLLoader.load(getClass().getResource("../gui/password_generator_view.fxml"));
        secondaryStage.setTitle("C3rBytes");
        secondaryStage.setScene(new Scene(root, 900, 600));
        secondaryStage.show();
         */
        //start mainView2
        Parent mainView = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        //secondaryStage stage = new Stage();
        secondaryStage.setTitle("C3rBytes");
        secondaryStage.setScene(new Scene(mainView, 1200, 600));
        secondaryStage.show();




    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {





        /*
        Connection connection = connectionFactory.getConnection();
        DatabaseEntryDao newDao = new DatabaseEntryDao();
        DatabaseEntry entry1 = new DatabaseEntry(
                "Jérémie",
                "Facebook.com",
                "https://facebook.com",
                "122499oiukjdfk"
        );
        newDao.insertDatabaseEntry();

        DatabaseEntry entry2 = new DatabaseEntry(
                "Olaf",
                "Instagram.com",
                "https://instagram.com",
                "!!!!!!!ukjdfk"

        );
        newDao.insertDatabaseEntry();

        DatabaseEntry entry3 = new DatabaseEntry(
                "Mersid",
                "Twitter.com",
                "https://Twitter.com",
                "@@@@@oiukjdfk"

        );
        newDao.insertDatabaseEntry();
        newDao.getAll();
        connection.close();
         */

        launch(args);
    }


}

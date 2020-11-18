package sample.ch.ffhs.c3rbytes.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.dao.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

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
        Parent addItem = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../gui/add_new_item_view.fxml")));
        secondaryStage.setTitle("Add new Item");
        secondaryStage.setScene(new Scene(addItem, 400, 400));
        secondaryStage.show();



        Parent root = FXMLLoader.load(getClass().getResource("../gui/password_generator_view.fxml"));
        secondaryStage.setTitle("C3rBytes");
        secondaryStage.setScene(new Scene(root, 900, 600));
        secondaryStage.show();
        */

        /*
        //start mainView2
        Parent mainView = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        //secondaryStage stage = new Stage();
        secondaryStage.setTitle("C3rBytes");
        secondaryStage.setScene(new Scene(mainView, 1200, 600));
        secondaryStage.show();
        */

        //start login_view
        Parent mainView = FXMLLoader.load(getClass().getResource("../gui/login_view.fxml"));
        //secondaryStage stage = new Stage();
        secondaryStage.setTitle("C3rBytes Login Masterpassword");
        secondaryStage.setScene(new Scene(mainView, 552, 371));
        secondaryStage.show();



    }

    /*
    private static void getView(Stage stage ,String resource, String title, int width, int height) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(resource));
        //secondaryStage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent, width, height));
        stage.show();


    }
*/

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        FileHandler fileHandler = new FileHandler();
        if (fileHandler.readFromFile("c3r.c3r").equals("File does not exist")){
            System.out.println("Enter Masterpassword Dialog");

        }
        else{
            launch(args);
        }


        Connection connection = DBConnection.getConnection();
        DatabaseEntryDao newDao = new DatabaseEntryDao();
        DatabaseEntry entry1 = new DatabaseEntry(
                "Jérémie",
                "Facebook.com",
                "https://facebook.com",
                "122499oiukjdfk"
        );
        newDao.insertDatabaseEntry(entry1);

        DatabaseEntry entry2 = new DatabaseEntry(
                "Olaf",
                "Instagram.com",
                "https://instagram.com",
                "!!!!!!!ukjdfk"

        );
        newDao.insertDatabaseEntry(entry2);

        DatabaseEntry entry3 = new DatabaseEntry(
                "Jonas",
                "Twitter.com",
                "https://Twitter.com",
                "@@@@@oiukjdfk"

        );
        newDao.insertDatabaseEntry(entry3);
        connection.close();

        //launch(args);
    }


}

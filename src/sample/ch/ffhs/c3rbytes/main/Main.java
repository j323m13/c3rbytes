package sample.ch.ffhs.c3rbytes.main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.controller.loginViewController;
import sample.ch.ffhs.c3rbytes.controller.setMasterPWViewController;
import sample.ch.ffhs.c3rbytes.connection.DBConnection;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class Main extends Application {
    private final String file = "c3r.c3r";




    /*
    @override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        primaryStage.setTitle("C3rBytes");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }*/


    public void start(Stage stage) throws Exception {
        /*
        Parent addItem = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../gui/add_new_item_view.fxml")));
        stage.setTitle("Add new Item");
        stage.setScene(new Scene(addItem, 400, 400));
        stage.show();



        Parent root = FXMLLoader.load(getClass().getResource("../gui/password_generator_view.fxml"));
        stage.setTitle("C3rBytes");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
        */

        /*
        //start mainView2
        Parent mainView = FXMLLoader.load(getClass().getResource("../gui/main_view_2.fxml"));
        //stage stage = new Stage();
        stage.setTitle("C3rBytes");
        stage.setScene(new Scene(mainView, 1200, 600));
        stage.show();
        */

        // First Init
        FileHandler fileHandler = new FileHandler();

        //check user lang and country variable. pass the result to DBConnenction
        OSBasedAction helper = new OSBasedAction();
        helper.setLocalValue();


        //fileHandler.setReadWriteAttributes(file,"allow");

        if (fileHandler.readFromFile(file).equals("File does not exist")){
            System.out.println("Enter Masterpassword Dialog");




            setMasterPWViewController mpwvc = new setMasterPWViewController();
            mpwvc.getView(stage);
            /*

            Parent setMasterpasswordView = FXMLLoader.load(getClass().getResource("../gui/set_master_pw_view.fxml"));
            //stage stage = new Stage();
            stage.setTitle("Welcome to C3rBytes");
            stage.setScene(new Scene(setMasterpasswordView, 552, 371));

             */

        }/*
        //TODO to implement correctly. not working.
        else if(fileHandler.readFromFile(dbFile).equals("file does not exist")){
            DBConnection.onNewStartup = true;

        }
        */
        else{ // or just logging in
            //start login_view

            loginViewController lvc = new loginViewController();
            lvc.getView(stage);



            /*
            Parent loginViewMP = FXMLLoader.load(getClass().getResource("../gui/login_view.fxml"));
            //stage stage = new Stage();
            stage.setTitle("C3rBytes Login Masterpassword");
            stage.setScene(new Scene(loginViewMP, 552, 371));
e
             */
        }
        //fileHandler.setReadWriteAttributes(file,"deny");
        stage.show();
    }



    /*
    private static void getView(Stage stage ,String resource, String title, int width, int height) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(resource));
        //secondaryStage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent, width, height));
        stage.show();


    }


    public static void entryView(Stage stage) throws IOException {
        //start login_view
        Parent loginViewMP = FXMLLoader.load(Main.class.getResource("../gui/login_view.fxml"));
        //secondaryStage stage = new Stage();
        stage.setTitle("C3rBytes Login Masterpassword");
        stage.setScene(new Scene(loginViewMP, 552, 371));
        stage.show();
    }*/

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        launch(args);

/*
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
     */
    }




}

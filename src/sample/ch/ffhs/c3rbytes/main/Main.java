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

    public void start(Stage stage) throws Exception {

        // First Init
        FileHandler fileHandler = new FileHandler();

        //check user lang and country variable. pass the result to DBConnenction
        OSBasedAction helper = new OSBasedAction();
        helper.setLocalValue();

        if (fileHandler.readFromFile(file).equals("File does not exist")){
            System.out.println("Enter Masterpassword Dialog");
            setMasterPWViewController mpwvc = new setMasterPWViewController();
            mpwvc.getView(stage);
        }
        else{ // or just logging in
            //start login_view
            loginViewController lvc = new loginViewController();
            lvc.getView(stage);
        }
        stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        launch(args);
    }

}

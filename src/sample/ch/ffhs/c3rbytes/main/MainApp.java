package sample.ch.ffhs.c3rbytes.main;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This class calls the class Main.class. We use it to create the jar-file.
 */
public class MainApp {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        sample.ch.ffhs.c3rbytes.main.Main.main(args);
    }
}

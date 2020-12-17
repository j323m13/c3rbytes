package sample.ch.ffhs.c3rbytes.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

/**
 * Test the DBConnection class
 */

class DBConnectionTest {

    String newBootPassword = null;
    String passworDB = "123456789";
    String dbName = "testDB2/testDB";
    String localValue = "fr_CH";


    @BeforeAll
    public static void setup() throws SQLException {
        //we prepare shema and table for the tests.
        String slqCreateSchemaTest = "CREATE SCHEMA \"CERBYTES\"";
        String sqlCreateTableTest = "CREATE TABLE \"CERBYTES\".\"database_entries\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(500) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(500) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(50) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(50) DEFAULT NULL,\n" +
                "                       \"note\" CLOB(2K) DEFAULT NULL)";
        try {
            dbExecuteUpdate(slqCreateSchemaTest,createURLSimple());
            dbExecuteUpdate(sqlCreateTableTest,createURLSimple());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void setEncryptionTest() throws SQLException {
        setDatabaseName(dbName+"01");
        setPasswordDB(passworDB);
        setBootPassword("lkdafjddfklsjdkllkdsafjkllkéadfjslkadsjflkadsfj");
        setLocalValues(localValue);
        //when creating a db with a bootpassword, it is encrypted (when shutdown).
        dbConnect(createURL());

        try {
            shutdownDB();
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + getDatabaseName() + " has shutdown successfully.");
                assertEquals(e.getMessage(),"08006");
            }
        }

        //Start db wihtout bootPassword
        try {
            setBootPassword(null);
            dbConnect(createURL());
        } catch (SQLException e) {
            assertEquals("Startup failed. An encrypted database cannot be accessed without the correct boot password.  ",e.getNextException().getMessage());
        }

        setBootPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }


    @Test
    void changeBootPasswordTest() throws IllegalArgumentException,  SQLException {
        setDatabaseName(dbName+"06");
        newBootPassword = "987654321987654321";
        //create db for testing
        dbConnect(createURLSimple());

        //shutdown db we just have created.
        try {
            shutdownDB();
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + getDatabaseName() + " has shutdown successfully.");
            }

        }try {
            System.out.println("sleeping 1");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Applying new boot password.");
            System.out.println("changing bootPassword");
            dbConnect(createURL() + ";newBootPassword=" + newBootPassword);
            System.out.println("URL with new bootPassword "+createURL() + ";newBootPassword=" + newBootPassword);
            setBootPassword(newBootPassword);
            //bootPassword = newBootPassword;
            System.out.println("success.");
            //here we can see that the boot password ist not the same.
            System.out.println(getBootPassword());
            System.out.println("new URL "+createURL());

        } catch (SQLException error) {
            System.out.println("failed. -> " + error);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        //check if the new bootPassword is not equals to the old bootPassword.
        //if this test passes this means that the bootPassword on the first place was properly set.
        //then per analogy this test checks if 1) the bootPassword has been set and 2) if the new bootPassword has been set (replaces the old bootPassword).
        assertEquals("987654321987654321",getBootPassword());
        setBootPassword(newBootPassword);
    }

    @Test
    void setupUserDBWithPasswordConnectionTest () throws IllegalArgumentException, SQLException {
        setPasswordDB("xxxxxxxxxxx");
        setDatabaseName(dbName+"07");
        dbConnect(createURLSimple());
        //setPasswordDB("123456789");
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        dbConnect(createURLSimple());
        setupUserDBWithPasswordConnection(createURLSimple(), "123456789", setupPasswordString);
        assertEquals("123456789",getPasswordDB());
    }

    @Test
    void resetUserPwdTest () throws IllegalArgumentException, SQLException {
        setDatabaseName(dbName+"07");
        setPasswordDB("123456789");
        dbConnect(createURLSimple());
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(setupPasswordString, "3874436sjsjsjzszhshs");
        assertEquals("3874436sjsjsjzszhshs", getPasswordDB());
    }


    @Test
    void dbConnectTest() throws SQLException {
        setDatabaseName(dbName+"03");
        dbConnect(createURLSimple());
        assertFalse(getConnection().isClosed());
    }

    @Test
    void dbDisconnectTest() throws SQLException {
        //create a database and connect to it and try to close the connection.
        dbConnectTest();
        dbDisconnect();
        assertTrue(getConnection().isClosed());
    }

    @Test
    void dbExecuteUpdateTest() throws IllegalArgumentException, SQLException {
        setPasswordDB("123456789");
        setBootPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        setLocalValues(localValue);
        setPasswordDB(passworDB);
        //setup the environment for the test.
        setDatabaseName(dbName+"14");
        dbConnect(createURL());
        setup();
        DatabaseEntry entry = new DatabaseEntry();
        entry.setId("1");
        entry.setUsername("Jérome");
        entry.setDescription("Social");
        entry.setUrl("https://facebook.com");
        entry.setPassword("123456789");
        entry.setCreationDate("2020-01-12");
        entry.setLastUpdate("2020-05-12");
        entry.setNote("this a test note");

        String insertTest = "INSERT INTO \"CERBYTES\".\"database_entries\"\n" +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"date_update\", \"note\" )\n" +
                "VALUES('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+"','"+entry.getCreationDate()+"', '"+entry.getLastUpdate()+"','"+entry.getNote()+"')";
        //we save the object in the db
        //if a exception is raised. the test fails.
        dbExecuteUpdate(insertTest,createURLSimple());
    }

    @Test
    void dbExecuteQueryTest() throws IllegalArgumentException, SQLException {
        setDatabaseName(dbName+"04");
        setPasswordDB(passworDB);
        setLocalValues(localValue);
        dbConnect(createURLSimple());

        DatabaseEntry entry = new DatabaseEntry();
                entry.setUsername("Jérome");
                entry.setDescription("Social");
                entry.setUrl("https://facebook.com");
                entry.setPassword("123456789");
                entry.setCreationDate("2020-01-12");
                entry.setLastUpdate("2020-05-12");
                entry.setNote("this a test note");

        String queryTest = "SELECT * FROM \"CERBYTES\".\"database_entries\"";

        ObservableList<DatabaseEntry> databaseEntriesTest = FXCollections.observableArrayList();
        ObservableList<DatabaseEntry> databaseEntriesTestResults = FXCollections.observableArrayList();
        databaseEntriesTest.add(entry);
        System.out.println(databaseEntriesTest.get(0));
        assertNotNull(databaseEntriesTestResults);
    }


    @Test
    void shutdownDBTest() throws SQLException {
        setDatabaseName(dbName+"05");
        setPasswordDB(passworDB);
        setLocalValues(localValue);
        //start db
        dbConnect(createURLSimple());
        try{
            shutdownDB();
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
            assertEquals(e.getSQLState(), "08006");
        }
    }





    @Test
    void deleteTest() throws IllegalArgumentException, SQLException {
        setDatabaseName(dbName + "14");
        setPasswordDB(passworDB);
        setLocalValues(localValue);
        dbConnect(createURLSimple());
        setup();
        String deleteStmtTest = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        setup();
        //if a exception is raised. the test fails.
        dbExecuteUpdate(deleteStmtTest, createURLSimple());

    }


    @AfterAll
    static void clean() throws IllegalArgumentException, IOException {
        OSBasedAction deleter = new OSBasedAction();
        Path dbFile = Paths.get("testDB2");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleter.deleteDatabaseFolder(db);
    }

}
package sample.ch.ffhs.c3rbytes.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

class DBConnectionDAOTest {
    DatabaseEntryDao helperTest = new DatabaseEntryDao();
    String passworDB = "123456789";
    String dbName = "testDB/testDB";




    @BeforeAll
    public static void setup(){
        setPasswordDB("123456789");
        setBootPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        setDatabaseName("testDB/testDB");
        setLocalValues("fr_CH");
    }

    @Test
    void setEncryptionTest() throws SQLException {
        helperTest.setDatabaseNameDAO(dbName+"01");
        helperTest.connect();

        try {
            //shutdown db
            helperTest.shutdown();
            //DriverManager.getConnection(createURL() + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + helperTest.getDatabaseNameDAO() + " has shutdown successfully.");
            }
        }

        //Start db wihtout bootPassword
        try {
            helperTest.setBootPasswordDAO(null);
            helperTest.connect();
        } catch (SQLException e) {
            assertEquals("Startup failed. An encrypted database cannot be accessed without the correct boot password.  ",e.getNextException().getMessage());
        }

        helperTest.setBootPasswordDAO("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


        }


    @Test
    void changeBootPasswordTest() throws  SQLException {
        helperTest.setDatabaseNameDAO(dbName+"06");
        String newBootPassword = "987654321987654321";
        String newPasswordDB = "zzzzzzzzz";
        //create db for testing
        helperTest.connect();

        //shutdown db we just have created.
        try {
            helperTest.shutdown();
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + helperTest.getDatabaseNameDAO() + " has shutdown successfully.");
            }

        }try {
            System.out.println("sleeping 1");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Applying new boot password.");
            System.out.println("changing bootPassword");
            helperTest.changeBootPassword(newBootPassword, newPasswordDB );
            System.out.println("URL with new bootPassword "+createURL() + ";newBootPassword=" + newBootPassword);
            helperTest.setBootPasswordDAO(newBootPassword);
            //bootPassword = newBootPassword;
            System.out.println("success.");
            //here we can see that the boot password ist not the same.
            System.out.println(helperTest.getBootPasswordDAO());
            System.out.println("new URL "+createURL());

        } catch (SQLException error) {
            System.out.println("failed. -> " + error);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        //check if the new bootPassword is not equals to the old bootPassword.
        //if this test passes this means that the bootPassword on the first place was properly set.
        //then per analogy this test checks if 1) the bootPassword has been set and 2) if the new bootPassword has been set (replaces the old bootPassword).
        assertEquals("987654321987654321",helperTest.getBootPasswordDAO());
        helperTest.setBootPasswordDAO(newBootPassword);
    }

    @Test
    void setupUserDBWithPasswordTest () throws SQLException {
        helperTest.setDatabaseNameDAO(dbName+"07");
        helperTest.connect();
        helperTest.setupUserDBWithPassword("123456789");
        assertEquals("123456789",helperTest.getPasswordDBDAO());
    }

    @Test
    void resetUserDBWithPasswordTest () throws SQLException, ClassNotFoundException, InterruptedException {
        helperTest.setDatabaseNameDAO(dbName+"02");
        helperTest.setPasswordDBDAO("123456789");
        helperTest.connect();
        setupUserDBWithPasswordTest();
        helperTest.resetUserDBWithPassword("3874436sjsjsjzszhshs");
        helperTest.setPasswordDBDAO("3874436sjsjsjzszhshs");
        assertEquals("3874436sjsjsjzszhshs", helperTest.getPasswordDBDAO());
    }


    @Test
    void connectTest() throws SQLException {
        helperTest.setDatabaseNameDAO(dbName+"03");
        helperTest.connect();
        assertFalse(connection.isClosed());
    }

    @Test
    void disconnectTest() throws SQLException {
        helperTest.setDatabaseNameDAO(dbName+"03");
        helperTest.connect();
        helperTest.disconnect();
        assertTrue(connection.isClosed());
        }

    @Test
    void saveTest() throws InterruptedException, SQLException, ClassNotFoundException {
        helperTest.setDatabaseNameDAO(dbName+"04");
        helperTest.setPasswordDBDAO(passworDB);
        helperTest.connect();
        DatabaseEntry entry = new DatabaseEntry();
        entry.setId("1");
        entry.setUsername("Jérome");
        entry.setDescription("Social");
        entry.setUrl("https://facebook.com");
        entry.setPassword("123456789");
        entry.setCreationDate("2020-01-12");
        entry.setLastUpdate("2020-05-12");
        entry.setNote("this a test note");
        assertTrue(helperTest.save(entry));
    }

    @Test
    void updateTest() throws InterruptedException, SQLException, ClassNotFoundException {
        saveTest();

        DatabaseEntry entry = new DatabaseEntry();
                entry.setId("1");
                entry.setUsername("Jérome");
                entry.setDescription("Social");
                entry.setUrl("https://facebook.com");
                entry.setPassword("123456789");
                entry.setCreationDate("2020-01-12");
                entry.setLastUpdate("2020-05-12");
                entry.setNote("this a test note");


        ObservableList<DatabaseEntry> databaseEntriesTest = FXCollections.observableArrayList();
        ObservableList<DatabaseEntry> databaseEntriesTestResults;
        databaseEntriesTest.add(entry);
        helperTest.update(entry);
        databaseEntriesTestResults = helperTest.getAll();
        System.out.println(databaseEntriesTest.get(0));
        assertNotNull(databaseEntriesTestResults);
        //assertEquals();


    }


    @Test
    void shutdownDBTest() throws SQLException {
        helperTest.setDatabaseNameDAO(dbName+"05");
        helperTest.setPasswordDBDAO(passworDB);
        //start db
        helperTest.connect();
        try{
            helperTest.shutdown();
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
            assertEquals(e.getSQLState(), "08006");
        }
    }





    @Test
    void removeTestsEffects() throws SQLException, InterruptedException, ClassNotFoundException {
        OSBasedAction deleter = new OSBasedAction();
        helperTest.setDatabaseNameDAO(dbName+"04");
        helperTest.setPasswordDBDAO(passworDB);
        helperTest.connect();
        String deleteStmtTest = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try{
            assertFalse(dbExecuteUpdate(deleteStmtTest,createURLSimple()));
        }catch (SQLException exception){
            assertNotEquals(exception.getSQLState(),"Username not found in SYS.SYSUSERS");
        }


    }


    @AfterAll
    static void clean(){
        OSBasedAction deleter = new OSBasedAction();
        Path dbFile = Paths.get("testDB/");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleter.deleteDatabaseFolder(db);
    }

}
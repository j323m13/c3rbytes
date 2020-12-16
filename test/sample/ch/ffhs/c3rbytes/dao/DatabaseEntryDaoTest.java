package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;


/**
 * Test de methodes of the DatabaseEntryDao Class.
 * for each test a new db is created and the task performed.
 * the last methode, clean the db (delete them).
 */
class DatabaseEntryDaoTest {
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

    public DatabaseEntry createAnEntry(){
        DatabaseEntry entry = new DatabaseEntry();
        entry.setId("1");
        entry.setUsername("Jérome");
        entry.setDescription("Social");
        entry.setUrl("https://facebook.com");
        entry.setPassword("123456789");
        entry.setCreationDate("2020-01-12");
        entry.setLastUpdate("2020-05-18");
        entry.setNote("this a test note");
        return entry;
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
        helperTest.setDatabaseNameDAO(dbName+"100");
        helperTest.setPasswordDBDAO("");
        helperTest.connect();
        helperTest.setupUserDBWithPassword("123456789");
        assertEquals("123456789",helperTest.getPasswordDBDAO());
    }

    @Test
    void resetUserDBWithPasswordTest () throws SQLException, ClassNotFoundException, InterruptedException {
        helperTest.setDatabaseNameDAO(dbName+"02");
        helperTest.setPasswordDBDAO("123456789");
        helperTest.connect();
        helperTest.setupUserDBWithPassword("123456789");
        helperTest.resetUserDBWithPassword("3874436sjsjsjzszhshs");
        //helperTest.setPasswordDBDAO("3874436sjsjsjzszhshs");
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
    void saveTest() throws InterruptedException, SQLException, ClassNotFoundException, IOException {
        helperTest.setDatabaseNameDAO(dbName+"04");
        helperTest.setPasswordDBDAO(passworDB);
        helperTest.connect();
        helperTest.setup();
        helperTest.save(createAnEntry());
        ObservableList<DatabaseEntry> databaseEntriesTestResults = null;
        assertNotNull(databaseEntriesTestResults=helperTest.getAll());
    }

    @Test
    void updateTest() throws InterruptedException, SQLException, ClassNotFoundException, IOException {
        DatabaseEntry entry = createAnEntry();
        ObservableList<DatabaseEntry> databaseEntriesTest = null;
        helperTest.setup();
        helperTest.save(entry);
        entry.setUrl("www.instagramm");
        helperTest.update(entry);
        ObservableList<DatabaseEntry> databaseEntriesTestResults = null;
        databaseEntriesTestResults = helperTest.getAll();
        assertEquals(entry.getUrl(),databaseEntriesTestResults.get(0).getUrl());
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
    void removeTestsEffects() throws SQLException, InterruptedException, ClassNotFoundException, IOException {
        OSBasedAction deleter = new OSBasedAction();
        helperTest.setDatabaseNameDAO(dbName+"04");
        helperTest.setPasswordDBDAO(passworDB);
        helperTest.connect();
        String deleteStmtTest = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        helperTest.setup();
        helperTest.save(createAnEntry());
        dbExecuteUpdate(deleteStmtTest,createURLSimple());
        ObservableList<DatabaseEntry> resultDelete = null;
        resultDelete = helperTest.getAll();
        assertTrue(resultDelete.size()==0);


    }


    @AfterAll
    static void clean() throws IOException {
        OSBasedAction deleter = new OSBasedAction();
        Path dbFile = Paths.get("testDB/");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleter.deleteDatabaseFolder(db);
    }

}
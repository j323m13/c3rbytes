package sample.ch.ffhs.c3rbytes.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

class DBConnectionTest {
    String newBootPassword = null;
    String passworDB = "123456789";
    String dbName = "testDB2/testDB";
    Connection connection = null;




    @BeforeAll
    public static void setup(){
        setPasswordDB("123456789");
        setBootPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //setDatabaseName("dbName");
        setLocalValues("fr_CH");
    }

    @Test
    void setEncryptionTest() throws SQLException {
        setDatabaseName(dbName+"01");
        dbConnect(createURL());

        try {
            //shutdown db
            shutdownDB();
            //DriverManager.getConnection(createURL() + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + getDatabaseName() + " has shutdown successfully.");
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
    void changeBootPasswordTest() throws  SQLException {
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
    void setupUserDBWithPasswordConnectionTest () throws SQLException {
        setPasswordDB("xxxxxxxxxxx");
        setDatabaseName(dbName+"07");
        dbConnect(createURL());
        setPasswordDB("123456789");
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        dbConnect(createURL());
        setupUserDBWithPasswordConnection(createURLSimple(), getPasswordDB(), setupPasswordString);
        assertEquals("123456789",getPasswordDB());
    }

    @Test
    void resetUserPwdTest () throws SQLException {
        setDatabaseName(dbName+"02");
        dbConnect(createURL());
        setupUserDBWithPasswordConnectionTest();
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(setupPasswordString, "3874436sjsjsjzszhshs");
        setPasswordDB("3874436sjsjsjzszhshs");
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
        setDatabaseName(dbName+"03");
        dbDisconnect();
        assertTrue(getConnection().isClosed());
    }

    @Test
    void dbExecuteUpdateTest() throws InterruptedException, SQLException, ClassNotFoundException {
        setDatabaseName(dbName+"04");
        setPasswordDB(passworDB);
        dbConnect(createURL());
        DatabaseEntry entry = new DatabaseEntry();
        entry.setId("1");
        entry.setUsername("Jérome");
        entry.setDescription("Social");
        entry.setUrl("https://facebook.com");
        entry.setPassword("123456789");
        entry.setCreationDate("2020-01-12");
        entry.setLastUpdate("2020-05-12");
        entry.setNote("this a test note");


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

        String insertTest = "INSERT INTO \"CERBYTES\".\"database_entries\"\n" +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"date_update\", \"note\" )\n" +
                "VALUES('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+"','"+entry.getCreationDate()+"', '"+entry.getLastUpdate()+"','"+entry.getNote()+"')";


        assertTrue(dbExecuteUpdate(slqCreateSchemaTest,createURL()));
        //assertFalse(DBConnection.dbExecuteUpdate(slqCreateSchema,createURL()));
        assertTrue(dbExecuteUpdate(sqlCreateTableTest,createURL()));
        //assertFalse(DBConnection.dbExecuteUpdate(sqlCreateTable,createURL()));
        assertTrue(dbExecuteUpdate(insertTest,createURL()));
    }

    @Test
    void dbExecuteQueryTest() throws InterruptedException, SQLException, ClassNotFoundException {
        setDatabaseName(dbName+"04");
        setPasswordDB(passworDB);
        dbConnect(createURL());

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
        databaseEntriesTestResults = dbExecuteQuery(queryTest,databaseEntriesTest,createURL());
        System.out.println(databaseEntriesTest.get(0));
        assertNotNull(databaseEntriesTestResults);
        //assertEquals();


    }


    @Test
    void shutdownDBTest() throws SQLException {
        setDatabaseName(dbName+"05");
        setPasswordDB(passworDB);
        //start db
        dbConnect(createURL());
        try{
            shutdownDB();
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
            assertEquals(e.getSQLState(), "08006");
        }
    }





    @Test
    void removeTestsEffects() throws SQLException, InterruptedException, ClassNotFoundException {
        OSBasedAction deleter = new OSBasedAction();
        setDatabaseName(dbName+"04");
        setPasswordDB(passworDB);
        dbConnect(createURL());
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
        Path dbFile = Paths.get("testDB2");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleter.deleteDatabaseFolder(db);
    }

}
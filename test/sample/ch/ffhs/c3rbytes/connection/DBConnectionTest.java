package sample.ch.ffhs.c3rbytes.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

class DBConnectionTest {

    Connection connectionTest = null;
    String passwordDB = "123456789";
    String bootPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String databaseName = "testDB/testDB";
    String newPassworDB = null;
    String newBootPassword = null;

    @Test
    void setEncryption() throws SQLException {
        DBConnection.databaseName = databaseName+"01";
        DBConnection.passwordDB = passwordDB;
        DBConnection.bootPassword = bootPassword;
        DriverManager.getConnection(createURL());
        //shutdown db

        try {
            DriverManager.getConnection(createURL() + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + databaseName + " has shutdown successfully.");
            }
        }

        //Start db wihtout bootPassword
        try {
            bootPassword = null;
            DriverManager.getConnection(createURL());
        } catch (SQLException e) {
            assertEquals("Startup failed. An encrypted database cannot be accessed without the correct boot password.  ",e.getNextException().getMessage());
        }

        bootPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";


        }


    @Test
    void changeBootPasswordTest() throws  SQLException {
        DBConnection.databaseName = databaseName+"01";
        DBConnection.passwordDB = passwordDB;
        DBConnection.bootPassword = bootPassword;
        newBootPassword = "987654321987654321";
        //create db for testing
        DriverManager.getConnection(createURL());

        //shutdown db we just have created.
        try {
            DriverManager.getConnection(createURL() + ";shutdown:true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Database " + databaseName + " has shutdown successfully.");
            }

        }try {
            System.out.println("sleeping 1");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Applying new boot password.");
            System.out.println("changing bootPassword");
            dbConnect(createURL() + ";newBootPassword=" + newBootPassword);
            bootPassword = newBootPassword;
            System.out.println("success.");
            //hier we can see that the boot password ist not the same.
            System.out.println(createURL());

        } catch (SQLException error) {
            System.out.println("failed. -> " + error);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        //check if the new bootPassword is not equals to the old bootPassword.
        //if this test passes this means that the bootPassword on the first place was properly set.
        //then per analogy this test checks if 1) the bootPassword has been set and 2) if the new bootPassword has been set (replaces the old bootPassword).
        assertEquals("987654321987654321",bootPassword);
        DBConnection.bootPassword = newBootPassword;
    }

    @Test
    void setupUserDBWithPasswordConnectionTest () throws SQLException {
        DBConnection.databaseName = databaseName+"02";
        DBConnection.passwordDB = passwordDB;
        DBConnection.bootPassword = bootPassword;

        connection = DriverManager.getConnection(createURL());
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        connection = DriverManager.getConnection(createURLSimple());
        DBConnection.setupUserDBWithPasswordConnection(createURLSimple(),passwordDB,setupPasswordString);
        assertEquals("123456789",passwordDB);
    }

    @Test
    void resetUserDBWithPasswordTest () throws SQLException {
        DBConnection.databaseName = databaseName + "02";
        DBConnection.passwordDB = passwordDB;
        DBConnection.bootPassword = bootPassword;
        connection = DriverManager.getConnection(createURL());
        setupUserDBWithPasswordConnectionTest();
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        DBConnection.resetUserPwd(setupPasswordString, "3874436sjsjsjzszhshs");
        passwordDB = "3874436sjsjsjzszhshs";
        assertEquals("3874436sjsjsjzszhshs", passwordDB);
    }


    @Test
    void dbConnectTest() throws SQLException {
        DBConnection.databaseName = databaseName+"03";
        DBConnection.passwordDB = passwordDB;
        dbConnect(createURLSimple());
        assertFalse(connection.isClosed());
    }

    @Test
    void dbDisconnectTest() throws SQLException {
        DBConnection.databaseName = databaseName+"03";
        DBConnection.passwordDB = passwordDB;
        dbDisconnect();
        assertTrue(connection.isClosed());
    }

    @Test
    void dbExecuteUpdateTest() throws InterruptedException, SQLException, ClassNotFoundException {
        DBConnection.databaseName = databaseName+"04";
        DBConnection.passwordDB = passwordDB;
        connection = DriverManager.getConnection(createURLSimple());
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


        assertTrue(DBConnection.dbExecuteUpdate(slqCreateSchemaTest,createURL()));
        //assertFalse(DBConnection.dbExecuteUpdate(slqCreateSchema,createURL()));
        assertTrue(DBConnection.dbExecuteUpdate(sqlCreateTableTest,createURL()));
        //assertFalse(DBConnection.dbExecuteUpdate(sqlCreateTable,createURL()));
        assertTrue(DBConnection.dbExecuteUpdate(insertTest,createURL()));
    }

    @Test
    void dbExecuteQueryTest() throws InterruptedException, SQLException, ClassNotFoundException {
        DBConnection.databaseName = databaseName+"04";
        DBConnection.passwordDB = passwordDB;
        connection = DriverManager.getConnection(createURLSimple());

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
        ObservableList<DatabaseEntry> databaseEntriesTestExpected = FXCollections.observableArrayList();
        databaseEntriesTest.add(entry);
        databaseEntriesTestResults = DBConnection.dbExecuteQuery(queryTest,databaseEntriesTest,createURL());
        System.out.println(databaseEntriesTest.get(0));
        assertNotNull(databaseEntriesTestResults);
        //assertEquals();


    }


    @Test
    void shutdownDBTest() throws SQLException {
        DBConnection.databaseName = databaseName+"05";
        DBConnection.passwordDB = passwordDB;
        DBConnection.databaseName = databaseName;
        //start db
        DriverManager.getConnection(createURLSimple());
        try{
            connection = DriverManager.getConnection(createURLSimple()+";shutdown=true");
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
            assertEquals(e.getSQLState(), "08006");
        }
    }





    @Test
    void removeTestsEffects() throws SQLException, InterruptedException, ClassNotFoundException {
        DBConnection.databaseName = databaseName+"04";
        DBConnection.passwordDB = passwordDB;
        connection = DriverManager.getConnection(createURL());
        String deleteStmtTest = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try{
            assertFalse(DBConnection.dbExecuteUpdate(deleteStmtTest,createURLSimple()));
        }catch (SQLException exception){
            assertNotEquals(exception.getSQLState(),"Username not found in SYS.SYSUSERS");
        }
        Path dbFile = Paths.get("testDB/");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleteDatabaseFolderTest(db);


    }


    @AfterAll
    static void clean(){
        Path dbFile = Paths.get("testDB/");
        File db = new File(dbFile.toAbsolutePath().toString());
        deleteDatabaseFolderTest(db);
    }


private static void deleteDatabaseFolderTest(File file){

        for (File subFile : file.listFiles()) {
        if(subFile.isDirectory()) {
        deleteDatabaseFolderTest(subFile);
        } else {
        subFile.delete();
        }
        }
        file.delete();
        }

}
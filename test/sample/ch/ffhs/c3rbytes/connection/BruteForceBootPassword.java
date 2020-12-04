package sample.ch.ffhs.c3rbytes.connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

class BruteForceBootPassword {
    String passwordDB = "123456789";
    String bootPassword = "password123";
    String databaseName = "testDB/bruteForcedDB";
    String HASHALGORITHM = "SHA3-512";
    Connection connectionTest = null;

    @Test
    public void bruteForceBootPasswordTest() throws SQLException {
        DBConnection.databaseName = databaseName;
        DBConnection.passwordDB = passwordDB;
        StringHasher stringHasher = new StringHasher();
        DBConnection.bootPassword = stringHasher.encryptSHA3(HASHALGORITHM,bootPassword);
        //we create an encrypted database
        connectionTest = DriverManager.getConnection(createURL());
        try{
            // we shut it down
            connection = DriverManager.getConnection(createURLSimple()+";shutdown=true");
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
            assertEquals(e.getSQLState(), "08006");
        }

        String tmpBootPassword = null;
        File hacker = null;
        Scanner myReader = null;
        String tmpHashedBootPassword = null;
        Boolean up = false;

        try {
            //we load a password text files with passwords.
            Path passwordsFilePath = Paths.get("test/sample/ch/ffhs/c3rbytes/connection/10-million-password-list-top-1000000.txt");
            hacker = new File(passwordsFilePath.toAbsolutePath().toString());
             //hacker = new File("10-million-password-list-top-1000000.txt");
             myReader = new Scanner(hacker);
            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            while (myReader.hasNextLine()){
                tmpBootPassword = myReader.nextLine();
                //we hash the password
                StringHasher stringHasher2 = new StringHasher();
                tmpHashedBootPassword = stringHasher.encryptSHA3(HASHALGORITHM,tmpBootPassword);
                try{
                    System.out.println("trying password "+tmpBootPassword);
                    //we try to decrypt and boot the database
                    connectionTest = DriverManager.getConnection(createURL());
                }catch (SQLException ex){
                    if(ex.getMessage().equals("Startup failed. An encrypted database cannot be accessed without the correct boot password.  ")){
                        System.out.println("wrong password "+tmpBootPassword);
                        throw ex;
                    }
                }
                //if the database has booted, we threw no exception. We compare the two values (unhashed). if they matched, we exit the while-loop.
                if(tmpBootPassword.equals(bootPassword)){
                    break;
                }
            }
            //we print the result and check if the connectionTest is closed (return false). which should not be the case because the database has booted.
            System.out.println("BootPassword found "+tmpBootPassword);
            assertFalse(connectionTest.isClosed());

        }


        @AfterAll
        static void clean(){
            Path dbFile = Paths.get(DBConnection.databaseName);
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




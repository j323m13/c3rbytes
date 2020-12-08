package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.connection.DBConnection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

public class DatabaseEntryDao implements Dao{


    /*
    * Methode to retrieve all the entries from the Database.
    * @return ObservableList databaseEntries
    * @see DBConnection.java dbExecuteQuery()
     */
    public static ObservableList<DatabaseEntry> getAll() throws SQLException, ClassNotFoundException, InterruptedException {
        String getAll = "SELECT * FROM \"CERBYTES\".\"database_entries\"";
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
        try{
            dbExecuteQuery(getAll,databaseEntries,createURLSimple());
        }catch (SQLException ex){
            if(ex.getSQLState().equals("X0Y32")){
                System.out.println("Table is empty");
            }
        }
        return databaseEntries;
    }


    /*
    * this methode save a databaseEntry object to the database
    * @param a databaseEntry object
    * @see DBConnection.class
     */
    @Override
    public Dao save(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        System.out.println(entry.getUsername()+", "+entry.getDescription()+", "+
        entry.getPassword()+", "+ entry.getCreationDate()+", "+entry.getLastUpdate()+", "+entry.getUrl());

        String saveStmt = "INSERT INTO \"CERBYTES\".\"database_entries\"\n" +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"date_update\", \"note\" )\n" +
                "VALUES('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+"','"+entry.getCreationDate()+"', '"+entry.getLastUpdate()+"','"+entry.getNote()+"')";

        try {
            System.out.println("SAVE -> -> -> ");
            dbExecuteUpdate(saveStmt,createURLSimple());
        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            System.out.print("Error occurred while insert Operation: " + e);

        }
        return null;
    }

    /*
     * this methode update a databaseEntry object to the database
     * @param a databaseEntry object
     * @see DBConnection.class
     */
    @Override
    public Dao update(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE \"CERBYTES\".\"database_entries\"\n" +
                "SET \"username\"='"+entry.getUsername()+"', \"description\"='"+entry.getDescription()+"'," +
                "\"url_content\"='"+entry.getUrl()+"', \"password_text\"='"+entry.getPassword()+"', \"date_update\"='"+entry.getLastUpdate()+"'," +
                "\"note\"='"+entry.getNote()+"' WHERE \"user_id\"="+Integer.parseInt(entry.getId())+" ";
        try {
            dbExecuteUpdate(updateStmt,createURLSimple());
        }catch (SQLException | InterruptedException e) {
                System.out.print("Error occurred while DELETE Operation: " + e);

            }
        return null;

    }



    /*
    * Methode to delete an entry
    * @para entry (DatabaseEntry)
     */

    @Override
    public Dao delete(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        String deleteStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"\n" +
                "WHERE \"date_creation\"='"+entry.getCreationDate()+"'";
        try {
            dbExecuteUpdate(deleteStmt,createURLSimple());
        }catch (SQLException | InterruptedException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);

        }

        return null;
    }

    /*
    * Methode to delete the account. it deletes all the entries from the database.
    * Delete also the database folders
    * @return null
     */
    public Dao deleteAccount() throws SQLException, ClassNotFoundException {
        System.out.print("account will be deleted");
        String deleteAccountStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try {
            //delete db file
            dbExecuteUpdate(deleteAccountStmt,createURLSimple());
            Path dbFile = Paths.get("db");
            File db = new File(dbFile.toAbsolutePath().toString());
            deleteDatabaseFolder(db);
            //delete db log file
            Path logFile = Paths.get("derby.log");
            File log = new File(logFile.toAbsolutePath().toString());
            log.delete();
            //delete c3r.c3r file
            dbExecuteUpdate(deleteAccountStmt,createURLSimple());
            String osName = null;
            osName = System.getProperty("os.name");
            String fileName = null;
            if(osName.equals("win")){
                fileName = "c3r.c3r";
            }else{
                fileName = ".c3r.c3r";
            }
            Path c3rfile = Paths.get(fileName);
            File c3r = new File(c3rfile.toAbsolutePath().toString());
            c3r.delete();
        }catch (SQLException | ClassNotFoundException | InterruptedException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);

        }

        return null;
    }

    private void deleteDatabaseFolder(File file){
            for (File subFile : file.listFiles()) {
                if(subFile.isDirectory()) {
                    deleteDatabaseFolder(subFile);
                } else {
                    subFile.delete();
                }
            }
            file.delete();
    }


    /*
    * call the dbConnect methode from DBConnenction.java
     */
    @Override
    public void connect() throws SQLException {
        dbConnect(createURL());
    }

    /*
     * Setup a new password for the database user. it is not the bootPassword, which encrypt the database. it is only for queries with the db.
     */

    @Override
    public void setup() throws SQLException, ClassNotFoundException, InterruptedException {
        //setupEncryption(bootPassword);
        setupUserDBWithPassword(passwordDB);
        setupTable();

    }

    public void setupUserDBWithPassword(String newPasswordDB) throws SQLException, InterruptedException, ClassNotFoundException {
        //TODO get a string 10CHARS from bootpassword as passwordDB
        //on first call, database will be create if it does not exist: (create=true).
        String url = createURLSimple();
        // set userDB's password
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        setupUserDBWithPasswordConnection(url, newPasswordDB, setupPasswordString);
    }

    public void resetUserDBWithPassword(String newPasswordDB) throws SQLException, InterruptedException, ClassNotFoundException {
        //TODO get a string 10CHARS from bootpassword as passwordDB
        //on first call, database will be create if it does not exist: (create=true).
        // set userDB's password
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(setupPasswordString, newPasswordDB);
    }

    public void changeBootPassword(String newBootPassword, String newPasswordDB) throws SQLException {
        shutdownDB();
            try {
                System.out.println("sleeping 1");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Applying new boot password.");
                dbConnect(createURL() + ";newBootPassword=" + newBootPassword);
                bootPassword = newBootPassword;
                System.out.println("applying new database password.");
                resetUserDBWithPassword(newPasswordDB);
                System.out.println("success.");
            } catch (SQLException error) {
                System.out.println("failed. -> "+error);
            } catch (InterruptedException | ClassNotFoundException interruptedException) {
                interruptedException.printStackTrace();
            }
        }



    public void setupTable() throws SQLException, ClassNotFoundException, InterruptedException {
        String urlForSetupSchemaAndTable = createURLSimple()+";password="+passwordDB;
        String slqCreateSchema = "CREATE SCHEMA \"CERBYTES\"";
        String sqlCreateTable = "CREATE TABLE \"CERBYTES\".\"database_entries\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(500) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(500) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(50) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(50) DEFAULT NULL,\n" +
                "                       \"note\" CLOB(2K) DEFAULT NULL)";


        dbExecuteUpdate(slqCreateSchema,urlForSetupSchemaAndTable);
        dbExecuteUpdate(sqlCreateTable,urlForSetupSchemaAndTable);

    }

    public void setupEncryption(String encryptionKey) throws InterruptedException, ClassNotFoundException, SQLException {
        bootPassword = encryptionKey;
        /*
        String encryptString = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('bootPassword', '"+ bootPassword+","+bootPassword+"')";
        //setupDBEncryption(encryptionKey);
        //dbExecuteUpdate(encryptString,createURL());
        dbConnect(createURLSimple()+";bootPassword="+bootPassword+"");
        System.out.println("DB ist encrypted with: "+bootPassword);
         */

        dbConnect(createURL());


    }

    public ObservableList<DatabaseEntry> searchElement(String searched, ObservableList databaseEntries){
        String element = "SELECT \"user_id\",\"username\",\"description\",\"url_content\",\"password_text\",\"date_creation\",\"date_update\",\"note\"\n" +
                "FROM \"CERBYTES\".\"database_entries\"\n" +
                "WHERE \"username\" LIKE '%"+searched+"%'\n" +
                "OR \"description\" LIKE '%"+searched+"%'\n" +
                "OR \"url_content\" LIKE '%"+searched+"%'\n" +
                "OR \"note\" LIKE '%"+searched+"%'";
        System.out.println(element);
        try {
            dbExecuteQuery(element,databaseEntries,createURLSimple());
        }catch (SQLException | ClassNotFoundException | InterruptedException e) {
            System.out.print("Error occurred while search Operation: " + e);
        }
        return databaseEntries;
    }

    public void shutdown() throws SQLException {
        shutdownDB();
    }

    public String getUrl() {
        return DBConnection.createURL();
    }

}





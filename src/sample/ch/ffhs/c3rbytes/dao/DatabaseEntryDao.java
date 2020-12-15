package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeUnit;

import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

public class DatabaseEntryDao implements Dao{

    /**
     * Methode to retrieve all the entries from the Database.
     * @return an observableList ObservableList<DatabaseEntry> which contains the results of the query.
     * @throws SQLException if the transaction failed, an error is raised.
     * @throws ClassNotFoundException if the table or the schema are not set, an error is raised.
     * @throws InterruptedException if the task is interrupted, an error is raised.
     */
    @Override
    public ObservableList<DatabaseEntry> getAll() throws SQLException, ClassNotFoundException, InterruptedException {
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



    /**
     * Save a databaseEntry object to the database
     * @param entry a databaseEntry object
     * @return true if the transaction is successful.
     * @throws SQLException if the transaction failed, an error is raised.
     * @throws ClassNotFoundException if the table or the schema are not set, an error is raised.
     * @throws InterruptedException if the task is interrupted, an error is raised.
     */
    @Override
    public boolean save(DatabaseEntry entry) throws SQLException, ClassNotFoundException, InterruptedException {
        System.out.println(entry.getUsername()+", "+entry.getDescription()+", "+
        entry.getPassword()+", "+ entry.getCreationDate()+", "+entry.getLastUpdate()+", "+entry.getUrl());

        String saveStmt = "INSERT INTO \"CERBYTES\".\"database_entries\"\n" +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"date_update\", \"note\" )\n" +
                "VALUES('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+"','"+entry.getCreationDate()+"', '"+entry.getLastUpdate()+"','"+entry.getNote()+"')";

        try {
            System.out.println("SAVE -> -> -> ");
            dbExecuteUpdate(saveStmt,createURLSimple());
        } catch (SQLException e) {
            System.out.print("Error occurred while insert Operation: " + e);
            return false;

        }
        return true;
    }



    /**
     * this methode update a databaseEntry object to the database
     * @param entry a databaseEntry object
     * @throws SQLException if the transaction failed, an error is raised.
     * @throws ClassNotFoundException if the table or the schema are not set, an error is raised.
     */
    @Override
    public void update(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE \"CERBYTES\".\"database_entries\"\n" +
                "SET \"username\"='"+entry.getUsername()+"', \"description\"='"+entry.getDescription()+"'," +
                "\"url_content\"='"+entry.getUrl()+"', \"password_text\"='"+entry.getPassword()+"', \"date_update\"='"+entry.getLastUpdate()+"'," +
                "\"note\"='"+entry.getNote()+"' WHERE \"user_id\"="+Integer.parseInt(entry.getId())+" ";
        try {
            dbExecuteUpdate(updateStmt,createURLSimple());
        }catch (SQLException e) {
                System.out.print("Error occurred while DELETE Operation: " + e);
            }

    }


    /**
     * Methode to delete an entry in the database
     * @param entry the entry to be delete in the database
     * @throws SQLException if the transaction failed, an error is raised.
     * @throws ClassNotFoundException if the table or the schema are not set, an error is raised.
     */
    @Override
    public void delete(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        String deleteStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"\n" +
                "WHERE \"date_creation\"='"+entry.getCreationDate()+"'";
        try {
            dbExecuteUpdate(deleteStmt,createURLSimple());
        }catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);

        }

    }



    /**
     * Methode to delete the account. it deletes all the entries from the database.
     * Delete also the database folders
     * @throws SQLException if an error with the query to the database occurs, an error is raised.
     * @throws InterruptedException require for the sleep function.
     */
    public void deleteAccount() throws SQLException, InterruptedException {
        System.out.print("account will be deleted");
        String deleteAccountStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        OSBasedAction handler = new OSBasedAction();
        try {
            //delete db table
            dbExecuteUpdate(deleteAccountStmt,createURLSimple());
            String fileName;
            if(handler.getOSName().contains("win")){
                fileName = "c3r.c3r";
            }else{
                fileName = ".c3r.c3r";
            }
            //delete c3r.c3r file
            handler.deleteDatabaseFolder(handler.getPath(fileName));
        }catch (SQLException | IOException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);

        }
        shutdown();
        TimeUnit.SECONDS.sleep(1);
        try {
            //delete db folder
            handler.deleteDatabaseFolder(handler.getPath("db"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("delete Action failed");
        }
        TimeUnit.SECONDS.sleep(1);
        try{
            //delete db log file (derby.log)
            handler.deleteDatabaseFolder(handler.getPath("derby.log"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("delete log file failed");

        }

    }


    /**
     * call the dbConnect methode from DBConnection.java
     * @throws SQLException if the transaction failed, an error is raised.
     */
    @Override
    public void connect() throws SQLException {
        dbConnect(createURL());
    }


    /**
     * call the dbConnect methode from DBConnenction.java with a newBootPassword element.
     * use this methode when changing the bootpassword.
     * @param newBootPassword the new boot password
     * @throws SQLException if the transaction failed, an error is raised.
     */
    public void applyNewBootPassword(String newBootPassword) throws SQLException {
        System.out.println("new BootPassword "+createURL()+";newBootpassword="+newBootPassword);
        dbConnect(createURL() + ";newBootPassword=" + newBootPassword);
    }



    /**
     * Setup a new password for the database user. it is not the bootPassword, which encrypt the database. it is only for queries with the db.
     * @throws SQLException if the transaction failed, an error is raised.
     * @throws ClassNotFoundException if the table or the schema already exists, an error is thrown.
     */
    @Override
    public void setup() throws SQLException, InterruptedException, ClassNotFoundException {
        setupUserDBWithPassword(getPasswordDB());
        setupTable();

    }


    /**
     * Methode to setup user of the db with a password.
     * on first call, database will be create if it does not exist: (create=true).
     * DB is created with an url passed to dbConnect()
     * @param newPasswordDB the new password
     * @throws SQLException if the transaction failed, an error is raised.
     */
    public void setupUserDBWithPassword(String newPasswordDB) throws SQLException {
        // set userDB's password
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        setupUserDBWithPasswordConnection(createURLSimple(), newPasswordDB, setupPasswordString);
    }

    /**
     * Reset the password of the user of the Database
     * @param newPasswordDB the new password
     * @throws SQLException if the transaction failed, an error is raised.
     */
    public void resetUserDBWithPassword(String newPasswordDB) throws SQLException {
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(setupPasswordString, newPasswordDB);
    }



    /**
     * change the bootpassword of the db. it is the encryption key.
     * @param newBootPassword a new Bootpassword (encryption key)
     * @param newPasswordDB a new Password for the database user (cerbytes)
     * @throws SQLException if an error occur during this process, an SQL exception is raised.
     */
    public void changeBootPassword(String newBootPassword, String newPasswordDB) throws SQLException {
        //shutdown the database. without a shutdown, this action is not allowed.
        shutdown();
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Applying new boot password.");
                applyNewBootPassword(newBootPassword);
                setBootPasswordDAO("");
                //if the operation was successful, we set the new bootPassword for later use
                setBootPasswordDAO(newBootPassword);
                System.out.println("applying new database password.");
                //we set a new password for the db user.
                resetUserDBWithPassword(newPasswordDB);
                System.out.println("success.");
                //we try to start it with the new bootpassword.
                connect();
            } catch (SQLException error) {
                System.out.println("failed. -> "+error);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }


    /*
    *
     */

    /**
     * Methode to setup the table in the database
     * Setup schema (CERBYTES)
     * setup table (database_entries)
     * @throws SQLException if the schema or the table are already created, an exception is raised.
     */
    public void setupTable() throws SQLException {
        String urlForSetupSchemaAndTable = createURLSimple()+";password="+getPasswordDBDAO();
        String slqCreateSchema = "CREATE SCHEMA \"CERBYTES\"";
        String sqlCreateTable = "CREATE TABLE \"CERBYTES\".\"database_entries\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(80) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(25) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(100) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(50) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(50) DEFAULT NULL,\n" +
                "                       \"note\" CLOB(2K) DEFAULT NULL)";


        dbExecuteUpdate(slqCreateSchema,urlForSetupSchemaAndTable);
        dbExecuteUpdate(sqlCreateTable,urlForSetupSchemaAndTable);

    }

    /**
     * methode to setup the encryption.
     * During the first start (and first creation of the db), a bootPassword is applied and become the encryption key.
     * to start the db, the bootpassowrd has to be provided.
     * @param encryptionKey the bootpassword tio be used as the encryption key.
     * @throws SQLException if the connect() methode encounters a problem with the database, a sql exception is raised.
     */
    public void setupEncryption(String encryptionKey) throws SQLException {
        setBootPasswordDAO(encryptionKey);
        dbConnect(createURL());
    }


    /**
     * Methode to search the db with a searchElement
     * @param searched the element string to be searched.
     * @param databaseEntries an observable to hold the results of the search query.
     * @return the results (databaseEntries)
     */
    public ObservableList<DatabaseEntry> searchElement(String searched, ObservableList<DatabaseEntry> databaseEntries){
        String element = "SELECT \"user_id\",\"username\",\"description\",\"url_content\",\"password_text\",\"date_creation\",\"date_update\",\"note\"\n" +
                "FROM \"CERBYTES\".\"database_entries\"\n" +
                "WHERE \"username\" LIKE '%"+searched+"%'\n" +
                "OR \"description\" LIKE '%"+searched+"%'\n" +
                "OR \"url_content\" LIKE '%"+searched+"%'\n" +
                "OR \"note\" LIKE '%"+searched+"%'";
        System.out.println(element);
        try {
            dbExecuteQuery(element,databaseEntries,createURLSimple());
        }catch (SQLException | InterruptedException e) {
            System.out.print("Error occurred while search Operation: " + e);
        }
        return databaseEntries;
    }

    /**
     * Methode to disconnect
     * @throws SQLException if there is a connection problem with the db, an exception is raised.
     */
    @Override
    public void disconnect() throws SQLException {
        dbDisconnect();
    }

    /**
     * shutdown the database
     * @throws SQLException if there is a connection problem with the db, an exception is raised.
     */
    public void shutdown() throws SQLException {
        shutdownDB();
    }

    /**
     * Methode to get the url (JDBC_URL). call createURL() (with bootPassword)
     * @return url the JDBC_URL
     */
    public String getUrl() {
        return createURL();
    }

    /**
     * set the password of the db
     * @param passwordDBString the password string to be set as the password for the DB
     */
    public void setPasswordDBDAO (String passwordDBString){
        setPasswordDB(passwordDBString);
    }

    /**
     * set the bootPassword of the db
     * @param bootPasswordString the bootpassword string to set as the bootpassword.
     */
    public void setBootPasswordDAO (String bootPasswordString){
        setBootPassword(bootPasswordString);
    }

    /**
     * set the local values of the user of the software.
     * @param localValuesString the string containing the local values of the user (of the software)
     */
    public void setLocalValuesDAO (String localValuesString){
        setLocalValues(localValuesString);
    }


    /**
     * set the database name
     * @param databaseNameString the name of the database
     */
    public void setDatabaseNameDAO(String databaseNameString){ setDatabaseName(databaseNameString); }

    /**
     * get the password value
     * @return the Password of the DB
     */
    public String getPasswordDBDAO(){
        return getPasswordDB();
    }

    /**
     * get the bootPassword value
     * @return the BootPassword
     */
    public String getBootPasswordDAO(){
        return getBootPassword();
    }

    /**
     * get the database Name
     * @return database Name
     */
    public String getDatabaseNameDAO(){ return getDatabaseName();}




}





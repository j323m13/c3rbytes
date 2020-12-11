package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.sql.*;
import java.util.concurrent.TimeUnit;

import static sample.ch.ffhs.c3rbytes.connection.DBConnection.*;

public class DatabaseEntryDao implements Dao{


    /*
    * Methode to retrieve all the entries from the Database.
    * @return ObservableList databaseEntries
    * @see DBConnection.java dbExecuteQuery()
     */
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


    /*
    * this methode save a databaseEntry object to the database
    * @param a databaseEntry object
    * @see DBConnection.class
     */
    @Override
    public boolean save(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
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
            return false;

        }
        return true;
    }

    /*
     * this methode update a databaseEntry object to the database
     * @param a databaseEntry object
     * @see DBConnection.class
     */
    @Override
    public void update(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE \"CERBYTES\".\"database_entries\"\n" +
                "SET \"username\"='"+entry.getUsername()+"', \"description\"='"+entry.getDescription()+"'," +
                "\"url_content\"='"+entry.getUrl()+"', \"password_text\"='"+entry.getPassword()+"', \"date_update\"='"+entry.getLastUpdate()+"'," +
                "\"note\"='"+entry.getNote()+"' WHERE \"user_id\"="+Integer.parseInt(entry.getId())+" ";
        try {
            dbExecuteUpdate(updateStmt,createURLSimple());
        }catch (SQLException | InterruptedException e) {
                System.out.print("Error occurred while DELETE Operation: " + e);

            }

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
    public void deleteAccount() {
        System.out.print("account will be deleted");
        String deleteAccountStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try {
            OSBasedAction handler = new OSBasedAction();
            //delete db file
            dbExecuteUpdate(deleteAccountStmt,createURLSimple());
            handler.deleteDatabaseFolder(handler.getPath("db"));
            //delete db log file (derby.log)
            handler.deleteDatabaseFolder(handler.getPath("derby.log"));
            dbExecuteUpdate(deleteAccountStmt,createURLSimple());
            String fileName;
            if(handler.getOSName().contains("win")){
                fileName = "c3r.c3r";
            }else{
                fileName = ".c3r.c3r";
            }
            //delete c3r.c3r file
            handler.deleteDatabaseFolder(handler.getPath(fileName));
        }catch (SQLException | ClassNotFoundException | InterruptedException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);

        }

    }



    /*
    * call the dbConnect methode from DBConnenction.java
     */
    @Override
    public void connect() throws SQLException {
        dbConnect(createURL());
    }
    /*
     * call the dbConnect methode from DBConnenction.java with a newBootPassword element.
     * use this methode when changing the bootpassword.
     */
    public void applyNewBootPassword(String newBootPassword) throws SQLException {
        System.out.println("new BootPassword "+createURL()+";newBootpassword="+newBootPassword);
        dbConnect(createURL() + ";newBootPassword=" + newBootPassword);
    }

    /*
     * Setup a new password for the database user. it is not the bootPassword, which encrypt the database. it is only for queries with the db.
     */

    @Override
    public void setup() throws SQLException, ClassNotFoundException, InterruptedException {
        setupUserDBWithPassword(getPasswordDB());
        setupTable();

    }

    /*
    * Methode to setup user of the db with a password.
    * on first call, database will be create if it does not exist: (create=true).
    * DB is created with an url passed to dbConnect()
    * @see DBConnection.java
    * @see createURLSimple() (without bootPassword)
    * @see createURL() (with bootPassword)
     */
    public void setupUserDBWithPassword(String newPasswordDB) throws SQLException {
        //TODO get a string 10CHARS from bootpassword as passwordDB
        //on first call, database will be create if it does not exist: (create=true). -> see create
        // set userDB's password
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        setupUserDBWithPasswordConnection(createURLSimple(), newPasswordDB, setupPasswordString);
    }

    /*
    * Reset the password of the user of the Dtabase
    * @param the new password
    * @see resetUserPwd() from DBConnection.java
     */
    public void resetUserDBWithPassword(String newPasswordDB) throws SQLException, InterruptedException, ClassNotFoundException {
        //TODO get a string 10CHARS from bootpassword as passwordDB
        //on first call, database will be create if it does not exist: (create=true).
        // set userDB's password
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(setupPasswordString, newPasswordDB);
    }

    /*
    * change the bootpassword of the db. it is the encryption key.
    * @param newBootPassword (encryption key)
    * @param newPasswordDB (user of the db)
    * the db has to be shutdown to perform the change of the bootpassword. furthermore, the database owner (our user) has to own the database
    * if not, the action is not allowed.
     */
    public void changeBootPassword(String newBootPassword, String newPasswordDB) throws SQLException {
        //shutdown the dabase. without a shutdown, this action is not allowed.
        shutdown();
            try {
                System.out.println("new bootPassword "+newBootPassword);
                System.out.println("New passwordDB "+newPasswordDB);
                System.out.println("sleeping 1");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Applying new boot password.");
                applyNewBootPassword(newBootPassword);
                System.out.println("applying new database password.");
                setBootPasswordDAO("");
                setBootPasswordDAO(newBootPassword);
                resetUserDBWithPassword(newPasswordDB);
                System.out.println("new PasswordDB: "+getPasswordDBDAO());
                System.out.println("success.");
                System.out.println("test url full "+createURL());
                shutdown();
                connect();
            } catch (SQLException error) {
                System.out.println("failed. -> "+error);
            } catch (InterruptedException | ClassNotFoundException interruptedException) {
                interruptedException.printStackTrace();
            }
        }


    /*
    * Methode to setup the table in the database
    * Setup shema (CERBYTES)
    * setup table (database_entries)
     */
    public void setupTable() throws SQLException, ClassNotFoundException, InterruptedException {
        String urlForSetupSchemaAndTable = createURLSimple()+";password="+getPasswordDBDAO();
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

    }/*
    * methode to setup the encryption.
    * During the first start (and first creation of the db), a bootPassword is applied and become the encryption key.
    * to start the db, the bootpassowrd has to be provided.
    *@param encryptionKey (=bootPassword)
    */
    public void setupEncryption(String encryptionKey) throws InterruptedException, ClassNotFoundException, SQLException {
        setBootPasswordDAO(encryptionKey);
        connect();
    }

    /*
    * Methode to search the db with a searchElement
    * @param searched: a string to search
    * @param databaseEntries: an ObservableList to hold the result(s) and display it(them) on the tableView of the main view.
    * @return the databaEntries (observableList)
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
        }catch (SQLException | ClassNotFoundException | InterruptedException e) {
            System.out.print("Error occurred while search Operation: " + e);
        }
        return databaseEntries;
    }
    @Override
    public void disconnect() throws SQLException {
        dbDisconnect();
    }

    /*
    * Shutdown the DB
    * @see shutDownDB() in DBConnection.java
     */
    public void shutdown() throws SQLException {
        shutdownDB();
    }

    /*
    * Methode to get the url (JDBC_URL). call createURL() (with bootPassword)
     */
    public String getUrl() {
        return createURL();
    }


    public void setPasswordDBDAO (String passwordDBString){
        setPasswordDB(passwordDBString);
    }

    public void setBootPasswordDAO (String bootPasswordString){
        setBootPassword(bootPasswordString);
    }

    public void setLocalValuesDAO (String localValuesString){
        setLocalValues(localValuesString);
    }

    public void setDatabaseNameDAO(String databaseNameString){ setDatabaseName(databaseNameString); }

    public String getPasswordDBDAO(){
        return getPasswordDB();
    }

    public String getBootPasswordDAO(){
        return getBootPassword();
    }

    public String getDatabaseNameDAO(){ return getDatabaseName();}




}





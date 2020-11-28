package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.*;

import static sample.ch.ffhs.c3rbytes.dao.DBConnection.*;

public class DatabaseEntryDao implements Dao{


    /*
    * Methode to retrieve all the entries from the Database.
    * @return ObservableList databaseEntries
    * @see DBConnection.java dbExecuteQuery()
     */
    public static ObservableList<DatabaseEntry> getAll() throws SQLException, ClassNotFoundException {
        String getAll = "SELECT * FROM \"CERBYTES\".\"database_entries\"";
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
        dbExecuteQuery(getAll,databaseEntries);
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
            dbExecuteUpdate(saveStmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while insert Operation: " + e);
            throw e;
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
            dbExecuteUpdate(updateStmt);
        }catch (SQLException e) {
                System.out.print("Error occurred while DELETE Operation: " + e);
                throw e;
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
            dbExecuteUpdate(deleteStmt);
        }catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }

        return null;
    }

    /*
    * Methode to delete the account. it deletes all the entries from the database.
    * @return null
     */
    public Dao deleteAccount() throws SQLException, ClassNotFoundException {
        System.out.print("account will be deleted");
        String deleteAccountStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try {
            dbExecuteUpdate(deleteAccountStmt);
        }catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
        return null;
    }

    /*
    * call the dbConnect methode from DBConenction.java
     */
    @Override
    public void connect() throws SQLException {
        DBConnection.dbConnect();
    }

    /*
    * Setup a new password for the database user. it is not the bootPassword, which encrypt the database. it is only for queries with the db.
     */
    public void setup(String passwordDB, String bootPasswordHashed) throws SQLException, ClassNotFoundException {
        String oldPassword = DBConnection.passwordDB;
        DBConnection.passwordDB = passwordDB;
        String url = "jdbc:derby:"+DBConnection.databaseName+";create=true;username="+DBConnection.userDB+"";
        //first connection to the DB. create DB if not exist.
        //setupDBEncryption(url,passwordDB, bootPasswordHashed);
        setupDatabase();


    }
    //TODO to implement correctly
    public void onNewStartup(boolean startup) throws SQLException, ClassNotFoundException {
        if(startup){
            setupUserDBWithPassword();
            setupTable();
            //setupEncryption()
        }

    }

    public void setupUserDBWithPassword() throws SQLException {
        String urlx = "jdbc:derby:"+ databaseName+";create=true;user="+userDB+"";
        String setupPasswordString = "CALL SYSCS_UTIL.SYSCS_CREATE_USER(?,?)";
        setupUserDBWithPasswordConnection(urlx, "123456789", setupPasswordString);
    }

    public void resetUserDBPassword(String newUserDBPassword){
        //TODO
        String resetPassord = "CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?,?)";
        resetUserPwd(resetPassord, newUserDBPassword);

    }

    public void setupTable() throws SQLException, ClassNotFoundException {
        setupDatabase();
    }

    public void setupEncryption() throws SQLException {
        setupDBEncryption();

    }

    public ObservableList<DatabaseEntry> searchElement(String searched, ObservableList databaseEntries){
        String element = "SELECT \"user_id\",\"username\",\"description\",\"url_content\",\"password_text\",\"date_creation\",\"date_update\",\"note\"\n" +
                "FROM \"CERBYTES\".\"database_entries\"\n" +
                "WHERE \"username\" = '"+searched+"'\n" +
                "OR \"description\"='"+searched+"'\n" +
                "OR \"url_content\"='"+searched+"'";
        System.out.println(element);
        try {
            dbExecuteQuery(element,databaseEntries);
        }catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while search Operation: " + e);
        }
        return databaseEntries;
    }


    /*
    * Methode to create a simple DatabaseEntry object
    * @return a databaseEntry Object
     */
    public DatabaseEntry createSimple(String username, String password, String description, String url) {
        return new DatabaseEntry(username, description, url, password);
    }

    public String getUrl() {
        return DBConnection.createURL();
    }
}


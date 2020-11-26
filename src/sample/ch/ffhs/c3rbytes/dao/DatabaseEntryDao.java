package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.*;

public class DatabaseEntryDao implements Dao{


    private static final QueryRunner dbAccess = new QueryRunner();
    private final ResultSet rs = null;
    private DBConnection connection;

    private DatabaseEntry createSimple(String id, String username, String description,
                                 String url, String password){
        return new DatabaseEntry(username, description, url, password);
    }



    public static ObservableList<DatabaseEntry> getAll() throws SQLException, ClassNotFoundException {
        String getAll = "SELECT * FROM \"CERBYTES\".\"database_entries\"";
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
        DBConnection.dbExecuteQuery(getAll,databaseEntries);
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
            DBConnection.dbExecuteUpdate(saveStmt);
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
            DBConnection.dbExecuteUpdate(updateStmt);
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
            DBConnection.dbExecuteUpdate(deleteStmt);
        }catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }

        return null;
    }

    public Dao deleteAccount() throws SQLException, ClassNotFoundException {
        System.out.print("account will be deleted");
        String deleteAccountStmt = "DELETE FROM \"CERBYTES\".\"database_entries\"";
        try {
            DBConnection.dbExecuteUpdate(deleteAccountStmt);
        }catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
        return null;
    }


    public DatabaseEntry createSimple(String username, String password, String description, String url) {
        return new DatabaseEntry(username, description, url, password);
    }
}



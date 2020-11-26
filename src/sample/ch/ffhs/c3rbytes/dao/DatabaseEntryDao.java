package sample.ch.ffhs.c3rbytes.dao;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.apache.commons.dbutils.QueryRunner;

import javax.xml.crypto.Data;
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
        String updateStmt = "UPDATE FROM \"CERBYTES\".\"database_entries\"\n" +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"note\" )\n" +
                "VALUES('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+"','"+entry.getLastUpdate()+"','"+entry.getNote()+"') WHERE \"user_id\"='"+entry.getId()+"' ";
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
/*
    @Override
    public Dao getEntryById(int id) throws SQLException, ClassNotFoundException {
        return null;
    }



       @Override
       public Dao getEntryById(int id) throws SQLException, ClassNotFoundException {
           Connection connection = DBConnection.getConnection();
           ResultSet rs;
           PreparedStatement stmt = null;
           rs = stmt.executeQuery("SELECT * FROM user WHERE id=" + id + "");
           while(rs.next()){
               return (Dao) extractEntryFromResultSet(rs);
           }
           return null;
       }

     methode to extract the values of a row (or multiples rows) from a query and save the values in a DataEntry Object.
     *@param rs take a Resultset object as paramenter and save the values inside a DatabaseEntry object
     * @return entry a DatabaseEntry Object which is a row in the Dababase

    private static DatabaseEntry extractEntryFromResultSet(ResultSet rs) throws SQLException{
        DatabaseEntry entry = new DatabaseEntry();
        //DatabaseEntry.setId(rs.getInt("id"));
        DatabaseEntry.setUsername(rs.getString(1));
        DatabaseEntry.setDescription(rs.getString(2));
        DatabaseEntry.setUrl(rs.getString(3));
        DatabaseEntry.setPassword(rs.getString(4));
        DatabaseEntry.setCreationDate(rs.getString(5));
        DatabaseEntry.setLastUpdate(rs.getString(6));
        return entry;
    }


    @Override
    public Dao save() {
        return null;
    }

    @Override
    public Dao update() {
        return null;
    }

    @Override
    public Dao delete() {
        return null;
    }


     * Insert a DatabaseEntry Object into the Database
     *
    */
    public void insertDatabaseEntry(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException {
        /*
        DBConnection helper = new DBConnection();
        //System.out.println("connection to insert "+helper.getUrlWithParameters());
        //Connection connection = DriverManager.getConnection(helper.getUrlWithParameters());
        //PreparedStatement ps = connection.prepareStatement("INSERT INTO database_entries " +
                "(username, description, url_content, password_text, date_creation, date_update)" +
                " VALUES (?,?,?,?,?,?)");
        ps.setString(1, databaseEntry.getUsername());
        ps.setString(2, databaseEntry.getDescription());
        ps.setString(3, databaseEntry.getUrl());
        ps.setString(4, databaseEntry.getPassword());
        ps.setString(5, databaseEntry.getCreationDate());
        ps.setString(6, databaseEntry.setLastUpdate().toString());

        int i = ps.executeUpdate();
        //connection.close();
         */

    }


    public DatabaseEntry createSimple(String username, String password, String description, String url) {
        return new DatabaseEntry(username, description, url, password);
    }
}



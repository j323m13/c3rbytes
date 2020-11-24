package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.commons.dbutils.QueryRunner;

import javax.xml.crypto.Data;
import java.sql.*;

public class DatabaseEntryDao implements Dao{


    private static final QueryRunner dbAccess = new QueryRunner();

    private DatabaseEntry createSimple(String id, String username, String description,
                                 String url, String password){
        return new DatabaseEntry(username, description, url, password);
    }



    public ObservableList<DatabaseEntry> getAll(ObservableList<DatabaseEntry> databaseEntries) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = null;

        ps = connection.prepareStatement("SELECT * FROM CERBYTES.\"database_entries\"");
        ResultSet rs = null;
        rs = ps.executeQuery();

        //TODO implement List as DataEntry Type




        while(rs.next()) {
            String tmpId = rs.getString(1);
            String tmpUsername = rs.getString(2);
            String tmpDescription = rs.getString(3);
            String tmpUrl = rs.getString(4);
            String tmpPassword = rs.getString(5);
            String tmpCreationDate = rs.getString(6);
            String tmpLastUpdate = rs.getString(7);
            DatabaseEntry catchResults = new DatabaseEntry(
                    tmpId,
                    tmpUsername,
                    tmpDescription,
                    tmpUrl,
                    tmpPassword,
                    tmpCreationDate,
                    tmpLastUpdate
            );

            //Print results in terminal for debugging
            System.out.println(rs.getInt(1) + "," +
                    rs.getString(2) + ", " + rs.getString(3) + ", " +
                    rs.getString(4) + ", " + rs.getString(5) + ", " +
                    rs.getString(6) + ", " +
                    rs.getString(7)
            );
            databaseEntries.add(catchResults);

        }
        //contain all the entries from the database CERBYTES (user only)
        //TODO implement join statement
        connection.close();
        return databaseEntries;


    }

    @Override
    public Dao getEntryById(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Dao save() {
        return null;
    }

    @Override
    public Dao update(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        if(entry !=null){
            Connection connection = DBConnection.getConnection();
            PreparedStatement update = null;
            update = connection.prepareStatement(
                    "UPDATE \"CERBYTES\".\"database_entries\" SET \"username\" = ?,"+
                    "\"description\"=?,"+
                    "\"url_content\"=?,"+
                    "\"password_text\"=?,"+
                    "\"date_creation\"=?,"+
                    "\"date_update\"=? WHERE \"user_id\"=?"
            );
            // we give the date to update
            update.setString(1,entry.getUsername());
            update.setString(2,entry.getDescription());
            update.setString(3,entry.getUrl());
            update.setString(4,entry.getPassword());
            update.setString(5,entry.getCreationDate());
            update.setString(6,entry.getLastUpdate());
            update.setString(7,entry.getId());
            try {
                update.executeUpdate();

            System.out.println("entry was updated succesfully.");
            }catch (SQLException e){
                System.out.print(e);
            }
        }
        return null;
    }






    @Override
    public Dao delete(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        if(entry !=null){
            Connection connection = DBConnection.getConnection();
            PreparedStatement delete = null;

            delete = connection.prepareStatement("DELETE FROM \"CERBYTES\".\"database_entries\" WHERE \"user_id\"="+entry.getId()+"");
            try {
                delete.executeUpdate();
            }catch (SQLException e){
                System.out.print(e);
            }
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
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO CERBYTES.\"database_entries\" " +
                "(\"username\", \"description\", \"url_content\", \"password_text\", \"date_creation\", \"date_update\")" +
                " VALUES (?,?,?,?,?,?)");
        ps.setString(1, databaseEntry.getUsername());
        ps.setString(2, databaseEntry.getDescription());
        ps.setString(3, databaseEntry.getUrl());
        ps.setString(4, databaseEntry.getPassword());
        ps.setString(5, databaseEntry.getCreationDate());
        ps.setString(6, databaseEntry.setLastUpdate().toString());

        int i = ps.executeUpdate();
        connection.close();

    }


    public DatabaseEntry createSimple(String username, String password, String description, String url) {
        return new DatabaseEntry(username, description, url, password);
    }
}



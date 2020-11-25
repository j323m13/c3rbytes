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
        Connection con = DBConnection.dbConnect();
        PreparedStatement ps = con.prepareStatement(getAll);
        ResultSet rs = null;
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();
        databaseEntries = getEntries(rs);
        try{
            rs = ps.executeQuery();

            while(rs.next()) {
                DatabaseEntry entry = new DatabaseEntry();
                entry.setId(rs.getString("user_id"));
                entry.setUsername(rs.getString("username"));
                entry.setDescription(rs.getString("description"));
                entry.setPassword(rs.getString("password_text"));
                entry.setUrl(rs.getString("url_content"));
                entry.setCreationDate(rs.getString("date_creation"));
                entry.setLastUpdate(rs.getString("date_update"));
                //entry.getNote(rs.getString("notes"));
                databaseEntries.addAll(entry);
            }

            //Print results in terminal for debugging
            System.out.println(rs.getInt(1) + "," +
                    rs.getString(2) + ", " + rs.getString(3) + ", " +
                    rs.getString(4) + ", " + rs.getString(5) + ", " +
                    rs.getString(6) + ", " +
                    rs.getString(7)
            );
        }catch (SQLException e){
            System.out.println("Table is empty? "+e);
        }finally {
            ps.close();
            rs.close();
            DBConnection.dbDisconnect();
        }

        return databaseEntries;

    }



    private static ObservableList<DatabaseEntry> getEntries (ResultSet rs) throws SQLException {
        ObservableList<DatabaseEntry> databaseEntries = FXCollections.observableArrayList();

        if(rs.next()) {
            DatabaseEntry entry = new DatabaseEntry();
            entry.setId(rs.getString("user_id"));
            entry.setUsername(rs.getString("username"));
            entry.setPassword(rs.getString("password_text"));
            entry.setUrl(rs.getString("url"));
            entry.setCreationDate(rs.getString("date_creation"));
            entry.setLastUpdate(rs.getString("date_update"));
            //entry.getNote(rs.getString("notes"));
            databaseEntries.addAll(entry);
        }
        return databaseEntries;

    }


    @Override
    public Dao getEntryById(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Dao save(DatabaseEntry entry) throws SQLException, ClassNotFoundException {

        String saveStmt =
                        "INSERT INTO database_entries\n" +
                        "(user_id, username, description, url_content, password_text, date_creation, date_update )\n" +
                        "VALUES\n" +
                        "('"+entry.getUsername()+"','"+entry.getDescription()+"','"+entry.getUrl()+"','"+entry.getPassword()+
                        "','"+entry.getCreationDate()+"','"+entry.getLastUpdate()+"')";


        //Execute DELETE operation
        try {
            DBConnection.dbExecuteUpdate(saveStmt);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
        return null;
    }


    @Override
    public Dao update(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
/*
        if(entry !=null){
            DBConnection helper = new DBConnection();
            //Connection connection = helper.getConnection(helper.getUrlWithParameters());
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
         */
        return null;

    }






    @Override
    public Dao delete(DatabaseEntry entry) throws SQLException, ClassNotFoundException {
        if(entry !=null){
            DBConnection helper = new DBConnection();
            //Connection connection = helper.getConnection(helper.getUrlWithParameters());
            PreparedStatement delete = null;

            //delete = connection.prepareStatement("DELETE FROM \"CERBYTES\".\"database_entries\" WHERE \"user_id\"="+entry.getId()+"");
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



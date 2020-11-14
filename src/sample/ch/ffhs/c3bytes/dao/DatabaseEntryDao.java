package sample.ch.ffhs.c3bytes.dao;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseEntryDao implements Dao{



    public static ArrayList<ResultSet> getAll() throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CERBYTES.\"user\"");
        ResultSet rs = ps.executeQuery();
        //TODO implement List as DataEntry Type
        ArrayList<ResultSet> list = new ArrayList<>() {
        };
        while(rs.next()){
           list.add(rs);
            System.out.println(rs.getInt(1)+","+
            rs.getString(2)+", "+rs.getString(3)+", "+
                    rs.getString(4)+", "+rs.getString(5)+", "+rs.getString(6)+", "+
                    rs.getString(7)
            );

        }
        //contain all the entries from the database CERBYTES (user only)
        //TODO implement join statement
        return list;


    }



    @Override
    public Dao getEntryById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getInstance();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id="+id+"");
        while(rs.next()){
            return (Dao) extractEntryFromResultSet(rs);
        }
        return null;
    }
    /* methode to extract the values of a row (or multiples rows) from a query and save the values in a DataEntry Object.
     *@param rs take a Resultset object as paramenter and save the values inside a DatabaseEntry object
     * @return entry a DatabaseEntry Object which is a row in the Dababase
     */
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

    /*
     * Insert a DatabaseEntry Object into the Database
     *
     */
    public void insertDatabaseEntry() throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
        PreparedStatement psForPassword = connection.prepareStatement(("INSERT INTO CERBYTES.\"password\"(\"password_text\") values (?)"));
        psForPassword.setString(1,DatabaseEntry.getPassword());
        psForPassword.executeUpdate();

        PreparedStatement psPasswordForeignKey = connection.prepareStatement(("(SELECT MAX(\"password_id\") FROM CERBYTES.\"password\")"));
        ResultSet passwordReference = psPasswordForeignKey.executeQuery();
        int passwordReferenceTmp = 0;
        if(passwordReference.next()){
            passwordReferenceTmp = passwordReference.getInt(1);
        }

        PreparedStatement ps = connection.prepareStatement("INSERT INTO CERBYTES.\"user\" " +
                "(\"username\", \"description\", \"url_content\", \"password_id\", \"date_creation\", \"date_update\")" +
                " VALUES (?,?,?,?,?,?)");
        ps.setString(1, DatabaseEntry.getUsername());
        ps.setString(2,DatabaseEntry.getDescription());
        ps.setString(3,DatabaseEntry.getUrl());
        ps.setInt(4,passwordReferenceTmp);
        ps.setString(5,DatabaseEntry.getCreationDate());
        ps.setString(6,DatabaseEntry.getLastUpdate());

        int i = ps.executeUpdate();
    }

    }
    //boolean updateDatabaseEntry();
    //boolean deleteDatabaseEntry();


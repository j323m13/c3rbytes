package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnection {
    public static boolean firstBoot = true;
    public static String userDB = "cerbytes";
    public static String passwordDB = "tH94mLBaKr";
    //public static String passwordDB = "123456789";
    //public static String passwordDB;
    private String oldBootPassword;
    //public static String oldBootPassword = "f235c129089233ce3c9c85f1";
    private String newBootPassword;
    public static String bootPassword;
    public boolean newBootPasswordEnabled = false;
    private static int encryptionKeyLength = 256;
    private static String encryptionAlgorithm = "AES/CBC/NoPadding";
    public static String databaseName = "cerbytesdb";
    private static Boolean databaseEncryption = true;
    //public static String JDBC_URL = "jdbc:derby:cerbytesdb;create=true;username=cerbytes;password=tH94mLBaKr;"+
    //       "dataEncryption=true;encryptionKeyLength=192;encryptionAlgorithm=AES/CBC/NoPadding";
    public static String JDBC_URL;
    public static Connection connection = null;
    private boolean connectionOpen;
    private boolean connectionClose;
    //public final DBConnection helper = new DBConnection();





    public static void dbConnect() throws SQLException {
        try {
            System.out.println("Connecting to db ... ");
            //System.out.println("url inside dbconnect(): " + createURL());
            connection = DriverManager.getConnection(createURL());
            System.out.println("connection successful");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }

    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                //System.out.println("url inside dbDisconnect(): " + createURL());
                System.out.println("shutdown? " + createURL() + ";shutdown=true");
                connection = DriverManager.getConnection(createURL() + ";shutdown=true");
                System.out.println("disconnect() successful");
                connection.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * this methode holds all the entries from the database and return them to an ObservableList. For insertion, use dbExecuteUpdate()
     * @param getAll (String)
     * @param databaseEntries (ObservableList)
     * @see dbExecuteUpdate()
     */
    public static ObservableList<DatabaseEntry> dbExecuteQuery(String getAll, ObservableList<DatabaseEntry> databaseEntries) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        dbConnect();
        //databaseEntries = getEntries(rs);
        try (PreparedStatement ps = connection.prepareStatement(getAll); ResultSet rs = ps.executeQuery()) {
            int i = 1;
            while (rs.next()) {
                DatabaseEntry entry = new DatabaseEntry();
                entry.setDummytId(String.valueOf(i));
                entry.setId(rs.getString("user_id"));
                entry.setUsername(rs.getString("username"));
                entry.setDescription(rs.getString("description"));
                entry.setPassword(rs.getString("password_text"));
                entry.setUrl(rs.getString("url_content"));
                entry.setCreationDate(rs.getString("date_creation"));
                entry.setLastUpdate(rs.getString("date_update"));
                entry.setNote(rs.getString("note"));
                databaseEntries.addAll(entry);
                i++;
                //Print results in terminal for debugging
                System.out.println(rs.getInt(1) + "," +
                        rs.getString(2) + ", " + rs.getString(3) + ", " +
                        rs.getString(4) + ", " + rs.getString(5) + ", " +
                        rs.getString(6) + ", " +
                        rs.getString(7) + ", " + rs.getString(8)
                );
            }
            ps.close();

        } catch (SQLException e) {
            System.out.println("Table is empty? " + e);
        } finally {
            //dbDisconnect();
        }

        return databaseEntries;
    }


    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        System.out.println("query " + sqlStmt);
        try {
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            //dbDisconnect();
            connection.close();
        }
    }


    //NOT WORKING YET -> DO NOT TOUCH
    public static void setupUserDBWithPasswordConnection(String urlx, String passwordDB, String setupPasswordString) throws SQLException {

        System.out.println(urlx);
        Connection con = DriverManager.getConnection(urlx);
        System.out.println("Connect success -> " + urlx);
        DBConnection.passwordDB = passwordDB;
        System.out.println("DBConnection.password: " + DBConnection.passwordDB);

        CallableStatement cs = con.prepareCall(setupPasswordString);
        cs.setString(1, DBConnection.userDB);
        cs.setString(2, DBConnection.passwordDB);
        cs.execute();
        cs.close();
        try {
            System.out.println("Connect -> " + urlx + ";password=" + passwordDB + "");
            con = DriverManager.getConnection(urlx + ";password=" + passwordDB + "");
            System.out.println("success with password");
        } catch (SQLException e) {
            System.out.println(e);
        }
        try {
            System.out.println("disconnect");
            con.close();
            //con = DriverManager.getConnection(urlx+";password="+ passwordDB+";shutdown=true");
            System.out.println("success disconnect with password");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void resetUserPwd(String reset, String newUserDBPassword) {
        try {
            connection = DriverManager.getConnection(createURL());
            System.out.println("Connect success -> " + createURL());
            CallableStatement cs = connection.prepareCall(reset);
            cs.setString(1, DBConnection.userDB);
            cs.setString(2, newUserDBPassword);
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void setupDBEncryption() {
        Connection connection = null;
        try {
            dbConnect();
            System.out.println("Database is almost encrypted");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            //connection = DriverManager.getConnection(createURL()+";shutdown=true");
            if (connection != null) {
                connection.close();
            }
            System.out.println("DB ist encrypted with: ");
            System.out.println("bootPassword:  " + DBConnection.bootPassword);
            System.out.println("passwordDB: " + DBConnection.passwordDB);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void setupDatabase() throws SQLException, ClassNotFoundException {
        //encrypt Database with bootPassword
        connection = DriverManager.getConnection(createURLSimple() + ";password=" + passwordDB + "");
        //set schema
        String slqCreateSchema = "CREATE SCHEMA \"CERBYTES\"";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(slqCreateSchema);
            System.out.println("Schema was created.");
        } catch (SQLException e) {
            System.out.println("Schema was not created." + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
        }

        String sqlCreateTable = "CREATE TABLE \"CERBYTES\".\"database_entries\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(50) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(50) DEFAULT NULL,\n" +
                "                       \"note\" CLOB(2K) DEFAULT NULL)";

        Statement stmt2 = null;
        try {
            stmt2 = connection.createStatement();
            stmt2.executeUpdate(sqlCreateTable);
            System.out.println("table was created.");
        } catch (SQLException e) {
            System.out.println("table was not created." + e);
            throw e;
        } finally {
            if (stmt2 != null) {
                //Close statement
                stmt2.close();
            }
            System.out.println("Table was created");

            //disconnect to encrypt the database (without reboot, change will be overturned.
            connection.close();

        }
    }


    private static String createURL() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";user=" + userDB + ";password=" + passwordDB + ";databaseEncryption=" + databaseEncryption + "" +
                ";encryptionKeyLength=" + encryptionKeyLength + ";encryptionAlgorithm=" + encryptionAlgorithm + ";bootPassword=" + bootPassword + "";
        System.out.println("createURL() -> " + JDBC_URL);
        return JDBC_URL;
    }


    private static String createURLSimple() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";create=true;user=cerbytes;password=" + passwordDB + "";
        System.out.println("createURL() -> " + JDBC_URL);
        return JDBC_URL;
    }



}

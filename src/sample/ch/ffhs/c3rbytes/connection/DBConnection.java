package sample.ch.ffhs.c3rbytes.connection;

import javafx.collections.ObservableList;
import sample.ch.ffhs.c3rbytes.DatabaseEntry.DatabaseEntry;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class DBConnection {
    public static String userDB = "cerbytes";
    public static String passwordDB;
    public static String bootPassword;
    public boolean newBootPasswordEnabled = false;
    private static int encryptionKeyLength = 256;
    private static String encryptionAlgorithm = "AES/CBC/NoPadding";
    public static String databaseName = "db/cerbytes";
    private static Boolean databaseEncryption = true;
    private static boolean createDB = true;
    public static String JDBC_URL;
    public static Connection connection = null;


    public static void dbConnect(String JDBC_URL) throws SQLException {
        try {
            System.out.println("Connecting to db ... ");
            //System.out.println("url inside dbconnect(): " + createURL());
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("connection successful");
            getConnectionInstance();

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

                System.out.println("disconnect ->");
                connection.close();
                System.out.println("disconnect() successful");
                //connection.close();
                getConnectionInstance();
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
    public static ObservableList<DatabaseEntry> dbExecuteQuery(String getAll, ObservableList<DatabaseEntry> databaseEntries, String JDBC_URL) throws SQLException, ClassNotFoundException, InterruptedException {
        //Declare statement, resultSet and CachedResultSet as null
        dbConnect(JDBC_URL);
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
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }

        } catch (SQLException e) {
            System.out.println("Table is empty? " + e);
        }
        TimeUnit.SECONDS.sleep(1);
        dbDisconnect();

        return databaseEntries;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static boolean dbExecuteUpdate(String sqlStmt, String JDBC_URL) throws SQLException, ClassNotFoundException, InterruptedException {
        //Declare statement as null
        Statement stmt = null;
        System.out.println("query " + sqlStmt);
        dbConnect(JDBC_URL);
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException ex){
            if(ex.getSQLState().equals("X0Y32")){
                System.out.println("Table is empty");
            }
            return false;

        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }

        }
        //Close connection
        dbDisconnect();
        return true;
    }


    /*
     * Set a password value to the userDB for security and queries.
     * @param url to connect to the database
     * @param passwordDB: the password to be set
     * @param setupPasswordString: a sql statement to be executed.
     */
    public static void setupUserDBWithPasswordConnection(String JDBC_URL, String passwordDB, String setupPasswordString) throws SQLException {

        System.out.println("entering setup");
        System.out.println(JDBC_URL);
        dbConnect(JDBC_URL);
        System.out.println("Connect success -> " + JDBC_URL);

        System.out.println("DBConnection.password: " + DBConnection.passwordDB);

        CallableStatement cs = connection.prepareCall(setupPasswordString);
        try {
            cs.setString(1, DBConnection.userDB);
            cs.setString(2, passwordDB);
            cs.execute();
            System.out.println("Success: passwordDB has been set. " + userDB + ";" + DBConnection.passwordDB);
            DBConnection.passwordDB = passwordDB;
        } catch (SQLException e) {
            System.out.println("PasswordDB has not been set. " + e);
        }
        cs.close();
        System.out.println("JDBC_URL = " + createURLSimple());
        dbDisconnect();

        System.out.println("Connect -> " + createURLSimple() + ";password=" + DBConnection.passwordDB + "");
        dbConnect(createURL() + ";password=" + DBConnection.passwordDB + "");
        System.out.println("success with password");

        dbDisconnect();

    }

    public static void resetUserPwd(String reset, String newUserDBPassword) {
        try {
            dbConnect(createURLSimple());
            System.out.println("Connect success -> " + createURLSimple());
            CallableStatement cs = connection.prepareCall(reset);
            cs.setString(1, DBConnection.userDB);
            cs.setString(2, newUserDBPassword);
            cs.execute();
            cs.close();
            DBConnection.passwordDB = newUserDBPassword;
            System.out.println("new password: " + passwordDB);
            System.out.println(createURLSimple());
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public static String createURL() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";create=" + createDB + ";user=" + userDB + ";password=" + passwordDB + ";dataEncryption=" + databaseEncryption + "" +
                ";encryptionKeyLength=" + encryptionKeyLength + ";encryptionAlgorithm=" + encryptionAlgorithm + ";bootPassword=" + bootPassword + "";
        System.out.println("createURL() -> " + JDBC_URL);
        return JDBC_URL;
    }


    public static String createURLSimple() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";create=true;user=" + userDB + ";password=" + passwordDB + "";
        System.out.println("createURLSimple() -> " + JDBC_URL);
        return JDBC_URL;
    }

    public static void getConnectionInstance() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            System.out.println("connection is open");
        } else {
            System.out.println("connection is closed");
        }
    }


    public static void shutdownDB() throws SQLException {
        try {
            getConnectionInstance();
            connection = DriverManager.getConnection(createURLSimple() + ";shutdown=true");
            getConnectionInstance();
        } catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("DB " + databaseName + "has shutdown");
            }
        }
    }
}


package sample.ch.ffhs.c3rbytes.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class DBConnection {
    public static boolean onNewStartup = false;
    public static String userDB = "cerbytes";
    public static String passwordDB;
    //public static String passwordDB;
    //public static String passwordDB;
    private String oldBootPassword;
    //public static String oldBootPassword = "f235c129089233ce3c9c85f1";
    private String newBootPassword;
    public static String bootPassword;
    public boolean newBootPasswordEnabled = false;
    private static int encryptionKeyLength = 256;
    private static String encryptionAlgorithm = "AES/CBC/NoPadding";
    public static String databaseName = "cerbytes";
    private static Boolean databaseEncryption = true;
    private static boolean createDB = true;
    //public static String JDBC_URL = "jdbc:derby:cerbytesdb;create=true;username=cerbytes;password=tH94mLBaKr;"+
    //       "dataEncryption=true;encryptionKeyLength=192;encryptionAlgorithm=AES/CBC/NoPadding";
    public static String JDBC_URL="jdbc:derby:";
    public static Connection connection = null;
    private boolean connectionOpen;
    private boolean connectionClose;
    //public final DBConnection helper = new DBConnection();





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
            if(rs != null){
                rs.close();
            }
            if(ps != null){
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
    public static void dbExecuteUpdate(String sqlStmt, String JDBC_URL) throws SQLException, ClassNotFoundException, InterruptedException {
        //Declare statement as null
        Statement stmt = null;
        System.out.println("query " + sqlStmt);
        dbConnect(JDBC_URL);
        try {
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

        }
        //Close connection
        TimeUnit.SECONDS.sleep(1);
        dbDisconnect();
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
        DBConnection.passwordDB = passwordDB;
        System.out.println("DBConnection.password: " + DBConnection.passwordDB);

        CallableStatement cs = connection.prepareCall(setupPasswordString);
        try{
            cs.setString(1, DBConnection.userDB);
            cs.setString(2, DBConnection.passwordDB);
            cs.execute();
            System.out.println("Success: passwordDB has been set. "+userDB+";"+DBConnection.passwordDB);
    }catch (SQLException e){
            System.out.println("PasswordDB has not been set. "+e);
        }
        cs.close();
        System.out.println("JDBC_URL = "+JDBC_URL);
        dbDisconnect();

        System.out.println("Connect -> " + JDBC_URL + ";password=" + DBConnection.passwordDB + "");
        dbConnect(JDBC_URL + ";password=" + DBConnection.passwordDB +"" );
        System.out.println("success with password");

        dbDisconnect();
        System.out.println("disconnection successful");
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

    public static void setupDBEncryption(String encryptionKey) throws SQLException {
        //TODO: to try
        /*
        CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(
    'bootPasword', 'oldbpw , newbpw');
         */
        DBConnection.bootPassword = encryptionKey;
        dbConnect(createURLSimple()+";bootPassword="+bootPassword);
        System.out.println("Database is almost encrypted");
        dbDisconnect();
        /*
        try{
            System.out.println("DB shutdown -> jdbc:derby:;user="+userDB+";password="+passwordDB+";bootPassword="+bootPassword+";shutdown=true");
            DriverManager.getConnection("jdbc:derby:;user="+userDB+";password="+passwordDB+";bootPassword="+bootPassword+";shutdown=true");
        }catch (SQLException e){
            System.out.println("nope, failed");
        }
         */
        System.out.println("DB ist encrypted with: ");
        System.out.println("bootPassword:  " + DBConnection.bootPassword);
        System.out.println("passwordDB: " + DBConnection.passwordDB);
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
                "                        \"url_content\" VARCHAR(500) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(500) DEFAULT NULL,\n" +
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


            //disconnect to encrypt the database (without reboot, change will be overturned.
            connection.close();

        }
    }


    public static String createURL() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";create="+createDB+";user=" + userDB + ";password=" + passwordDB + ";dataEncryption=" + databaseEncryption + "" +
                ";encryptionKeyLength=" + encryptionKeyLength + ";encryptionAlgorithm=" + encryptionAlgorithm + ";bootPassword=" + bootPassword + "";
        System.out.println("createURL() -> " + JDBC_URL);
        return JDBC_URL;
    }


    public static String createURLSimple() {
        JDBC_URL = "jdbc:derby:" + databaseName + ";create=true;user="+userDB;
        System.out.println("createURLSimple() -> " + JDBC_URL);
        return JDBC_URL;
    }

    public static void getConnectionInstance() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            System.out.println("connection is open");
        }else {
            System.out.println("connection is closed");
        }
    }


    public static void shutdownDB() throws SQLException {
        try{
            getConnectionInstance();
            Connection shutdown = DriverManager.getConnection(createURL()+";shutdown=true");
        }catch (SQLException e){
            System.out.println("db shutdown: "+e);
        }
    }
}

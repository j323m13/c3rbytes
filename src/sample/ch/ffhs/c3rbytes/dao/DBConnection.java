package sample.ch.ffhs.c3rbytes.dao;

import java.sql.*;

public class DBConnection {
    private String userDB = "cerbytes";
    private String passwordDB = "tH94mLBaKr";
    private String oldBootPassword;
    //public static String oldBootPassword = "f235c129089233ce3c9c85f1";
    private String newBootPassword;
    public static String bootPassword;
    private boolean newBootPasswordEnabled = false;
    private int encryptionKeyLength = 192;
    private String encryptionAlgorithm = "AES/CBC/NoPadding";
    private String databaseName = "cerbytesdb";
    private Boolean databaseEncryption = true;
    //public static String JDBC_URL = "jdbc:derby:cerbytesdb;create=true;username=cerbytes;password=tH94mLBaKr;"+
    //       "dataEncryption=true;encryptionKeyLength=192;encryptionAlgorithm=AES/CBC/NoPadding";
    public static String JDBC_URL = "jdbc:derby:cerbytesdbTEST;create=true";
    public static Connection connection = null;
    private boolean connectionOpen;
    private boolean connectionClose;
    //public final DBConnection helper = new DBConnection();


    public static Connection dbConnect() throws SQLException {
        try {
            System.out.println("url inside dbconnect(): "+JDBC_URL);
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("connection successful");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    //query
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            System.out.println("Select statement: " + queryStmt + "\n");

            //Create statement
            stmt = connection.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return resultSet
        return resultSet;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            DBConnection.dbConnect();
            //Create Statement
            stmt = connection.createStatement();
            //Run executeUpdate operation with given sql statement
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
            dbDisconnect();
        }
    }

    /*
     * Methode to change the bootpassword to encrypt the DB
     * jdbc:derby:salesdb;bootPassword=abc1234xyz;newBootPassword=new1234xyz
     * @param the newBootPassword
     */
  /*¨
    public static void changebootPasswordAndEncryptDBWithNewBootPassword(String oldPassword, String newMasterpassword) throws SQLException, ClassNotFoundException {
        DBConnection.passwordDB = oldPassword;

        String connectionString = createUrlWithParamenters();

        System.out.println(connectionString);

        Connection conn = DBConnection.getConnection();
        CallableStatement cs = conn.prepareCall("CALL SYSCS_UTIL.SYSCS_RESET_PASSWORD(?, ?)");
        cs.setString(1, "cerbytes");
        cs.setString(2, newMasterpassword);
        cs.execute();
        cs.close();
        conn.close();
        //return connection;
    }
   */
    /*


    public static void changeBootPassword(String oldBootMasterPassword, String newBootMasterpassword ) throws SQLException, ClassNotFoundException {

        DBConnection helper = new DBConnection();
        helper.setBootPassword(oldBootMasterPassword);
        helper.setNewBootPassword(newBootMasterpassword);


        //connect to the db with old bootpassword and apply newBootPassword
        //jdbc:derby:cerbytesdb;user=cerbytes;password=tH94mLBaKr;dataEncryption=true;encryptionKeyLength=192;encryptionAlgorithm=AES/CBC/NoPadding;
        // bootPassword=654321654321Access to DB granted
        String url = helper.getURL();
        System.out.println("url for changing bootPassword");
        Connection connection = DriverManager.getConnection(helper.getURL()+";newBootPassword="+newBootMasterpassword);
        System.out.println(helper.setURL()+";newBootPassword="+newBootMasterpassword);
        helper.setBootPassword(newBootMasterpassword);
        //shutdown the DB to apply newbootPassword
        connection.close();
        System.out.println("new url after bootpassword change: ");
        System.out.println(helper.createUrlWithParamenters());
        //try the connection with the new bootPassword
        try{
            Connection connection2 = DriverManager.getConnection(helper.createUrlWithParamenters());
            connection2.close();
            System.out.println("connection is close. bootPassword was sucessfully changed");
        }catch (SQLException e){
            System.out.println("the connection with new bootpassword does not work.");
        }

    }
    */


    //TODO to implement a setup routine
    /*
    * Create a user (userDB), a table in CERBYTES, set userDB password and encrypt the database with the masterpassword.
     */
    /*
    public void setup() throws SQLException {
        DBConnection helper = new DBConnection();
        connection = DriverManager.getConnection(helper.getUrlWithParameters());
        //create a simple url with a username (necessary). table belongs to this username afterwards.
        System.out.println(createUrl());
        //create a password for userdb
        CallableStatement setup = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_CREATE_USER(?, ?)");
        setup.setString(1, helper.userDB);
        setup.setString(2, helper.passwordDB);
        setup.executeQuery();
        setup = null;
        // restart db
        connection.close();
        Connection connection = DriverManager.getConnection(createUrl());
        // create table data_entries
        String sqlCreate = "CREATE TABLE CERBYTES.\"data_entries\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(20) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(20) DEFAULT NULL,\n" +
                "                       \"note\" CLOB(2K) DEFAULT NULL)";
        try {
            setup.executeQuery(sqlCreate);
        }catch (SQLException e){
            System.out.println(e);
        }
        connection.close();

        // create url with all the parameters and encryption
        System.out.println(createUrlWithParamenters());
        try{
            Connection secure = DriverManager.getConnection(createUrlWithParamenters());
            secure.close();

        }catch (SQLException e){
            System.out.println(e);
        }

    }
    */




}

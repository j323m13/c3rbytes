package sample.ch.ffhs.c3rbytes.dao;

import java.sql.*;

public class DBConnection {
    private static final String userDB = "cerbytes";
    public static String passwordDB;
    //public static String passwordDB = "tH94mLBaKr";
    public static String oldBootPassword = "c12345";
    //public static String oldBootPassword = "f235c129089233ce3c9c85f1";
    public static String newBootPassword;
    public boolean newBootPasswordEnabled = false;
    private static final int encryptionKeyLength = 192;
    private static final String encryptionAlgorithm = "AES/CBC/NoPadding";
    private static final String databaseName = "cerbytesdb";
    private static final Boolean databaseEncryption = true;
    private static String JDBC_URL;
    private static Connection connection;



    /*
     * create the url for the database (embedded version)
     * @param jdbd = driver
     * @param derby = database type
     * @param databasename
     * @create create database if not exist;
     *
     */
    private static String createUrl(){
        return JDBC_URL = "jdbc:derby:dbFactory;create=true";

    }

    /*
     * create the url for the database (embedded version)
     * @param databasename
     * @param createDabaseIfNotExist -> create database if not exist;
     *
     */
    public static String createUrlWithParamenters(){
        JDBC_URL = "jdbc:derby:"+databaseName +
                ";user="+ userDB+
                ";password="+passwordDB+
                ";dataEncryption="+databaseEncryption+
                ";encryptionKeyLength="+encryptionKeyLength+
                ";encryptionAlgorithm="+encryptionAlgorithm+
                ";bootPassword="+ oldBootPassword +"";
        return JDBC_URL;
    }

    /*
     * Methode to change the bootpassword to encrypt the DB
     * jdbc:derby:salesdb;bootPassword=abc1234xyz;newBootPassword=new1234xyz
     * @param the newBootPassword
     */
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

    public static void changeBootPassword(String oldBootMasterPassword, String newBootMasterpassword ) throws SQLException, ClassNotFoundException {
        DBConnection.oldBootPassword = oldBootMasterPassword;
        DBConnection.newBootPassword = newBootMasterpassword;

        //DBConnection.close();
        //String url = getConnection()+";newBootPassword="+newBootMasterpassword;
        String url = "jdbc:derby:"+databaseName+";user="+ userDB+";password="+passwordDB+";bootPassword="+oldBootMasterPassword+";newBootPassword="+newBootMasterpassword;
        System.out.println(url);
        DBConnection.oldBootPassword = newBootMasterpassword;
        connection = DriverManager.getConnection(url);
        DBConnection.close();
        connection = getConnection();
        //DBConnection.close();
        //DriverManager.getConnection("jdbc:derby:"+databaseName+";shutdown=true");

        /*

        CallableStatement cs = conn.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?, ?)");
        cs.setString(1,"bootPassword");
        cs.setString(2, oldBootMasterPassword);
        cs.setString(3, newBootMasterpassword);
        cs.execute();
        cs.close();
        conn.close();

/*
        DBConnection.JDBC_URL = createUrlWithParamenters() + ";newBootPassword="+newBootMasterpassword;
        DBConnection.getConnection();
*/

    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        createUrlWithParamenters();
         return DriverManager.getConnection(JDBC_URL);
    }

    public Connection getInstance() throws SQLException, ClassNotFoundException {
        if(connection != null){
            return connection;
        } else {
            connection = DBConnection.getConnection();
        }
        return connection;
    }

    public static Connection close() throws SQLException, ClassNotFoundException{
        connection.close();
        try{
             DriverManager.getConnection(createUrlWithParamenters()+"shutdown=true");
        }catch (Exception e){
            System.out.print(e);
        }
        return connection;
    }

    //TODO to implement a setup routine
    public void setup() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL);
        String sqlCreate = "CREATE TABLE CERBYTES.\"user\" (\n" +
                "                        \"user_id\" INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),\n" +
                "                        \"username\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"description\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"url_content\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"password_text\" VARCHAR(255) DEFAULT NULL,\n" +
                "                        \"date_creation\" VARCHAR(20) DEFAULT NULL,\n" +
                "                        \"date_update\" VARCHAR(20) DEFAULT NULL\n)";
        //dbAccess.update(connection, sqlCreate);
    }

    /*
    * for testing purpose
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
    }
     */



}

package sample.ch.ffhs.c3bytes.dao;

import java.sql.*;
import java.util.Objects;

public class connectionFactory {
    private static final String userDB = "cerbytes";
    private static final String passwordDB = "1234";
    private static final String bootPassword = "12345secureAF";
    private static final int encryptionKeyLength = 192;
    private static final String encryptionAlgorithm = "AES/CBC/NoPadding";
    private static final String databaseName = "dbEncryptedAES";
    private static final Boolean databaseEncryption = true;
    private static final Boolean createDabaseIfNotExist = true;
    private static String JDBC_URL;
    public Statement statement = null;
    public static Connection connection = null;



    /*
     * create the url for the database (embedded version)
     * @param jdbd = driver
     * @param derby = database type
     * @param databasename
     * @create create database if not exist;
     *
     */
    private static String createUrl(){
        return JDBC_URL = "jdbc:derby:dbFactory;create=true;user=cerbytes;password=1234";

    }

    private  static String urlShutDownDatabse(){
        return JDBC_URL = "jdbc:derby:dbFactory;create=true;user=cerbytes;password=1234;shutdown=true";
    }

    /*
     * create the url for the database (embedded version)
     * @param databasename
     * @param createDabaseIfNotExist -> create database if not exist;
     *
     */
    private String createUrlWithParamenters(String bootPassword, Boolean databaseEncryption, int encryptionKeyLength, String encryptionAlgorithm, String databaseName,
                                            Boolean createDabaseIfNotExist, String userDB, String passwordDB){
        JDBC_URL = "jdbc:derby:"+databaseName+";create="+createDabaseIfNotExist+";user="+
                userDB+";password="+passwordDB+";dataEncryption="+databaseEncryption+";encryptionKeyLength="+encryptionKeyLength+";encryptionAlgorithm="+encryptionAlgorithm+";bootPassword="+bootPassword+"";
        return JDBC_URL;
    }


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        createUrl();
         return DriverManager.getConnection(JDBC_URL);
    }

    public static Connection getInstance() throws SQLException, ClassNotFoundException {
        if(connection != null){
            return connection;
        } else {
            connection = connectionFactory.getConnection();
        }
        return connection;
    }

    public Connection close() throws SQLException, ClassNotFoundException{
        return DriverManager.getConnection(Objects.requireNonNull(urlShutDownDatabse()));
    }

    /*
    * for testing purpose
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
    }
     */



}

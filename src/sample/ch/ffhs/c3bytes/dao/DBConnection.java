package sample.ch.ffhs.c3bytes.dao;

import org.apache.commons.dbutils.QueryRunner;

import javax.management.Query;
import java.sql.*;
import java.util.Objects;

public class DBConnection {
    private static final String userDB = "cerbytes";
    private static final String passwordDB = "1234";
    private static final String bootPassword = "12345secureAF";
    private static final int encryptionKeyLength = 192;
    private static final String encryptionAlgorithm = "AES/CBC/NoPadding";
    private static final String databaseName = "dbEncryptedAES";
    private static final Boolean databaseEncryption = true;
    private static final Boolean createDabaseIfNotExist = true;
    private static String JDBC_URL;
    private Connection connection;



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

    /*
     * create the url for the database (embedded version)
     * @param databasename
     * @param createDabaseIfNotExist -> create database if not exist;
     *
     */
    private String createUrlWithParamenters(){
        JDBC_URL = "jdbc:derby:"+databaseName+";create="+createDabaseIfNotExist+";user="+
                userDB+";password="+passwordDB+";dataEncryption="+databaseEncryption+";encryptionKeyLength="+encryptionKeyLength+";encryptionAlgorithm="+encryptionAlgorithm+";bootPassword="+bootPassword+"";
        return JDBC_URL;
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


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        createUrl();
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

    public Connection close() throws SQLException, ClassNotFoundException{
        connection.close();
        try{
             DriverManager.getConnection(JDBC_URL+"shutdown=true");
        }catch (Exception e){
            System.out.print(e);
        }
        return connection;
    }

    /*
    * for testing purpose
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = connectionFactory.getConnection();
    }
     */



}

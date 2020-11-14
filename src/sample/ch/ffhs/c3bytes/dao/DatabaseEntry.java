package sample.ch.ffhs.c3bytes.dao;

import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class DatabaseEntry{

    private static int id;
    private static String username;
    private static String description;
    private static String url;
    private static String password;
    private static String creationDate;
    private static String lastUpdate;
    private HashSet dataEntries;

    public DatabaseEntry() {

    }

    public DatabaseEntry(int id, String username, String description, String url, String password){
        DatabaseEntry.id = id;
        DatabaseEntry.username = username;
        DatabaseEntry.description = description;
        DatabaseEntry.url = url;
        DatabaseEntry.password = password;
        DatabaseEntry.creationDate = getDateTime();
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = DatabaseEntry.creationDate;

    }

    public DatabaseEntry(String username, String description, String url, String password){
        DatabaseEntry.username = username;
        DatabaseEntry.description = description;
        DatabaseEntry.url = url;
        DatabaseEntry.password = password;
        DatabaseEntry.creationDate = getDateTime();
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = DatabaseEntry.creationDate;

    }

    public static void setId(int id) {
        DatabaseEntry.id = id;
    }

    public static int getId() {
        return id;
    }

    public static String getIdAsString(){
        return String.valueOf(id);
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DatabaseEntry.username = username;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        DatabaseEntry.description = description;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DatabaseEntry.url = url;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DatabaseEntry.password = password;
    }

    public static String getCreationDate() {
        return creationDate;
    }

    public static void setCreationDate(String creationDate) {
        DatabaseEntry.creationDate = creationDate;
    }

    public static String getLastUpdate() {
        return lastUpdate;
    }

    public static void setLastUpdate(String lastUpdate) {
        DatabaseEntry.lastUpdate = lastUpdate;
    }

    public String getDateTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        //System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //System.out.println("After formatting: " + formattedDate);
        return myDateObj.format(myFormatObj);
    }


}

package sample.ch.ffhs.c3bytes.dao;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DatabaseEntry{

    private static int id;
    private static String username;
    private static String description;
    private static String url;
    private static String password;
    private static String creationDate;
    private static String lastUpdate;



    public DatabaseEntry() {
        //creation of a DatabaseEntry without paramenters.
        //DatabaseEntry.id = 0;
        DatabaseEntry.username = null;
        DatabaseEntry.description = null;
        DatabaseEntry.url = null;
        DatabaseEntry.password = null;
        DatabaseEntry.creationDate = new String(getDateTime());
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = DatabaseEntry.getCreationDate();

    }

    /*
    * Constructor for an DatabaseEntry Object which contains an id
    * * @param id the id from the database
     * @param username the username
     * @param description what kind of entry it is (social media account, etc.)
     * @param url
     * @param password password is protected (hashed)
     */
    public DatabaseEntry(int id, String username, String description, String url, String password){
        DatabaseEntry.id = id;
        DatabaseEntry.username = username;
        DatabaseEntry.description = description;
        DatabaseEntry.url = url;
        DatabaseEntry.password = password;
        DatabaseEntry.creationDate = getDateTime();
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = DatabaseEntry.getCreationDate();


    }
    /*
    *Constructor for an DatabaseEntry Object which contains an id
    * @param username the username
    * @param description what kind of entry it is (social media account, etc.)
    * @param url
    * @param password password is protected (hashed)
     */
    public DatabaseEntry(String username, String description, String url, String password){
        DatabaseEntry.username = username;
        DatabaseEntry.description = description;
        DatabaseEntry.url = url;
        DatabaseEntry.password = password;
        DatabaseEntry.creationDate = getDateTime();
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = DatabaseEntry.getCreationDate().toString();

    }

    public DatabaseEntry(int id, String username, String description, String url, String password, String creationDate, String lastUpdate){
        DatabaseEntry.id = id;
        DatabaseEntry.username = username;
        DatabaseEntry.description = description;
        DatabaseEntry.url = url;
        DatabaseEntry.password = password;
        DatabaseEntry.creationDate = creationDate;
        //the first time, creationDate and lastUpdate have the same value
        DatabaseEntry.lastUpdate = lastUpdate;

    }


    public static void setId(int id) {
        DatabaseEntry.id = id;
    }

    public static int getId() {
        return id;
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
    /*
     * When setCreationDate is set (for i.e. when it is returned from the database). if creationDate has to be set, then call
     * getDateTime()
     * @see setCreationDate
     */
    public  static void setCreationDate(String time) {
        DatabaseEntry.creationDate = time;
    }


    public static String getLastUpdate() {
        return lastUpdate;
    }

    /*
    * When lastUpdate is set (for i.e. when it is returned from the database). if lastUpdate has to be set, then call
    * getDateTime()
    * @see getDateTime
     */
    public static void setLastUpdate(String lastUpdate) {
        DatabaseEntry.lastUpdate = lastUpdate;
    }


    /*
    * Methode to create the a time stamp for the date_creation (Database) and date_update (Database)
    * This methode is also used to store a time stamp value inside an DatabaseEntry Object, for the fields creationDate and
    * lastUpdate.
     */
    public static String getDateTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        //System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //System.out.println("After formatting: " + formattedDate);
        return myDateObj.format(myFormatObj);
    }


}

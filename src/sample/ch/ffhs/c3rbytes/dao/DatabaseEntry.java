package sample.ch.ffhs.c3rbytes.dao;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DatabaseEntry{

    private SimpleStringProperty  id;
    private SimpleStringProperty username;
    private SimpleStringProperty  description;
    private SimpleStringProperty  url;
    private SimpleStringProperty  password;
    private SimpleStringProperty  creationDate;
    private SimpleStringProperty  lastUpdate;
    private String passwordTrick;


    public DatabaseEntry(String id, String username, String description,
                         String url, String password, String creationDate,
                         String lastUpdate){
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.description = new SimpleStringProperty(description);
        this.url = new SimpleStringProperty(url);
        this.password = new SimpleStringProperty(password);
        this.creationDate = new SimpleStringProperty(creationDate);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        passwordTrick = "* * * * *";
    }


    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getCreationDate() {
        return creationDate.get();
    }

    public SimpleStringProperty creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }

    public String getLastUpdate() {
        return lastUpdate.get();
    }

    public SimpleStringProperty lastUpdateProperty() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public DatabaseEntry getAll(){
        return this;
    }

    /*
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


        * Constructor for an DatabaseEntry Object which contains an id
        *  @param id the id from the database
         * @param username the username
         * @param description what kind of entry it is (social media account, etc.)
         * @param url
         * @param password password is protected (hashed)

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

        *Constructor for an DatabaseEntry Object which contains an id
        * @param username the username
        * @param description what kind of entry it is (social media account, etc.)
        * @param url
        * @param password password is protected (hashed)

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

         * When setCreationDate is set (for i.e. when it is returned from the database). if creationDate has to be set, then call
         * getDateTime()
         * @see setCreationDate

        public  static void setCreationDate(String time) {
            DatabaseEntry.creationDate = time;
        }


        public static String getLastUpdate() {
            return lastUpdate;
        }


        * When lastUpdate is set (for i.e. when it is returned from the database). if lastUpdate has to be set, then call
        * getDateTime()
        * @see getDateTime

        public static void setLastUpdate(String lastUpdate) {
            DatabaseEntry.lastUpdate = lastUpdate;
        }



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


    public String getHiddenPasswordTrick() {
        return passwordTrick;
    }
}

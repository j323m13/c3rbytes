package sample.ch.ffhs.c3rbytes.DatabaseEntry;


import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DatabaseEntry {

    private SimpleStringProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty description;
    private SimpleStringProperty url;
    private SimpleStringProperty password;
    private SimpleStringProperty creationDate;
    private SimpleStringProperty lastUpdate;
    private String passwordTrick;
    private SimpleStringProperty note;
    private String dummyId;

    /*
    * Empty constructor
     */
    public DatabaseEntry() {
        this.id = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.url = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.creationDate = new SimpleStringProperty();
        this.lastUpdate = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        passwordTrick = "* * * * *";
        dummyId = null;
    }
    /*
    * Constructor with full paramenters.
    * @param: id, dummyId, username, description, url, password, creationDate, lastUpdate, note.
     */
    public DatabaseEntry(String id, String dummyId, String username, String description,
                         String url, String password, String creationDate,
                         String lastUpdate, String note) {
        this.id = new SimpleStringProperty(id);
        this.dummyId = dummyId;
        this.username = new SimpleStringProperty(username);
        this.description = new SimpleStringProperty(description);
        this.url = new SimpleStringProperty(url);
        this.password = new SimpleStringProperty(password);
        this.creationDate = new SimpleStringProperty(creationDate);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        this.note = new SimpleStringProperty(note);
        passwordTrick = "* * * * *";
    }

    public String getDescription() {
        if(creationDate.get() == null){
            creationDate.set(getDateTime());
        }
        return description.get();
    }


    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getCreationDate() {
        return creationDate.get();
    }


    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }

    public String getLastUpdate() {
        String lastupdateString = lastUpdate.get();
        return lastupdateString;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public String getUsername() {
        return username.get();
    }


    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getId() {
        return id.get();
    }

    public String getDummyId() {
        return dummyId;
    }

    public void setDummytId(String id) {
        this.dummyId = id;
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public DatabaseEntry getAll() {
        return this;
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note.set(note);
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

    /*
    * Retur 5 ***** to simulate a password field. it ain't stupid if it works.
     */
    public String getHiddenPasswordTrick() {
        return passwordTrick;
    }


}

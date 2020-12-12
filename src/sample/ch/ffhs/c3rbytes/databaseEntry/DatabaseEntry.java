package sample.ch.ffhs.c3rbytes.databaseEntry;


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

    /**
     * empty contructor
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

    /**
     * the contructor
     * @param id an id set by the database
     * @param dummyId the index of all the entries out of the database. it is used to have a better display in tableView
     * @param username username
     * @param description type of account
     * @param url url
     * @param password a password (encrypted)
     * @param creationDate date of creation of the entry (getDateTime() from this class)
     * @param lastUpdate date of last update (first time is the same date as date of creation)
     * @param note a note of the user.
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


    /**
     * get the description value (type of account)
     * @return description
     */
    public String getDescription() {
        if(creationDate.get() == null){
            creationDate.set(getDateTime());
        }
        return description.get();
    }

    /**
     * set the type of account (description)
     * @param description
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * get URL value
     * @return the url value
     */

    public String getUrl() {
        return url.get();
    }

    /**
     * set url value
     * @param url the url to be set
     */
    public void setUrl(String url) {
        this.url.set(url);
    }

    /**
     * get password value
     * @return the password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * set password value
     * @param password the password to be set
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * get the date of creation of the object
     * @return the date of creation
     */
    public String getCreationDate() {
        return creationDate.get();
    }

    /**
     * set the creation date
     * @param creationDate the creation date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }

    /**
     * get the last update value of the object
     * @return the last update value (a String)
     */
    public String getLastUpdate() {
        String lastupdateString = lastUpdate.get();
        return lastupdateString;
    }

    /**
     * set the last update of the object
     * @param lastUpdate the time of the last update (String)
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    /**
     * get the username value of an object
     * @return the username
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * set the username of a object
     * @param username the username value
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * get the id of the object (id is generated in the database)
     * @return the id
     */
    public String getId() {
        return id.get();
    }

    /**
     * get a dummy id (dummy id is calculated on the amount of objects contained in the observableList which
     * hold the results of a query.
     * in the table view we see a chronological index (which is not the case with database ids)
     * @return the dummy id
     */
    public String getDummyId() {
        return dummyId;
    }

    public void setDummytId(String id) {
        this.dummyId = id;
    }

    /**
     * set the id value
     * @param id the id value (String)
     */
    public void setId(String id) {
        this.id.set(id);
    }

    /**
     * get the note of an object
     * @return the note
     */
    public String getNote() {
        return note.get();
    }

    /**
     * set the note value
     * @param note the note value
     */
    public void setNote(String note) {
        this.note.set(note);
    }


    /**
     * Methode to create the a time stamp for the date_creation (Database) and date_update (Database)
     * This methode is also used to store a time stamp value inside an DatabaseEntry Object, for the fields creationDate and
     * lastUpdate.
     * @return myDateObj (LocalDateTime) formatted as dd-MM-yyyy HH:mm:ss
     */
    public static String getDateTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        //System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //System.out.println("After formatting: " + formattedDate);
        return myDateObj.format(myFormatObj);
    }

    /**
     * Return 5 ***** to simulate a password field. it ain't stupid if it works.
     * @return the 5 *****
     */
    public String getHiddenPasswordTrick() {
        return passwordTrick;
    }


}

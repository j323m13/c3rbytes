package sample.ch.ffhs.c3rbytes.models;

public class profileModel {
    private String profileName;
    private String password;
    private String category;
    private String url;
    private Integer id;
    private String notes;

    public profileModel(String profileName, String password){}

    public String getCategory() {
        return category;
    }

    public String getProfileName(){
        return profileName;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }


}

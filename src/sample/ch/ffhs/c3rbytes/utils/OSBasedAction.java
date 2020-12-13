package sample.ch.ffhs.c3rbytes.utils;

import sample.ch.ffhs.c3rbytes.connection.DBConnection;
import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;


public class OSBasedAction {
    private String osName = null;

    public OSBasedAction(){

    }

    /**
     * Methode to retur the absolute path of file / folder
     * @param fileName
     * @return file
     */
    public File getPath(String fileName){
        File file = null;
        Path filePath = Paths.get(fileName);
        System.out.println("path of file to delete: "+filePath.toAbsolutePath().toString());
        file = new File(filePath.toAbsolutePath().toString());
        return file;
    }

    /**
     * methode to return the os name
     * @return osName a string (low case) of the os.
     */
    public String getOSName(){
        String osName = null;
        osName = System.getProperty("os.name").toLowerCase();
        return osName;
    }

    /**
     * Delete a folder on user computer.
     * it iterates through a folder and delete the files.
     * @param file the file we want to delete.
     */
    public void deleteDatabaseFolder(File file){
            if (file.isDirectory()){
                for (File files : file.listFiles()) {
                    deleteDatabaseFolder(files);
                }
            }
            file.delete();
            //System.out.println(file.delete());
        }




    /**
     * set local value from the user for DB use later on.
     * this allow to use a non case sensitive db for queries later.
     * set DBConnection.localValues
     */
    public void setLocalValue() {
        DatabaseEntryDao helper = new DatabaseEntryDao();
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        String country = locale.getCountry();
        System.out.println(lang+"_"+country);
        helper.setLocalValuesDAO(lang+"_"+country);
    }
}

package sample.ch.ffhs.c3rbytes.utils;

import sample.ch.ffhs.c3rbytes.dao.DatabaseEntryDao;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * some OS based actions
 * @author Jérémie Equey
 */

public class OSBasedAction {
    private String osName = null;

    public OSBasedAction(){

    }

    /**
     * Methode to retur the absolute path of file / folder
     * @param fileName the name of the file from which we want to get the path.
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
     * @param file : the file we want to delete.
     * @throws  IOException if a problem occurs, an exception is raised (cannot be deleted).
     */
    public void deleteDatabaseFolder(File file) throws IOException {
            if (file.isDirectory()){
                for (File files : file.listFiles()) {
                    deleteDatabaseFolder(files);
                }
            }
            file.delete();

           if(getOSName().equals("win")){
               try {
                   if (file.toString().contains("derby.log")) {
                       System.out.println("derby.log path: " + file);
                       Runtime.getRuntime().exec("del " + file);

                   }
               } catch (IOException e){
                   e.printStackTrace();
               }
           }
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

    /**
     * print all the instance variable in the console.
     * @param object the object which will be printed.
     */
    public void printDatabaseEntryObject(DatabaseEntry object){
        System.out.print(object.getId());
        System.out.print(", ");
        System.out.print(object.getDummyId());
        System.out.print(", ");
        System.out.print(object.getUsername());
        System.out.print(", ");
        System.out.print(object.getDescription());
        System.out.print(", ");
        System.out.print(object.getPassword());
        System.out.print(", ");
        System.out.print(object.getUrl());
        System.out.print(", ");
        System.out.print(object.getCreationDate());
        System.out.print(", ");
        System.out.print(object.getLastUpdate());
        System.out.print(", ");
        System.out.print(object.getNote()+"\n");
    }
}

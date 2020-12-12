package sample.ch.ffhs.c3rbytes.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class deals with files
 *  @author Olaf Schmidt
 */

public class FileHandler {


    OSBasedAction helper = new OSBasedAction();

    /**
     * This method writes to a file and hides it. If the file does not exist. It will be created
     * @param file String. The filename
     * @param content byte[]. The content in bytes to write to the file
     * @throws IOException in case it is not possible to write to file
     */
    // if file exists, it overwrites it's content. if not, it creates a new file and write the content into
    public void writeToFile(String file, byte[] content) throws IOException {
        //check if the user os is Windows or something better.
        if(helper.getOSName().contains("win")){
            // get filepath
            Path path = Paths.get(file);

            // write to file
            Files.write(path, content);
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE);

        }else{
            System.out.println("Unix system");
            Path path = Paths.get(file);

            // write to file
            Files.write(path, content);
        }

    }

    /**
     * This method reads from a file or throws an exception if it does not exist
     * @param filenameFinal String. The filename
     * @return returns the content of the file (String)
     * @throws IOException if the file is missing
     */
    public String readFromFile(String filenameFinal) throws IOException {
        String filename = filenameFinal;
        // create new file instance
        if (!helper.getOSName().contains("win")) {
            filename = "."+filenameFinal;
        }
        System.out.println("filename " +filename);
        File file = new File(filename);


        // if not exists return, else read and cast content to string
        if (file.exists()) {
            byte[] fileContent = Files.readAllBytes(Paths.get(filename));

            String content = new String(fileContent, StandardCharsets.UTF_8);
            System.out.println(content);
            return content;
        } else {
            System.out.println("not exist");
            return "File does not exist";
        }
    }

    /**
     * This method sets read or write attributes of a file
     * @param file String. The filename
     * @param lock boolean. true if the file should be locked (write). false if the file is not write-locked
     */
    public void setReadWriteAttributes(String file, boolean lock) {
        File file1 = new File(file);

        System.out.println("fileabspath: " + file1.getAbsolutePath());

        if (lock){
            // Another approach
            //Runtime.getRuntime().exec(new String[]{"attrib", "+H", file});
            //Runtime.getRuntime().exec("attrib +H +W +R" + file1);
            file1.setWritable(false);
        } else{
            //Runtime.getRuntime().exec("attrib +H -W -R" + file1);
            file1.setWritable(true);
        }

    }
    //Todo: use delte methode in OSBasedAction.java
    public void deleteFile(String filename){
        File file = new File(filename);

        if (file.exists()){
            file.delete();
        } else {
            System.out.println("File does not exist");
        }
    }

}

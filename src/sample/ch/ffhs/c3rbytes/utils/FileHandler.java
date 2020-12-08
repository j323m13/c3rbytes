package sample.ch.ffhs.c3rbytes.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;


public class FileHandler {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    // if file exists, it overwrites it's content. if not, it creates a new file and write the content into
    public void writeToFile(String file, byte[] content) throws IOException {
        String osName = null;
        osName = System.getProperty("os.name").toLowerCase();
        //check if the user os is Windows or something better.
        if(osName.contains("win")){
            System.out.println("windows");
            // get filepath
            Path path = Paths.get(file);

            // write to file
            Files.write(path, content);
            //Files.setAttribute(path, "dos:hidden", Boolean.TRUE);

            //setReadWriteAttributes(file, false);
            //setReadWriteAttributes(file, "deny");
        }else{
            System.out.println("Unix system");
            Path path = Paths.get(file);

            // write to file
            Files.write(path, content);
        }

    }

    public String readFromFile(String filenameFinal) throws IOException {
        String filename = filenameFinal;
        String osName = null;
        // create new file instance
        osName = System.getProperty("os.name").toLowerCase();
        System.out.println(osName);
        if (!osName.contains("win")) {
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


    public void setReadWriteAttributes(String file, boolean lock) throws IOException {
        File file1 = new File(file);

        //Path path = Paths.get(file);
        //System.out.println("path: " + path);
        System.out.println("fileabspath: " + file1.getAbsolutePath());


        if (lock){
            //Runtime.getRuntime().exec(new String[]{"attrib", "+H", file});
            //Runtime.getRuntime().exec("attrib +H +W +R" + file1);
            file1.setReadable(false);
            file1.setWritable(false);

        } else{
            //Runtime.getRuntime().exec("attrib +H -W -R" + file1);
            file1.setWritable(true);
            file1.setReadable(true);
        }

        /*
        switch (isHidden){
            case "allow":
                // allow all permissions
                Runtime.getRuntime().exec(new String[]{"attrib", "+H", file});
                //Runtime.getRuntime().exec(new String[]{"cacls", file1.getAbsolutePath(), "/E", "/P", "Benutzer:f"} );
                return;

            case "deny":
                // deny all permissions
                //Runtime.getRuntime().exec(new String[]{"cacls", file1.getAbsolutePath(), "/E", "/P", "Benutzer:n"});
                return;
        }

        //file1.setWritable(permissionToAccess);
        //file1.setReadable(bool);

         */
    }

    public void deleteFile(String filename){
        File file = new File(filename);

        if (file.exists()){
            file.delete();
        } else {
            System.out.println("File does not exist");
        }
    }

    public boolean fileExists(File filename){
        if (filename.exists()){
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler();

        byte[] content = "Test".getBytes(UTF_8);
        //fileHandler.writeToFile("testfile.txt",content);
        fileHandler.readFromFile("testfile.txt");
        //fileHandler.setReadWriteAttributes("testfile.txt", true);
        //fileHandler.deleteFile("testfile.txt");

    }



}

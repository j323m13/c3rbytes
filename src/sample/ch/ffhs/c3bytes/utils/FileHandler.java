package sample.ch.ffhs.c3bytes.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileHandler {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    // if file exists, it overwrites it's content. if not, it creates a new file and write the content into
    public void writeToFile(String file, byte[] content) throws IOException {

        // get filepath
        Path path = Paths.get(file);

        // write to file
        Files.write(path, content);
    }

    public String readFromFile(String filename) throws IOException {

        // create new file instance
        File file = new File(filename);

        // if not exists return, else read and cast content to string
        if (!file.exists()) {
            return "File does not exist";
        } else {
            byte[] fileContent = Files.readAllBytes(Paths.get(filename));

            String content = new String(fileContent, StandardCharsets.UTF_8);

            return content;
        }
    }
}

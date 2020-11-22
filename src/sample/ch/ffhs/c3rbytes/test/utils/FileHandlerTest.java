package sample.ch.ffhs.c3rbytes.test.utils;

import org.junit.Test;
import sample.ch.ffhs.c3rbytes.utils.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileHandlerTest {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    @Test
    public void fileHandlerTest() throws IOException {

        FileHandler fileHandler = new FileHandler();
        String filename = "testfile";
        String content = "heidihou";
        byte [] byteContent = "heidihou".getBytes(UTF_8);

        fileHandler.writeToFile(filename, byteContent);

        File file = new File(filename);

        assertTrue(file.exists());

        //fileHandler.setReadWriteAttributes(filename,false);
        fileHandler.setReadWriteAttributes(filename,"deny");

        assertTrue(file.canWrite() == false);

        //fileHandler.setReadWriteAttributes(filename,true);
        fileHandler.setReadWriteAttributes(filename,"allow");

        assertTrue(file.canWrite() == true);

        String fileContent = fileHandler.readFromFile(filename);

        assertTrue(content.equals(fileContent));


    }

}

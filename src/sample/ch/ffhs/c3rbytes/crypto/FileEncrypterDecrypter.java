package sample.ch.ffhs.c3rbytes.crypto;

import sample.ch.ffhs.c3rbytes.utils.FileHandler;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;

public class FileEncrypterDecrypter {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    OSBasedAction handler = new OSBasedAction();

    public void encryptFile(String content, String toFile, String password) throws Exception {

        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();

        // read a normal txt
        byte[] contentBytes =  content.getBytes(UTF_8);

        // encrypt content with password
        String encryptedText = passwordEncrypterDecrypter.encrypt(contentBytes, password);

        System.out.println("encryptedText: " + encryptedText);

        // in purpose of obfuscation decoding in Base64 --> odd way
        //byte[] encryptedByteText = Base64.getDecoder().decode(encryptedText.getBytes(UTF_8));

        // Base64 Encoding the standard way
        byte[] encryptedByteText = Base64.getEncoder().encode(encryptedText.getBytes(UTF_8));

        FileHandler fileHandler = new FileHandler();

        if(handler.getOSName().contains("win")){
            // save to file and disable write permissions
            //fileHandler.setReadWriteAttributes(toFile,false);
            //fileHandler.setReadWriteAttributes(toFile,"allow");
            //Thread.sleep(1000);
            fileHandler.writeToFile(toFile, encryptedByteText);
            //fileHandler.setReadWriteAttributes(toFile, true);
            //fileHandler.setReadWriteAttributes(toFile,false);
            //fileHandler.setReadWriteAttributes(toFile,"deny");
        }else{
            System.out.println(encryptedText);
            System.out.println(toFile);
            fileHandler.writeToFile("."+toFile, encryptedByteText);
        }
        fileHandler = null;
        passwordEncrypterDecrypter = null;
    }

    public byte[] decryptFile(String fromEncryptedFile, String password) throws Exception {
        String fromEncryptedFilePath = null;
        // create new passwordEncrypterDecrypter
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();

        // Enable write Persmissions of the file
        FileHandler fileHandler = new FileHandler();
        //fileHandler.setReadWriteAttributes(fromEncryptedFile, true);
        //fileHandler.setReadWriteAttributes(fromEncryptedFile, true);

        //fileHandler.setReadWriteAttributes(fromEncryptedFile, "allow");
        if(handler.getOSName().contains("win")){
            fromEncryptedFilePath = fromEncryptedFile;
        }else {
            fromEncryptedFilePath = "."+fromEncryptedFile;
        }

        // read from file
        System.out.println(fromEncryptedFilePath);
        byte[] fileContent = Files.readAllBytes(Paths.get(fromEncryptedFilePath));

        // in purpose of obfuscation encoding in Base64 --> odd way
        //String cryptedText = Base64.getEncoder().encodeToString(fileContent);

        // Base64 Decoding the standard way
        byte[] cryptedTextBytes = Base64.getDecoder().decode(fileContent);
        String cryptedText = new String(cryptedTextBytes,UTF_8);

        // Decrypt crypted Text
        String decryptedText = passwordEncrypterDecrypter.decrypt(cryptedText, password);

        // Cast to Bytes
        byte[] decryptedByteText = decryptedText.getBytes(UTF_8);

        /* in case to decrypt passphrasefiel c3r.c3r
        FileHandler fileHandler = new FileHandler();
        fileHandler.writeToFile(fromEncryptedFile, decryptedByteText);
        */

        //fileHandler.setReadWriteAttributes(fromEncryptedFile, false);
        //fileHandler.setReadWriteAttributes(fromEncryptedFile, "deny");
        //fileHandler.setReadWriteAttributes(fromEncryptedFile, false);

        return decryptedByteText;

    }
}




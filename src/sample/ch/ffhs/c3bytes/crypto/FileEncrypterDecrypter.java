package sample.ch.ffhs.c3bytes.crypto;

import sample.ch.ffhs.c3bytes.utils.FileHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileEncrypterDecrypter {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    private static FileEncrypterDecrypter enc;

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

        // save to file
        FileHandler fileHandler = new FileHandler();
        fileHandler.writeToFile(toFile, encryptedByteText);
        fileHandler = null;

        passwordEncrypterDecrypter = null;
    }

    public byte[] decryptFile(String fromEncryptedFile, String password) throws Exception {

        // create new passwordEncrypterDecrypter
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();

        // read from file
        byte[] fileContent = Files.readAllBytes(Paths.get(fromEncryptedFile));

        // in purpose of obfuscation encoding in Base64 --> odd way
        //String cryptedText = Base64.getEncoder().encodeToString(fileContent);

        // Base64 Decoding the standard way
        byte[] cryptedTextBytes = Base64.getDecoder().decode(fileContent);
        String cryptedText = new String(cryptedTextBytes,UTF_8);

        // Decrypt crypted Text
        String decryptedText = passwordEncrypterDecrypter.decrypt(cryptedText, password);

        // Cast to Bytes
        byte[] decryptedByteText = decryptedText.getBytes(UTF_8);

        return decryptedByteText;

    }
}




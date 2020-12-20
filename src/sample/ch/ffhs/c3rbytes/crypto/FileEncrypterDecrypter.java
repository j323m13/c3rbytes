package sample.ch.ffhs.c3rbytes.crypto;

import sample.ch.ffhs.c3rbytes.utils.FileHandler;
import sample.ch.ffhs.c3rbytes.utils.OSBasedAction;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * This class uses the PasswordEncrypterDecrypter to en- or decrypt the content of a given file and a chosen password.
 * @author Olaf Schmidt
 *
 */


public class FileEncrypterDecrypter {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;
    OSBasedAction handler = new OSBasedAction();

    /**
     *
     * This method encrypts a content in a file with a choosen password
     *
     * @param content The content to encrypt
     * @param toFile The filename to save the encrypted content
     * @param password The password to encrypt the content
     * @throws Exception in case of wrong encryption {@code -->}  wrong
     */
    public void encryptFile(String content, String toFile, String password) throws Exception {

        // create new PasswordEncrypterDecryptr Object
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

        // Make file hidden and apply write protection
        FileHandler fileHandler = new FileHandler();

        if(handler.getOSName().contains("win")){
            fileHandler.setReadWriteAttributes(toFile,false);
            fileHandler.writeToFile(toFile, encryptedByteText);
            fileHandler.setReadWriteAttributes(toFile,true);
        }else{
            System.out.println(encryptedText);
            System.out.println(toFile);
            fileHandler.writeToFile("."+toFile, encryptedByteText);
        }
    }

    /**
     * This method decrypts a file with the password
     *
     * @param fromEncryptedFile The String of the encrypted File
     * @param password The password to decrypt the filecontent
     * @return decrypted text in bytes
     * @throws Exception in case of wrong decryption {@code -->} ex. wrong password
     */
    public byte[] decryptFile(String fromEncryptedFile, String password) throws Exception {
        String fromEncryptedFilePath;

        // create new passwordEncrypterDecrypter
        PasswordEncrypterDecrypter passwordEncrypterDecrypter = new PasswordEncrypterDecrypter();

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

        return decryptedByteText;

    }
}




package sample.ch.ffhs.c3rbytes.crypto;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.crypto.FileEncrypterDecrypter;

public class FileEncrypterDecrypterTest {

    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    // encrypt and decrypt with same password, returns string in the file
    @Test
    public void correct_pass() throws Exception {

        FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();

        String originalContent = "Das ist ein Test";
        //String originalContent = "";
        String file = ".test.txt";
        String password = "password123";

        // encrypt file
        fileEncrypterDecrypter.encryptFile(originalContent, file, password);

        // decrypt file
        String pText = "null";
        try {
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(file, password);
            pText = new String(decryptedText, UTF_8);
            System.out.println(pText);
            System.out.println("Access granted");
        } catch (javax.crypto.AEADBadTagException e) {
            System.out.println("Access denied");
        }
        assertTrue(pText.equals(originalContent));
    }

    // decrypt encrypted file with a wrong password
    @Test
    public void wrong_pass() throws Exception {

        FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();

        String originalContent = "Das ist ein Test";
        String file = ".test.txt";
        String password = "password123";

        // encrypt file
        //fileEncrypterDecrypter.encryptFile(originalContent, file, password);

        password = "leer";

        // decrypt file
        String pText = "Access denied";
        try {
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(file, password);
            pText = new String(decryptedText, UTF_8);
            System.out.println(pText);
            System.out.println("Access granted");
        }catch(javax.crypto.AEADBadTagException e){
            System.out.println("Access denied");
        }
        assertFalse(pText.equals(originalContent));
    }

    @Test
    public void decryptFile() throws Exception {

        FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();

        String originalContent = "LUv&qp3ewq^Hn+VB+ *s{rgUGkodd(3P";
        String file = "c3r.c3r";
        String password = "123";

        // decrypt file
        String pText = "Access denied";
        try {
            byte[] decryptedText = fileEncrypterDecrypter.decryptFile(file, password);
            pText = new String(decryptedText, UTF_8);
            System.out.println(pText);
            System.out.println("Access granted");
        }catch(javax.crypto.AEADBadTagException e){
            System.out.println("Access denied");
        }
        assertTrue(pText.equals(originalContent));
    }

}

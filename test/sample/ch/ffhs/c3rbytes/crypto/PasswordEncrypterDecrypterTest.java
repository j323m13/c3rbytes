package sample.ch.ffhs.c3rbytes.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sample.ch.ffhs.c3rbytes.crypto.PasswordEncrypterDecrypter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PasswordEncrypterDecrypterTest {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    @Test
    public void correctText() throws Exception {

        String PASSWORD = "this is a passphrase";
        String plainText = "AES-GSM encryption!";

        PasswordEncrypterDecrypter encryptorAesGcmPassword = new PasswordEncrypterDecrypter();

        String encryptedTextBase64 = encryptorAesGcmPassword.encrypt(plainText.getBytes(UTF_8), PASSWORD);

        System.out.println("AES GCM Encryption");
        System.out.println("Input (plain text): " + plainText);
        System.out.println("Encrypted (base64): " + encryptedTextBase64);

        System.out.println("AES GCM Decryption");
        System.out.println("Input (base64): " + encryptedTextBase64);

        String decryptedText = encryptorAesGcmPassword.decrypt(encryptedTextBase64, PASSWORD);
        System.out.println("Decrypted (plain text): " + decryptedText);

        assertTrue(plainText.equals(decryptedText));
    }

    @Test
    public void wrongText() throws Exception {

        String password = "this is a password";
        String plainText = "AES-GSM Password-Bases encryption!";

        PasswordEncrypterDecrypter encryptorAesGcmPassword = new PasswordEncrypterDecrypter();

        String encryptedTextBase64 = encryptorAesGcmPassword.encrypt(plainText.getBytes(UTF_8), password);

        System.out.println("AES GCM Encryption");
        System.out.println("Input (plain text): " + plainText);
        System.out.println("Encrypted (base64): " + encryptedTextBase64);

        System.out.println("AES GCM Decryption");
        System.out.println("Input (base64): " + encryptedTextBase64);

        // changing the password
        password = "this is not a password";
        String decryptedText;
        try {
            decryptedText = encryptorAesGcmPassword.decrypt(encryptedTextBase64, password);
            System.out.println("Decrypted (plain text): " + decryptedText);
        }catch(javax.crypto.AEADBadTagException e){
            decryptedText = "Access denied";
        }

        assertFalse(plainText.equals(decryptedText));
    }


}

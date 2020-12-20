package sample.ch.ffhs.c3rbytes.crypto;

import sample.ch.ffhs.c3rbytes.utils.CryptoUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This class encrypts or decrypts content with AES-CBC.
 * @author Olaf Schmidt
 *
 * AES-CBC inputs - 16 bytes IV, need the same IV and secret keys for encryption and decryption.
 * <p>
 * The output consist of iv, password's salt, encrypted content and auth tag in the following format:
 * output = byte[] {i i i s s s c c c c c c ...}
 * <p>
 * i = IV bytes
 * s = Salt bytes
 * c = content bytes (encrypted content)
 */

public class PasswordEncrypterDecrypter {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     *This method encrypts the plain text in bytes with the choosen password and adds a random salt (16 bytes) and an
     * intialisation vector (12 bytes)
     *
     * @param plainText The plaintext to encrypt
     * @param password The password to encrypt the plaintext
     * @return encrypted String of the plaintext (Base64)
     * @throws Exception if something went wrong
     */
    // return a base64 encoded AES encrypted text
    public String encrypt(byte[] plainText, String password) throws Exception {

        // 16 bytes salt
        byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);

        // GCM recommended 12 bytes iv
        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

        // secret key from password
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        // get cipher
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        // ASE-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] cipherText = cipher.doFinal(plainText);

        // prefix IV and Salt to cipher text
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // string representation, base64, send this string to other for decryption.
        return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    }

    /**
     *This method decrypts a String (ciphertext in BASE64) with a password
     *
     * @param cipherText The encrypted String (Base64)
     * @param password The password to decrypt the String
     * @return decrypted plaintext
     * @throws Exception if something went wrong {@code -->} ex. wrong password
     */
    // we need the same password, salt and iv to decrypt it (reverse order)
    public String decrypt(String cipherText, String password) throws Exception {

        // Base64 decoding
        byte[] decode = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));

        // get back the iv and salt from the cipher text
        ByteBuffer bb = ByteBuffer.wrap(decode);

        // 12 bytes iv
        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);

        // 16 bytes salt
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        bb.get(salt);

        // content
        byte[] cipherTextBytes = new byte[bb.remaining()];
        bb.get(cipherTextBytes);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] plainText = cipher.doFinal(cipherTextBytes);

        return new String(plainText, UTF_8);
    }
}




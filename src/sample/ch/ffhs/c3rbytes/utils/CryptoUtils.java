package sample.ch.ffhs.c3rbytes.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * This class is a helper class. It creates either a RandomNonce for salts and iv's or provides a secrectkey for an AES Cipher
 * @author Olaf Schmidt
 */

public class CryptoUtils {

    /**
     * This method creates the salt and the iv for our AES-encrypted content
     * @param numBytes int. The length of bytes for the salt or the iv
     * @return returns a number only once
     */
    // salt an iv for passwordEncrypterDecrypter
        public static byte[] getRandomNonce(int numBytes) {
            byte[] nonce = new byte[numBytes];
            new SecureRandom().nextBytes(nonce);
            return nonce;
        }

        // AES secret key
        public static SecretKey getAESKey(int keysize) throws NoSuchAlgorithmException {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keysize, SecureRandom.getInstanceStrong());
            return keyGen.generateKey();
        }

    /**
     * This method creates an 256 bit secret key derived from the password
     * @param password The password to be derived
     * @param salt The salt created from the method getRandomNonce
     * @return returns the secretkey derived from password and the salt
     * @throws NoSuchAlgorithmException if the desired alorithm does not exist
     * @throws InvalidKeySpecException if the key is invalid
     */

        public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            // iterationCount = 65536
            // keyLength = 256
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret;

        }

}




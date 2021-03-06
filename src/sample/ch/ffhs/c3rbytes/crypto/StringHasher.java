package sample.ch.ffhs.c3rbytes.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * This class hashes a String in the desired hash-algorithm ex. SHA3-512
 *  @author Olaf Schmidt
 */

    public class StringHasher {

    /**
     * This method prepares a digest for
     *
     * @param input input in bytes
     * @param algorithm String. It is the desired hash algorithm. ex. SHA3-512
     * @return returns the messagedigest in bytes
     */
        private byte[] digest(byte[] input, String algorithm) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e);
            }
            byte[] result = md.digest(input);
            return result;
        }

    /**
     * This method converts bytes to hex values
     *
     * @param bytes the bytes to convert into hex
     * @return String in hexadecimal format
     */
    private String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        // in case of ...
        public byte[] hexToBytes(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
            return data;
        }

    /**
     * This method hashes a String in the desired hash algorithm
     *
     * @param algorithm String. The desired hash algorithm
     * @param password String. The string to hash
     * @return returns a String in hexadecimal format
     */
        public String encryptSHA3(String algorithm, String password){

            byte[] shaInBytes = new byte[0];
            try {
                byte[] textInBytes = password.getBytes(StandardCharsets.UTF_8);

                shaInBytes = this.digest(textInBytes, algorithm);

                // debugging
                System.out.println("Input (string): \t" + password);
                System.out.println("Input (length): \t" + password.length());
                System.out.println(algorithm + " (hex): \t" + bytesToHex(shaInBytes));
                System.out.println(algorithm + " (length): \t" + shaInBytes.length);
                //

                
            } catch (Exception e){
                e.printStackTrace();
            }
            return bytesToHex(shaInBytes);
        }
    }

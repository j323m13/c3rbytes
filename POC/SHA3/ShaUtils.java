package sha3;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ShaUtils {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String OUTPUT_FORMAT = "%-20s:%s";

    public static byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    public static void main(String[] args) {


        //String algorithm = "SHA3-512";
        String algorithm = "SHA3-512";

        String pText = "Hello World";
 
        
        //byte[] salt = getRandomNonce(16);
        byte[] text = pText.getBytes(StandardCharsets.UTF_8);     
        
        //byte[] input = ByteBuffer.allocate(salt.length + text.length)
                //.put(salt)
        //byte[] input = ByteBuffer.allocate(text.length)
        //        .put(text)
        //        .array();
                
        
        System.out.println(String.format(OUTPUT_FORMAT, "Input (string)", pText));
        System.out.println(String.format(OUTPUT_FORMAT, "Input (length)", pText.length()));

        //byte[] shaInBytes = ShaUtils.digest(pText.getBytes(UTF_8), algorithm);
        byte[] shaInBytes = ShaUtils.digest(text, algorithm);
        
        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (hex) ", bytesToHex(shaInBytes)));
        
        // fixed length, 32 bytes, 256 bits.
        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (length)", shaInBytes.length));
        //System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (salted 16 b)", bytesToHex(ShaUtils.digest(input, "SHA3-512"))));


    }
}
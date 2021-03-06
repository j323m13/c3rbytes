package sha3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Sha3Utils {
    public byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
	public byte[] hexToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
    
    public String encryptSHA3(String algorithm, String password){

        byte[] textInBytes = password.getBytes(StandardCharsets.UTF_8);
    	
    	byte[] shaInBytes = this.digest(textInBytes, algorithm);
        
        System.out.println("Input (string): \t" + password);
        System.out.println("Input (length): \t" + password.length());
        System.out.println(algorithm + " (hex): \t" + bytesToHex(shaInBytes));
        System.out.println(algorithm + " (length): \t" + shaInBytes.length);
        
        return bytesToHex(shaInBytes);    	
    }
    
    public static void main(String[] args) {
    	Sha3Utils sha = new Sha3Utils();
    	sha.encryptSHA3("SHA3-512", "test");    	
    }
    
}


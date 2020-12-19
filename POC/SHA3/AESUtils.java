package sha3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;



public class AESUtils {
	
	private final String ALGORITHM = "AES";
	private final String DIGESTALGORITHM = "SHA3-512";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private Sha3Utils sha3;
	
	public SecretKeySpec getSecretKeySpec(String password) {
		
		SecretKeySpec secretKeySpec = null;
		
		byte[] key = null;
		try {
			key = password.getBytes("UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sha3 = new Sha3Utils();
		key = sha3.digest(key, DIGESTALGORITHM);
		//key = Arrays.copyOf(key, 32);

		//Offset starts at 20, lentth 16 from key		
		secretKeySpec = new SecretKeySpec(key, 20 , 16, ALGORITHM);
		
		return secretKeySpec;
		
	}
	
	public Cipher getCipher(SecretKeySpec secretKeySpec) {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipher;
	}
		
	public String encryptAES(String password, String passwordToEncrypt) {
		Cipher cipher = null;
		String encryptedString = null;	
		
		try {
			
			SecretKeySpec secretKeySpec = getSecretKeySpec(password);
	
			cipher = getCipher(secretKeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encrypted = cipher.doFinal(passwordToEncrypt.getBytes());
			encryptedString = sha3.bytesToHex(encrypted);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return encryptedString;
	}
	    
    public String decryptAES(String password, String cipherText) {
    	SecretKeySpec secretKeySpec = getSecretKeySpec(password);
    	Cipher cipher = null;
    	byte[] plainText = "Wrong Password".getBytes();
    	
    	byte[] cText = sha3.hexToBytes(cipherText);
    	
    	try {
    		cipher = getCipher(secretKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);    	     	
			plainText =  cipher.doFinal(cText);
			System.out.println("Access granted");
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("Access denied");
			//e.printStackTrace();
		}
    	    	
       	
    	return new String(plainText, UTF_8);
    	
    }  	

}

package encrypter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

class CryptoTest {

	@Test
	public void correct_pass() {
	    String originalContent = "test";
	    SecretKeySpec secretKeySpec;
		try {
			
			// Das Passwort bzw der Schluesseltext
			String keyStr = "geheim";
			// byte-Array erzeugen
			byte[] key = (keyStr).getBytes("UTF-8");
			// aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			key = sha.digest(key);
			// nur die ersten 128 bit nutzen
			key = Arrays.copyOf(key, 16); 
			// der fertige Schluessel
			secretKeySpec = new SecretKeySpec(key, "AES");
			System.out.println(secretKeySpec.toString());
			
			     
			 
			// der zu verschl. Text
			//String text = "Das ist der Text";
			 
			// Verschluesseln
			//Cipher cipher = Cipher.getInstance("AES");
		  
		    

		    //secretKey = KeyGenerator.getInstance("AES").generateKey();
			FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKeySpec, "AES");
			fileEncrypterDecrypter.encrypt(originalContent, "test.enc");
			String decryptedContent = fileEncrypterDecrypter.decrypt("test.enc");
			assertTrue(decryptedContent.equals(originalContent));
			//new File("baz.enc").delete();//cleanup
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	@Test
	public void wrong_pass() throws BadPaddingException {
	    String originalContent = "test";
	    SecretKeySpec secretKeySpec;
	    SecretKeySpec secretKeySpec2;
		try {
			
			// Das Passwort bzw der Schluesseltext
			String keyStr = "geheim";
			// byte-Array erzeugen
			byte[] key = (keyStr).getBytes("UTF-8");
			// aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			key = sha.digest(key);
			// nur die ersten 128 bit nutzen
			key = Arrays.copyOf(key, 16); 
			// der fertige Schluessel
			secretKeySpec = new SecretKeySpec(key, "AES");
			System.out.println(secretKeySpec.toString());
			
			     
			 
			// der zu verschl. Text
			//String text = "Das ist der Text";
			 
			// Verschluesseln
			//Cipher cipher = Cipher.getInstance("AES");
		  
			// Das Passwort bzw der Schluesseltext
			String keyStr2 = "nicht geheim";
			// byte-Array erzeugen
			byte[] key2 = (keyStr2).getBytes("UTF-8");
			// aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
			MessageDigest sha2 = MessageDigest.getInstance("SHA-512");
			key2 = sha2.digest(key2);
			// nur die ersten 128 bit nutzen
			key2 = Arrays.copyOf(key2, 16); 
			// der fertige Schluessel
			secretKeySpec2 = new SecretKeySpec(key2, "AES");
			System.out.println(secretKeySpec2.toString());
			

		    //secretKey = KeyGenerator.getInstance("AES").generateKey();
			FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKeySpec, "AES");
			fileEncrypterDecrypter.encrypt(originalContent, "test.enc");
			
			FileEncrypterDecrypter fileEncrypterDecrypter2 = new FileEncrypterDecrypter(secretKeySpec2, "AES");
			String decryptedContent = fileEncrypterDecrypter2.decrypt("test.enc");
			assertFalse(decryptedContent.equals(originalContent));
			//new File("baz.enc").delete();//cleanup
		} catch (IOException e) {
			System.out.println("wrong pw");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("NullPointer");
		}
	}

}

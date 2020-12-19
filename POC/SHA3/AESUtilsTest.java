package sha3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class AESUtilsTest {
	
	String cText;
	
	@Test
	public void correctPwAES() {
	String password = "Test";
	String textToEncrypt = "Ein Test";
	
	AESUtils au = new AESUtils();
	
	cText = au.encryptAES(password, textToEncrypt);
	String decryptedContent = au.decryptAES("Test", cText);
	assertTrue(decryptedContent.equals(textToEncrypt));
	
	System.out.println("correctPwAES------------------------------------------");
	System.out.println("Passwort: " + password);
	System.out.println("textToEncrypt: " + textToEncrypt);
	
	System.out.println("ciphertext correct: " +  cText);
	System.out.println("DecryptedContent: " + decryptedContent);
	
	}
	
	@Test
	public void wrongPwAES() {
	String password = "nope";
	String textToEncrypt = "Ein Test";
	cText = "72bf7703c674721fe734c9eb7a852743";
	
	AESUtils au = new AESUtils();
	
	
	String decryptedContent = au.decryptAES(password, cText);
	
	assertFalse(decryptedContent.equals(textToEncrypt));
	
	System.out.println("wrongPwAES------------------------------------------");
	System.out.println("Passwort: " + password);
	System.out.println("textToEncrypt: " + textToEncrypt);
	
	System.out.println("ciphertext correct: " +  cText);
	System.out.println("DecryptedContent: " + decryptedContent);
	}

}

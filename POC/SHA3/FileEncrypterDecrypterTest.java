package sha3;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.crypto.BadPaddingException;
import org.junit.jupiter.api.Test;

class FileEncrypterDecrypterTest {

	@Test
	public void correct_pass() {
	    String originalContent = "Das ist ein Test";
	    String password = "geheim";
		
		FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(password);
		fileEncrypterDecrypter.encrypt(originalContent, "c3r.c3r");
		String decryptedContent = fileEncrypterDecrypter.decrypt("c3r.c3r");
		assertTrue(decryptedContent.equals(originalContent));
	}
	
	@Test
	public void wrong_pass() throws BadPaddingException {
		String originalContent = "Das ist ein Test";
	    String password = "nicht";

		FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(password);
		//fileEncrypterDecrypter.encrypt(originalContent, "c3r.c3r");
		String decryptedContent = fileEncrypterDecrypter.decrypt("c3r.c3r");
		assertFalse(decryptedContent.equals(originalContent));
    
	}
}


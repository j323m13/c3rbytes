package sha3;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;

public class FileEncrypterDecrypter {
	SecretKeySpec secretKeySpec;
	Cipher cipher;
	
	FileEncrypterDecrypter(String password){
		AESUtils au = new AESUtils();
		this.secretKeySpec = au.getSecretKeySpec(password);
		this.cipher = au.getCipher(secretKeySpec);
	}

	public void encrypt(String content, String fileName) {
	    try {
	    	cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encrypted = cipher.doFinal(content.getBytes());
			
			FileOutputStream fileOut = new FileOutputStream(fileName);
			CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
			cipherOut.write(encrypted);
			cipherOut.close();
			fileOut.close();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String decrypt(String fileName) {
	    String plainText = "Access denied";
	    
	    try {
			FileInputStream fileIn = new FileInputStream(fileName);
			
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[4096];
			while ((len = cipherIn.read(buffer, 0, buffer.length)) != -1) {
			    baos.write(buffer, 0, len);
			}
			baos.flush();
			byte[] cipherByteArray = baos.toByteArray();
	
	        byte[] cipherData2 = cipher.doFinal(cipherByteArray);
		    
	        plainText = new String(cipherData2);
		    cipherIn.close();
		    fileIn.close();
						
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				System.out.println("Access denied");
			}			catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Access denied");
			}

		return plainText;	
		}

}

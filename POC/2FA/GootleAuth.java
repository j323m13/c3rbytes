package google;

import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import de.taimos.totp.TOTP;


public class GootleAuth {

	static String secretKey = "";
	static String lastCode = null;
	
	public static String generateSecretKey() {
	    SecureRandom random = new SecureRandom();
	    byte[] bytes = new byte[20];
	    random.nextBytes(bytes);
	    Base32 base32 = new Base32();
	    return base32.encodeToString(bytes);
	}
	
	public static String getTOTPCode(String secretKey) {
	    Base32 base32 = new Base32();
	    byte[] bytes = base32.decode(secretKey);
	    String hexKey = Hex.encodeHexString(bytes);
	    return TOTP.getOTP(hexKey);
	}
	

	public static void run() {
		while (true) {
		    String code = getTOTPCode(secretKey);
		    if (!code.equals(lastCode)) {
		        System.out.println(code);
		    }
		    lastCode = code;
		    try {
		        Thread.sleep(1000);
		    } catch (InterruptedException e) {};
		}
	}
	
	
	public static void main(String[] args) {
		
		secretKey = "Q3P4EXVXZYYNYU67GBGHR4AZS7BZYSLD§";
		
		System.out.println(secretKey);
		run();

		
	}

	


}



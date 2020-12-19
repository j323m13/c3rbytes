package pwGen;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {

	private String str;
	private int randInt;
	private StringBuilder sb;
	private List<Integer> l;
	private int passwordLength;
	
	// Constructor
	public PasswordGenerator() {
	    this.l = new ArrayList<>();
	    this.sb = new StringBuilder();	
	}
	
	// prepare desired ASCII CharSet
	private void prepareCharSets(int[] charSet) {
		ArrayList<Integer> lowerCaseLetters = new ArrayList<>();
		ArrayList<Integer> upperCaseLetters = new ArrayList<>();
		ArrayList<Integer> digits = new ArrayList<>();
		ArrayList<Integer> specialChars = new ArrayList<>();
		ArrayList<ArrayList<Integer>> charSetContainer = new ArrayList<>();	
		
		// ASCII lower case letters 97 - 122
		// ASCII upper case letters 65 - 90
		// ASCII digits 48 - 57
		// ASCII special char 32 - 47, 58 - 64, 91 - 96, 123 - 126 
		
		// loop through all ASCII chars
		for (int i = 32; i < 127; i++) {
			
			// lower case letters
			if (i >= 97 && i <= 122) {
				lowerCaseLetters.add(i);					
			}
			
			// upper case letters			
			if (i >= 65 && i <= 90) {
				upperCaseLetters.add(i);
			}
			
			// digits
			if (i >= 48 && i <= 57) {
				digits.add(i);				
			}
			
			// special chars
			if (i >= 32 && i <= 47 || i >= 58 && i <= 64 || i >= 91 && i <= 96 || i >= 123 && i <= 126 ) {
				specialChars.add(i);
			}
		}
		
		// add charset collection to arraylist könnte man noch verbessern
		charSetContainer.add(lowerCaseLetters);
		charSetContainer.add(upperCaseLetters);
		charSetContainer.add(digits);
		charSetContainer.add(specialChars);
			
		// append for final charset
		for (int i = 0; i < charSet.length; i++) {
			l.addAll(charSetContainer.get(charSet[i]));
		}

		System.out.println(l.toString());
	
	}
	
	
	private void buildPassword(int[] charSet, int passwordLength) {
		
		prepareCharSets(charSet);
		int max = l.size();
	
	    // Randomise over the ASCII numbers and append respective character values into a StringBuilder
	    for (int i = 0; i <= passwordLength; i++) {
	        randInt = l.get(new SecureRandom().nextInt(max));
	        sb.append((char) randInt);
	    }
	
	    str = sb.toString();
	}
	
	public String generatePassword() {
	    return str;
	}

	

	public static void main(String[] args) {
		PasswordGenerator pwgen = new PasswordGenerator();
		
		/* input for int[] charSet must come from option buttons with options
		 * 0 = lower case letters
		 * 1 = upper case letters
		 * 2 = digits
		 * 3 = special characters
		 * or combinations of them 
		 */
		int[] charSet = {0, 1, 3};
		
		
		// Password length must also come from textfield in gui
		int passwordLength = 25;
		
		pwgen.buildPassword(charSet, passwordLength);
		String pw = pwgen.generatePassword();
		System.out.println(pw);
	}

}

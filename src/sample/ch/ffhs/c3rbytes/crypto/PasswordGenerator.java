package sample.ch.ffhs.c3rbytes.crypto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generates a password of a choosen length and a choosen charset
 */

public class PasswordGenerator {
    private String str;
    private int randInt;
    private StringBuilder sb;
    private List<Integer> l;
    private int passwordLength;

    /**
     * Constructor takes arraylist and stringbuilder
     */
    // Constructor
    public PasswordGenerator() {
        this.l = new ArrayList<>();
        this.sb = new StringBuilder();
    }

    /**
     * This method prepares the desired charset
     *
     * gets an arrayList (see description from method buildPassword(ArrayList<Integer> charSet, int passwordLength))
     * and prepares ASCII Letters the final List consisting of the desired ASCII-Letters of the charset.
     *
     * @param charSet charset from function buildPassowrd
     */
    // prepare desired ASCII CharSet
    private void prepareCharSets(ArrayList<Integer> charSet) {
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

        // add charset collection to arraylist, already improvable
        charSetContainer.add(lowerCaseLetters);
        charSetContainer.add(upperCaseLetters);
        charSetContainer.add(digits);
        charSetContainer.add(specialChars);

        // append for final charset
        for (int i = 0; i < charSet.size(); i++) {
            l.addAll(charSetContainer.get(charSet.get(i)));
            System.out.println(charSet.get(i));
        }
    }


    /**
     * This method builds the desired password
     *
     * @param charSet contains an arraylist with the charsetcodes {@code -->}
     *          * 0 = lower case letters
     *          * 1 = upper case letters
     *          * 2 = digits
     *          * 3 = special characters
     *          * or combinations of them
     * @param passwordLength The length of the passwort (Integer)
     */
    public void buildPassword(ArrayList<Integer> charSet, int passwordLength) {

        prepareCharSets(charSet);
        int max = l.size();

        // Randomise over the ASCII numbers and append respective character values into a StringBuilder
        for (int i = 0; i < passwordLength; i++) {
            randInt = l.get(new SecureRandom().nextInt(max));
            sb.append((char) randInt);
        }

        str = sb.toString();
    }

    /**
     * This method only returns the generated password
     *
     * @return the generated password
     */
    public String generatePassword() {
        return str;
    }

}

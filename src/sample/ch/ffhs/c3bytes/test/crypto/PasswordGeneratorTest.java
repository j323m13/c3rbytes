package sample.ch.ffhs.c3bytes.test.crypto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3bytes.crypto.PasswordGenerator;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PasswordGeneratorTest {

    ArrayList<Integer> charSet = new ArrayList<>();

    @Test
    public void laengentest(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        /* input for int[] charSet must come from option buttons with options
         * 0 = lower case letters
         * 1 = upper case letters
         * 2 = digits
         * 3 = special characters
         * or combinations of them
         */
        //int[] charSet = {0, 1, 3};
        charSet.add(0);
        charSet.add(1);
        charSet.add(2);
        charSet.add(3);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertEquals(pw.length(), passwordLength);
        System.out.println(pw.length());

        clear();

    }

    @Test
    public void char_lower_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(0);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[a-z]+")));

        clear();
    }

    @Test
    public void char_upper_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(1);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[A-Z]+")));

        clear();
    }

    @Test
    public void char_digits_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(2);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[0-9]+")));

        clear();
    }

    @Test
    public void char_specials_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(3);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[\\p{Punct}\\s]+")));

        clear();
    }

    @Test
    public void char_lower_upper_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(0);
        charSet.add(1);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[a-zA-Z]+")));

        clear();
    }

    @Test
    public void char_lower_upper_digits_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(0);
        charSet.add(1);
        charSet.add(2);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[a-zA-Z0-9]+")));

        clear();
    }

    @Test
    public void char_lower_upper_digits_special_test(){
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        charSet.add(0);
        charSet.add(1);
        charSet.add(2);
        charSet.add(3);

        // Password length must also come from textfield in gui
        int passwordLength = 25;

        passwordGenerator.buildPassword(charSet, passwordLength);
        String pw = passwordGenerator.generatePassword();
        assertThat(pw, new MatchesPattern(Pattern.compile("[a-zA-Z0-9\\p{Punct}\\s]+")));

        clear();
    }

    public void clear(){
        charSet.clear();
    }

}

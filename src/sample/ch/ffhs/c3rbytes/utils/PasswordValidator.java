package sample.ch.ffhs.c3rbytes.utils;

/**
 * This class validates if a string is not empty or two Strings are equal
 * @author Olaf Schmidt
 */


public class PasswordValidator {

    /**
     * This method checks whether a String is empty nor it's length is 0
     * @param string String. The string to check
     * @return returns true if is not empty or it' length is 0, false instead.
     */
    public boolean checkFillOut(String string) {
        boolean isFilledOut = false;

        if (!string.equals("") || !(string.length() == 0)){
            isFilledOut = true;
            System.out.println("filled out");
        }

        return isFilledOut;

    }

    /**
     * This method checks whether two strings are equal
     * @param oldString String. The first string
     * @param newString String. The other string
     * @return true if equal, false instead
     */
    public boolean isEqual(String oldString, String newString) {
        boolean isEqual = false;

        if (oldString.equals(newString)){
            isEqual = true;
            System.out.println("New equal");
        }

        return isEqual;
    }

}

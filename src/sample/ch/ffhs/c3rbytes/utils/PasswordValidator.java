package sample.ch.ffhs.c3rbytes.utils;

import javafx.scene.control.Label;

public class PasswordValidator {

    public boolean checkFillOut(String string) {
        boolean isFilledOut = false;

        if (!string.equals("") || !(string.length() == 0)){
            isFilledOut = true;
            System.out.println("filled out");
        }

        return isFilledOut;

    }

    public boolean isEqual(String oldString, String newString) {
        boolean isEqual = false;

        if (oldString.equals(newString)){
            isEqual = true;
            System.out.println("New equal");
        }

        return isEqual;
    }



}

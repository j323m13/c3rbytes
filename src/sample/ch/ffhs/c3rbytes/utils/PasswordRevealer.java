package sample.ch.ffhs.c3rbytes.utils;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordRevealer {

    public void passwordReveal (PasswordField passwordField, TextField textField, boolean passwordHidden) {
        if (passwordHidden){
            String password = passwordField.getText();
            textField.setText(password);
            passwordField.setVisible(false);
            textField.setVisible(true);
        } else {
            String password = textField.getText();
            passwordField.setText(password);
            textField.setVisible(false);
            passwordField.setVisible(true);
        }
    }
}

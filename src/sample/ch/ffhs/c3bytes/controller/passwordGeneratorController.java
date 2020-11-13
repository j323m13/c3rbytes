package sample.ch.ffhs.c3bytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import sample.ch.ffhs.c3bytes.utils.PasswordGenerator;


import java.util.ArrayList;

public class passwordGeneratorController {
    int useDigits;
    int useLower;
    int useUpper;
    int useSymbols;
    int minDigits = 0;
    int minSymbols = 0;

    @FXML private CheckBox digitCheck;
    public void onDigitsAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include digits in password");
    }

    @FXML private CheckBox lowerCaseCheck;
    public void onLowerCaseAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include lowercase in password");
    }

    @FXML private CheckBox upperCaseCheck;
    public void onUpperCaseAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include uppercase in password");
    }

    @FXML private TextField minimumDigits;
    public void minimumDigitsAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("minimum digits in password: ");
    }

    @FXML private Label lengthTextField;
    @FXML private Slider lengthSlider;
    public void lengthSliderAction(MouseEvent mouseEvent) {
        lengthSlider.valueProperty().addListener((observable, oldValue, newValue ) -> {
            lengthTextField.setText(Integer.toString(newValue.intValue()));
        });
    }

    @FXML private CheckBox symbolCheck;
    public void onSymbolAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include special characters in password");
    }

    @FXML private TextField minimumSpecialCharacters;
    public void minimumSpecialAction(ActionEvent actionEvent) {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("minimum special characters in password: ");
    }

    // Generate Password
    @FXML private TextField pwdOutputField;
    public void onGenerateAction(ActionEvent actionEvent) {

        /* input for ArrayList<Integer> charSet must come from option buttons with options
         * 0 = lower case letters
         * 1 = upper case letters
         * 2 = digits
         * 3 = special characters
         * or combinations of them
        */
        ArrayList<Integer> charSet = new ArrayList<>();

        //int pwlength = lengthSlider.getValue().int;
        int pwlength = Integer.parseInt(lengthTextField.getText());

        if (lowerCaseCheck.isSelected()) {
            useLower = 0;
            charSet.add(useLower);
        }

        if (upperCaseCheck.isSelected()) {
            useUpper = 1;
            charSet.add(useUpper);
        }

        if (digitCheck.isSelected()){
            useDigits = 2;
            charSet.add(useDigits);
        }

       if (symbolCheck.isSelected()){
            useSymbols = 3;
            charSet.add(useSymbols);
        }

       System.out.println(charSet.toString());

        //TODO make minimum digits and symbols into sliders to prevent null and non-digit entries
        minDigits = Integer.parseInt(minimumDigits.getText());
        minSymbols = Integer.parseInt(minimumSpecialCharacters.getText());

        System.out.println("Generating password");
        //TODO: call algorithm and enter in pwdOutputField.setText


        PasswordGenerator pwg = new PasswordGenerator();
        pwg.buildPassword(charSet, pwlength);
        String pw = pwg.generatePassword();

        System.out.println(pw);

        //pwdOutputField.setText((int)lengthSlider.getValue() + " " + useDigits +" "+ useLower +" "+ useUpper +" "+ useSymbols +" "+ minDigits +" "+ minSymbols);
        pwdOutputField.setText(pw);

        pwg = null;
    }

    public void onCopyAction(ActionEvent actionEvent) {
        //TODO: Copy contents of pwdOutputField.
        System.out.println("Copying password");
    }

    @FXML
    private javafx.scene.control.Button discardPassword;
    public void discardPasswordAction(ActionEvent actionEvent) {
        System.out.println("Discarding Password");
        //TODO: Discard information and close the window window.
        Stage stage = (Stage) discardPassword.getScene().getWindow();
        stage.close();
    }

    @FXML
    private javafx.scene.control.Button savePassword;
    public void savePasswordAction(ActionEvent actionEvent) {
        //TODO: Add necessary method and data transfer before closing the window.
        Stage stage = (Stage) savePassword.getScene().getWindow();
        stage.close();
    }
}

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

public class passwordGeneratorController {
    boolean useDigits = false;
    boolean useLower = false;
    boolean useUpper = false;
    boolean useSymbols = false;
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

    @FXML private TextField pwdOutputField;
    public void onGenerateAction(ActionEvent actionEvent) {
        if (digitCheck.isSelected()){
            useDigits = true;
        }
        if (lowerCaseCheck.isSelected()){
            useLower = true;
        }
        if (upperCaseCheck.isSelected()){
            useUpper = true;
        }
        if (digitCheck.isSelected()){
            useSymbols = true;
        }

        //TODO make minimum digits and symbols into sliders to prevent null and non-digit entries
        minDigits = Integer.parseInt(minimumDigits.getText());
        minSymbols = Integer.parseInt(minimumSpecialCharacters.getText());

        System.out.println("Generating password");
        //TODO: call algorithm and enter in pwdOutputField.setText
        pwdOutputField.setText((int)lengthSlider.getValue() + " " + useDigits +" "+ useLower +" "+ useUpper +" "+ useSymbols +" "+ minDigits +" "+ minSymbols);
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

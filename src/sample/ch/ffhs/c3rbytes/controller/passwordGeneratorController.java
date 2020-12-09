package sample.ch.ffhs.c3rbytes.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.PasswordGenerator;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class passwordGeneratorController implements IController {
    int useDigits;
    int useLower;
    int useUpper;
    int useSymbols;
    FXMLLoader loader = null;

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

        System.out.println("Generating password");
        //TODO: call algorithm and enter in pwdOutputField.setText


        PasswordGenerator pwg = new PasswordGenerator();
        pwg.buildPassword(charSet, pwlength);
        String pw = pwg.generatePassword();

        System.out.println(pw);

        //pwdOutputField.setText((int)lengthSlider.getValue() + " " + useDigits +" "+ useLower +" "+ useUpper +" "+ useSymbols);
        pwdOutputField.setText(pw);

        pwg = null;
    }

    public void onCopyAction(ActionEvent actionEvent) {
        //TODO: Copy contents of pwdOutputField.
        System.out.println("Copying password");
        ClipboardHandler clipboardHandler = new ClipboardHandler();
        String pwdOutputFieldText = pwdOutputField.getText();
        clipboardHandler.copyPasswordToClipboard(pwdOutputFieldText);

    }

    @FXML
    private javafx.scene.control.Button discardPassword;
    public void discardPasswordAction(ActionEvent actionEvent) {
        System.out.println("Discarding Password");
        //TODO: Discard information and close the window window.
        Stage stage = (Stage) discardPassword.getScene().getWindow();
        closeStage(stage);
    }

    @FXML
    private javafx.scene.control.Button savePassword;
    @FXML PasswordField passwordField;
    @FXML TextField passwordTextField;
    public void savePasswordAction(ActionEvent actionEvent) throws IOException {
        //TODO: Add necessary method and data transfer before closing the window.
        String pwdOutputFieldText = pwdOutputField.getText();
        passwordField.setText(pwdOutputFieldText);
        passwordTextField.setText(passwordField.getText());
        Stage stage = (Stage) savePassword.getScene().getWindow();
        closeStage(stage);
    }

    public void getpwdOutputTextField(PasswordField passwordField, TextField pwTextField){
        this.passwordField = passwordField;
        this.passwordTextField = pwTextField;
    }

    public void closeStage(Stage stage){
        stage.close();
    }

    @Override
    public void getView(Stage stage) throws IOException {
        URL url = getClass().getClassLoader().getResource("password_generator_view.fxml");
        loader = new FXMLLoader(url);
        Parent passwordGenerator = loader.load();
        //stage stage = new Stage();
        stage.setTitle("Password Generator");
        stage.getIcons().add(new Image("logo3.png"));
        stage.setScene(new Scene(passwordGenerator, 552, 420));
    }

    @Override
    public Object getController() throws IOException {
        return loader.getController();
    }



    public void lengthSliderMouseRelease(MouseEvent mouseEvent) {
        lengthTextField.setText(Integer.toString(lengthSlider.valueProperty().intValue()));
    }
}


package sample.ch.ffhs.c3rbytes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import sample.ch.ffhs.c3rbytes.crypto.PasswordGenerator;
import sample.ch.ffhs.c3rbytes.utils.ClipboardHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is the password generator controller.
 */

public class passwordGeneratorController implements IController {

    @FXML private CheckBox digitCheck;
    @FXML private CheckBox lowerCaseCheck;
    @FXML private CheckBox upperCaseCheck;
    @FXML private Label lengthTextField;
    @FXML private Slider lengthSlider;
    @FXML private CheckBox symbolCheck;
    @FXML private TextField pwdOutputField;
    @FXML private javafx.scene.control.Button savePassword;
    @FXML PasswordField passwordField;
    @FXML TextField passwordTextField;
    @FXML private javafx.scene.control.Button discardPassword;

    int useDigits;
    int useLower;
    int useUpper;
    int useSymbols;
    FXMLLoader loader = null;


    public void onDigitsAction() {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include digits in password");
    }

    public void onLowerCaseAction() {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include lowercase in password");
    }

    public void onUpperCaseAction() {
        //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
        System.out.println("Include uppercase in password");
    }

    public void lengthSliderAction() {
        lengthSlider.valueProperty().addListener((observable, oldValue, newValue ) -> {
            lengthTextField.setText(Integer.toString(newValue.intValue()));
        });
    }

    public void onSymbolAction() {
    //This field does not do anything by itself, it is referenced when creating a password in onGenerateAction
    System.out.println("Include special characters in password");
    }

    /**
     * This method generates a new password with the desired length and the desired charset
     *         /* input for ArrayList{@code <Integer>}  charSet must come from option buttons with options
     *          * 0 = lower case letters
     *          * 1 = upper case letters
     *          * 2 = digits
     *          * 3 = special characters
     *          * or combinations of them
     */
    public void onGenerateAction() {


        ArrayList<Integer> charSet = new ArrayList<>();

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

        PasswordGenerator pwg = new PasswordGenerator();
        pwg.buildPassword(charSet, pwlength);
        String pw = pwg.generatePassword();

        System.out.println(pw);

        pwdOutputField.setText(pw);

    }

    /**
     * This method copies the password to the clipboard
     *
     */
    public void onCopyAction() {
        System.out.println("Copying password");
        ClipboardHandler clipboardHandler = new ClipboardHandler();
        String pwdOutputFieldText = pwdOutputField.getText();
        clipboardHandler.copyPasswordToClipboard(pwdOutputFieldText);

    }


    public void discardPasswordAction() {
        System.out.println("Discarding Password");
        Stage stage = (Stage) discardPassword.getScene().getWindow();
        closeStage(stage);
    }

    /**
     * This method the password to the addNewItemView
     */
    public void savePasswordAction() {
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

    /**
     * This method manages key inputs
     * @param keyEvent The key that was used
     */
    public void manageKeyInput(KeyEvent keyEvent) {
        System.out.println("Key released");
        if (keyEvent.getCode().equals(KeyCode.TAB)){
            return;
        }

        if (isEnterKey(keyEvent)) {
            focusNext();
        }

    }

    /**
     * This method checks if key input is Enter
     * @param keyEvent The key that was used
     * @return true if Enter, false instead
     */
    private boolean isEnterKey(KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            System.out.println("Enter");
            return true;
        }
        return false;
    }

    /**
     * This method focuses the next element
     */
    private void focusNext(){
        Robot robot = new Robot();
        robot.keyPress(KeyCode.TAB);
        robot.keyRelease(KeyCode.TAB);
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

    public void lengthSliderMouseRelease() {
        lengthTextField.setText(Integer.toString(lengthSlider.valueProperty().intValue()));
    }
}


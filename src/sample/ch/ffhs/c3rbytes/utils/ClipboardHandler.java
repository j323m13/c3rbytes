package sample.ch.ffhs.c3rbytes.utils;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class handles the text to copy to clipboard and deletes it's content after a given time
 *  @author Olaf Schmidt
 */

public class ClipboardHandler{
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    /**
     * This method copies a text to the systemclipboard
     *
     * @param password the password to copy to clipboard
     */
    public void copyPasswordToClipboard(String password) {
        content.putString(password);
        String clipboardContent = content.getString();
        System.out.println("clipboardContent: " + clipboardContent);
        clipboard.setContent(content);
        setTimer(9000,1000);
    }

    /**
     * This method clears the content of the systemclipboard
     * @param timer The timer to be cancelled
     */
    public void clearClipboard(Timer timer) {
        clipboard.clear();

        String clipboardContent1 = content.getString();
        System.out.println("clipboardContent 1:" +clipboardContent1);
        content.clear();
        String clipboardContent2 = content.getString();
        System.out.println("Content :" +clipboardContent2);
        timer.cancel();
    }

    /**
     * This method is a timer.
     * @param delay int. the delay that the function clearclipboard has to be excecuted
     * @param period int. after this
     */
    private void setTimer(int delay, int period){
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> clearClipboard(timer));
            }
        };
        //timer.schedule(task, delay, period);
        timer.schedule(task,delay);
    }
}

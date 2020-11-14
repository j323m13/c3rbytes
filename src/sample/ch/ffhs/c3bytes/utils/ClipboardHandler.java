package sample.ch.ffhs.c3bytes.utils;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Timer;
import java.util.TimerTask;

public class ClipboardHandler{
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    public void copyPasswordToClipboard(String password) {
        content.putString(password);
        String clipboardContent = content.getString();
        System.out.println("clipboardContent: " + clipboardContent);
        clipboard.setContent(content);
        setTimer(5000,1000);
    }

    public void clearClipboard(Timer timer) {
        clipboard.clear();
        String clipboardContent1 = content.getString();
        System.out.println("clipboardContent 1:" +clipboardContent1);
        timer.cancel();
    }

    private void setTimer(int delay, int period){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> clearClipboard(timer));
            }
        };
        timer.schedule(task, delay, period);
    }
}

package sample.ch.ffhs.c3rbytes.utils;

public class UrlOpener {

    String url = null;
    Runtime runtime = null;
    String getSystem = null;

    public UrlOpener(){
        runtime = Runtime.getRuntime();
        getSystem = System.getProperty("os.name");
    }

    public void openURL(String domain){

        url = domain;

        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

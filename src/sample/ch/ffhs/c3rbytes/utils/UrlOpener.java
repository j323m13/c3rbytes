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

        System.out.println("url: " + url);

        try {
            if (isWindows(getSystem)) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url).waitFor();

            } else if (isMac(getSystem)) {
                String[] cmd = {"open", url};
                runtime.exec(cmd).waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWindows(String os){
        return os.contains("Win") || os.contains("win");
    }

    public boolean isMac(String os){
        return os.contains("mac") || os.contains("Mac");
    }
}

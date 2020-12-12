package sample.ch.ffhs.c3rbytes.utils;

/**
 * This class opens a url in the default browser
 * @author Olaf Schmidt
 */

public class UrlOpener {

    String url = null;
    Runtime runtime;
    String getSystem;

    /**
     * Constructor
     * Gets the runtime and systemproberty
     */
    public UrlOpener(){
        runtime = Runtime.getRuntime();
        getSystem = System.getProperty("os.name");
    }

    /**
     * This method opens a well formed url in the default browser
     * @param domain String. The url to open in the browser
     */
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

    /**
     * This method checks whether the os is windows or not
     * @param os String. The operation system
     * @return returns true if it is windows, false instead
     */
    public boolean isWindows(String os){
        return os.contains("Win") || os.contains("win");
    }

    /**
     * This method checks whether the os is mac_os or not
     * @param os String. The operation system
     * @return returns true if it is mac_os, false instead
     */
    public boolean isMac(String os){
        return os.contains("mac") || os.contains("Mac");
    }
}

/* Opens url in default web-browser
 *  
 */

public class Browser_Util 
{
	
	String url = null;
	Runtime runtime = null;
	String getSystem = null;
			
	
	public Browser_Util() 
	{
		runtime = Runtime.getRuntime();
		getSystem = System.getProperty("os.name");
	}
	
	public void openURL(String domain)
	{
	    url = "https://" + domain;

	    try {
	        if (isWindows(getSystem)) {
	            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url).waitFor();

	        } else if (isMac(getSystem)) {
	            String[] cmd = {"open", url};
	            runtime.exec(cmd).waitFor();

	        } else if (isUnix(getSystem)) {
	            String[] cmd = {"xdg-open", url};
	            runtime.exec(cmd).waitFor();

	        } else {
	            try {
	                throw new IllegalStateException();
	            } catch (IllegalStateException e1) {

	                e1.printStackTrace();
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public boolean isWindows(String os)
	{
	    return os.contains("Win") || os.contains("win");
	}

	public boolean isMac(String os)
	{
	    return os.contains("mac") || os.contains("Mac");
	}

	public boolean isUnix(String os)
	{
	    return os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0;
	}
	

	public static void main(String[] args) 
	{
		Browser_Util bu = new Browser_Util();
		bu.openURL("www.blick.ch");
	
	}
	
	
	
	
}

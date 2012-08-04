import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class MoodleAuth {
	
	private final String loginURL = "http://csemoodle.ucsd.edu/login/index.php";
	private final String mainURL = "http://csemoodle.ucsd.edu/";
	
	public MoodleAuth(){
		//nothing here
	}
	
	public boolean SO2Test(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		// First set the default cookie manager.
		//CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		
		CookieHandler.setDefault(new CookieManager());

		// Gather all cookies on the first request.
		URLConnection connection;
		try {
			connection = new URL(loginURL).openConnection();
			
			getHTML(connection); //doing this for init cookies
			
			
			//post data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
		    
		    //reset?
		    connection = new URL(loginURL).openConnection();
		    
		    //send data
		    connection.setDoOutput(true);
		    connection.setUseCaches(false);
		    connection.getOutputStream().write(data.getBytes("UTF-8"));
		    
		    
		    System.out.println(getHTML(connection));
		    
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public boolean SOTest(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		// Gather all cookies on the first request.
		URLConnection connection;
		try {
			connection = new URL(loginURL).openConnection();
			
			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
			// ...

			// Then use the same cookies on all subsequent requests.
			connection = new URL(loginURL).openConnection();
			for (String cookie : cookies) {
			    connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			    System.err.println(cookie);
			}
			
			//post data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
		    
		    //send data
		    connection.setDoOutput(true);
		    connection.setUseCaches(false);
		    connection.getOutputStream().write(data.getBytes("UTF-8"));
		    
		    List<String> cookies2 = connection.getHeaderFields().get("Set-Cookie");
		    System.err.println("take 2");
			connection = new URL(mainURL).openConnection();
			for (String cookie : cookies) {
			    connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			    System.err.println(cookie);
			}
			System.err.println("new cookies");
			for (String cookie : cookies2) {
			    connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
			    System.err.println(cookie);
			}
		    
		    System.out.println(getHTML(connection));
		    
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public boolean httpTest(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		//post data
		String data = null;
		try {
			data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(loginURL).openConnection();
			
		    con.setDoOutput(true); //triggers post
		    con.setUseCaches(false);
			
			//con.setRequestMethod("POST");
			
			System.err.println(data);
			
			con.getOutputStream().write(data.getBytes("UTF-8"));
			
			System.out.println(getHTML(con));
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public boolean cookieAuth(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		try {
			//get the cookies
			URL url = new URL(loginURL);
			url.openConnection();
			URLConnection conn1 = url.openConnection();
		    conn1.setDoOutput(true);
		    conn1.setUseCaches(false);
		    
			//settup second connection to set the cookies
			URLConnection conn2 = url.openConnection();
			
			HashMap<String, String> cookies = updateCookies(conn1, null);
		    conn2.setRequestProperty("Cookie", cookiesToString(cookies)); 
		    System.out.println(cookiesToString(cookies));
		    
		    String preAuthCookie = cookies.get("MoodleSession");
			
			//post data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

		    //send data
		    conn2.setDoOutput(true);
		    conn2.setUseCaches(false);
		    conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    DataOutputStream wr = new DataOutputStream(conn2.getOutputStream());
		    wr.writeBytes(data);
		    wr.flush();
		    wr.close();
		    
		    updateCookies(conn2, cookies);
		    System.out.println(cookiesToString(cookies));
		    
		    if (!(preAuthCookie.equals(cookies.get("MoodleSession")))){
		    	return true;
		    }
		    
		    
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not url-encode users password");
		} catch (MalformedURLException e) {
			System.err.println("Invalid Auth URL");
		} catch (IOException e) {
			System.err.println("Unale to connect to server");
		}
		return false;
		
	}
	
	
	
	public boolean cookieAuth2(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		// First set the default cookie manager.
		//CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		
		try {
			//get the cookies
			URL url = new URL(loginURL);
			url.openConnection();
			URLConnection conn1 = url.openConnection();
		    conn1.setDoOutput(true);
		    conn1.setUseCaches(false);
		    
			//settup second connection to set the cookies
			URLConnection conn2 = url.openConnection();
			
			HashMap<String, String> cookies = updateCookies(conn1, null);
		    //conn2.setRequestProperty("Cookie", cookiesToString(cookies)); 
		    System.out.println(cookiesToString(cookies));
		    
		    //String preAuthCookie = cookies.get("MoodleSession");
			
			//post data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

		    //send data
		    conn2.setDoOutput(true);
		    conn2.setUseCaches(false);
		    //conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    DataOutputStream wr = new DataOutputStream(conn2.getOutputStream());
		    wr.writeBytes(data);
		    wr.flush();
		    wr.close();
		    
		    cookies = updateCookies(conn2, null);
		    System.out.println(cookiesToString(cookies));
		    
		    //if (!(preAuthCookie.equals(cookies.get("MoodleSession")))){
		    	//return true;
		    //}
		    
		    
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not url-encode users password");
		} catch (MalformedURLException e) {
			System.err.println("Invalid Auth URL");
		} catch (IOException e) {
			System.err.println("Unale to connect to server");
		}
		return false;
		
	}
	
	
	public boolean testCredentials(String username, String password){
		if (username.length() == 0 || password.length() == 0){
			return false;
		}
		
		try {
			//get the cookies
			URL url = new URL(loginURL);
			url.openConnection();
			URLConnection conn1 = url.openConnection();
		    conn1.setDoOutput(true);
		    conn1.setUseCaches(false);
		    
			//settup second connection to set the cookies
			URLConnection conn2 = url.openConnection();
			
			
			HashMap<String, String> cookies = updateCookies(conn1, null);
			conn2.setRequestProperty("Cookie", cookiesToString(cookies));
		    
		    System.out.println("A");
		    System.out.println(cookiesToString(cookies));
			
			//post data
			String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		    data += "&" + URLEncoder.encode("testcookies", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

		    //send data
		    //URL url = new URL(loginURL);
		    conn2.setDoOutput(true);
		    conn2.setUseCaches(false);
		    conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    
		    //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    //wr.write(data);
		    DataOutputStream wr = new DataOutputStream(conn2.getOutputStream());
		    wr.writeBytes(data);
		    wr.flush();
		    wr.close();
		    
		    
		    URL url2 = new URL(mainURL);
		    URLConnection conn3 = url2.openConnection();
		    updateCookies(conn2, cookies);

		    conn3.setRequestProperty("Content-Type", "text/html,application/xhtml+xml");
		    conn3.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2");

		    conn3.setRequestProperty("Cookie", cookiesToString(cookies));
		    System.out.println("B");
		    System.out.println(cookiesToString(cookies));
		    
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn3.getInputStream()));
		    
		    updateCookies(conn3, cookies);
		    System.out.println("C");
		    System.out.println(cookiesToString(cookies));
		    
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	//TODO process data
		        System.out.println(line);
		    }
		    rd.close();
		    

		    
		    
		    
		} catch (UnsupportedEncodingException e) {
			System.err.println("Could not url-encode users password");
		} catch (MalformedURLException e) {
			System.err.println("Invalid Auth URL");
		} catch (IOException e) {
			System.err.println("Unale to connect to server");
		}
		return false;
	}
	
	private String getHTML(URLConnection conn){
	    //get response
	    //BufferedReader rd = new BufferedReader
	    try {
			return convertStreamToString(conn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
			//e.printStackTrace();
		}
		
	}
	
	private String convertStreamToString(InputStream is) {
	    try {
	        return new Scanner(is).useDelimiter("\\A").next();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
	
	private String cookiesToString(HashMap<String, String> cookies){
		StringBuilder out = new StringBuilder();
		for (Map.Entry<String, String> entry : cookies.entrySet())
		{
		    out.append(entry.getKey() + "=" + entry.getValue()+"; ");
		}
		//todo rm
		//System.out.println(out.toString());
		return out.toString();
	}
	
	private HashMap<String, String> updateCookies(URLConnection conn, HashMap<String, String> cookies){
	    String headerName;
	    if (cookies == null){
	    	cookies  = new HashMap<String,String>();
	    }
	    // checking for each headers
	    for (int i=1; (headerName = conn.getHeaderFieldKey(i))!=null; i++) {
	        // if its set-cookie, then take it
	    	/*
	    	System.out.println("=========================");
	    	System.out.println(conn.getHeaderField(i));
	    	System.out.println("=========================");
	    	 */
	        if (headerName.equalsIgnoreCase("Set-Cookie")) {   
	            String cookie = conn.getHeaderField(i);
	            cookie = cookie.substring(0, cookie.indexOf(";"));
	            //System.out.println(cookie);
	            //conn2.setRequestProperty("Cookie", cookie);
	            int split = cookie.indexOf("=");
	            cookies.put(cookie.substring(0, split), cookie.substring(split+1));
	            //returnCookie += cookie + " ";
	        }
	    }
	    //System.out.println(cookies);
	    return cookies;
		
	}
	
	
	
}

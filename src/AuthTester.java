import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class AuthTester {
	public static void main(String[] args){
		String user = getResponse("User");
		String pass = getResponse("Pass");
		MoodleAuth login = new MoodleAuth();
		
		//System.out.println(login.httpTest(user, pass));
		
		
		//System.out.println(login.SOTest(user, pass));
		
		
		 //does not pass all the required cookies on post
		//uses cookie manager
		System.out.println(login.SO2Test(user, pass));
		
		
		//System.out.println(login.cookieAuth(user, pass));
		//System.out.println(login.cookieAuth2(user, pass));
		//System.out.println(login.testCredentials(user, pass));

	}
	
	
	public static String getResponse(String question){
	      System.out.print(question+": ");

	      //  open up standard input
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	      String userName = null;

	      //  read the username from the command-line; need to use try/catch with the
	      //  readLine() method
	      try {
	         userName = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read data");
	         System.exit(1);
	      }
	      
	      return userName;


		
	}
}

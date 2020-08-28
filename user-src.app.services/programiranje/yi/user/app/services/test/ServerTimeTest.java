package programiranje.yi.user.app.services.test;


import programiranje.yi.user.app.services.client.ServerTimeClient;
import programiranje.yi.user.app.services.client.SessionClient;

/**
 * Тестиорање серверског времена, симулацијом клијента. 
 * @author mirko
 * @version 1.0
 */
public class ServerTimeTest {
	private static SessionClient session = new SessionClient(); 
	private static ServerTimeClient client = new ServerTimeClient().setSessionClient(session).tryReinit(); 
	
	public static void main(String ... args) {
		session.getHttpClient();
		System.out.println("Lokalno vrijeme : "  +client.covnvertClient());
		System.out.println("Serversko vrijeme : "+client.covnvertServer());
	}
}

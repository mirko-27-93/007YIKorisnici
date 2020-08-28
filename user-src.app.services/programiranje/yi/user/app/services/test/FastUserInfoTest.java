package programiranje.yi.user.app.services.test;

import java.util.Scanner;

import programiranje.yi.user.app.services.client.FastUserInfoClient;
import programiranje.yi.user.app.services.client.SessionClient;
import programiranje.yi.user.app.services.model.ShortCredentialsInformation;

/**
 * Тестирање сервиса за брзе информације о серверу и кориснику. 
 * @author mirko
 * @version 1.0
 */
public class FastUserInfoTest {
	private static Scanner scanner = new Scanner(System.in);
	private static SessionClient session = new SessionClient(); 
	private static FastUserInfoClient fuic = new FastUserInfoClient().setSessionClient(session);
	
	public static void main(String ... args) {
		try {
			System.out.print("Korisnicko ime : "); 
			String username = scanner.nextLine(); 
			System.out.println("==================");
			ShortCredentialsInformation info = fuic.get(username); 
			System.out.println("Korisnicko ime : "+info.getUsername());
			System.out.println("Baza podataka : "+info.getDatabase());
			System.out.println("Zapis lozinke : "+info.getUserPassword());
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Greska.");
		}
	}
}

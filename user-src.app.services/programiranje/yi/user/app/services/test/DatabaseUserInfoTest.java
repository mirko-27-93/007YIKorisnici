package programiranje.yi.user.app.services.test;

import java.util.Scanner;

import programiranje.yi.user.app.services.client.DatabaseConfigurationClient;
import programiranje.yi.user.app.services.client.SessionClient;
import programiranje.yi.user.app.services.model.DatabaseUserInformation;
import programiranje.yi.user.app.services.model.SessionInformation;

/**
 * Тестирање корисничких информација при бази података 
 * и о бази података. 
 * @author mirko
 * @version 1.0
 */
public class DatabaseUserInfoTest {
	private static Scanner scanner = new Scanner(System.in);
	private static SessionClient session = new SessionClient(); 
	private static DatabaseConfigurationClient dcc = new DatabaseConfigurationClient().setSessionClient(session);
	
	public static void main(String ... args) {
		try {
			System.out.print("Korisnicko ime : "); 
			String username = scanner.nextLine(); 
			System.out.print("Lozinka : ");
			String password = scanner.nextLine(); 
			System.out.println("==================");
			SessionInformation si = new SessionInformation(); 
			si.setUsername(username);
			session.login(si, password);
			DatabaseUserInformation info = dcc.getAllInfo(); 
			System.out.println("Korisnicko ime : "+info.getUsername());
			System.out.println("Baza podataka : "+info.getDatabase());
			System.out.println("BAP Korisnik : "+info.isDbInfoExists());
			System.out.println("YI Korisik : "+info.isDbUserExists());
			System.out.println("Procedure : "+info.getProcedures());
			System.out.println("Tabele : "+info.getTables()); 
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Greska.");
		}
	}
}

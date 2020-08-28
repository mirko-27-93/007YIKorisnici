package programiranje.yi.user.app.services.test;

import java.util.Scanner;

import programiranje.yi.user.app.services.client.SessionClient;
import programiranje.yi.user.app.services.client.UserInfoClient;
import programiranje.yi.user.app.services.model.SessionInformation;
import programiranje.yi.user.app.services.model.UserInformationModel;

/**
 * Тестирање корисничких података. 
 * @author mirko
 * @version 1.0
 */
public class UserInfoTest {
	private static Scanner scanner = new Scanner(System.in);
	private static SessionClient session = new SessionClient(); 
	private static UserInfoClient uic = new UserInfoClient().setSessionClient(session); 
	
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
			UserInformationModel info = uic.get(); 
			System.out.println("Korisnicko ime : "+info.getUsername());
			System.out.println(); 
			System.out.println("Ime : "+info.getFirstName()); 
			System.out.println("Prezime : "+info.getSecondName()); 
			System.out.println("Posta : "+info.getEmail());
			System.out.println();
			System.out.println("HAHSH PASSWORD : "+info.getPasswordRecord());
			System.out.println("\n\tOPIS");
			System.out.println(info.getDescription()); 
			System.out.println();
			System.out.println("\n\tADRESA \n"+info.getAddress());
			System.out.println();
			System.out.println("Telefon : "+info.getTelephone());
			System.out.println("Drzava : "+info.getCountry().getCountry());
			System.out.println();
			System.out.println("USER IMAGE :"+info.getUserImage());
			System.out.println("PROFILE IMAGE :"+info.getProfileImage());
			System.out.println("COUNTRY FLAG IMAGE :"+info.getCountry().getCountryFlagImage());
			System.out.println(); 
			System.out.println("COUNTRY A2C : "+info.getCountry().getA2c()); 
			System.out.println("COUNTRY A3C : "+info.getCountry().getA3c());
			System.out.println(); 
			System.out.println("CALL CODES : "+info.getCountry().getCcs());
			System.out.println("TOP LEVEL DOMAINS : "+info.getCountry().getTlds()); 
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Greska.");
		}
	}
}

package programiranje.yi.user.app.services.test;

import java.util.Scanner;

import programiranje.yi.user.app.services.client.MessageConfigurationClient;
import programiranje.yi.user.app.services.client.SessionClient;
import programiranje.yi.user.app.services.model.SessionInformation;
import programiranje.yi.user.app.services.model.UserMessageInformation;

/**
 * Тестирање информација о порукама. 
 * @author mirko
 * @version 1.0
 */
public class MessageUserInfoTest {
	private static Scanner scanner = new Scanner(System.in);
	private static SessionClient session = new SessionClient(); 
	private static MessageConfigurationClient mcc = new MessageConfigurationClient().setSessionClient(session);
	
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
			UserMessageInformation umi = mcc.getConfigurationsInfo();
			System.out.println("Korisnicko ime : "+umi.getUser());
			System.out.println("Email message : "+umi.isEmailMessages());
			System.out.println("Web message : "+umi.isWebMessages());
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Greska.");
		}
	}
}

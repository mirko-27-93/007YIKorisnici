package programiranje.yi.user.app.services.test;

import java.util.Scanner;

import programiranje.yi.user.app.services.client.SessionClient;
import programiranje.yi.user.app.services.model.SessionInformation;

/**
 * Клијентска страна, тест за успостављање сесија преко сервиса. 
 * @author mirko
 * @version 1.0
 */
public class SessionServiceTest {
	public static Scanner scanner = new Scanner(System.in);
	
	private static SessionInformation infos = new SessionInformation();
	private static SessionClient client = new SessionClient(); 
	
	public static SessionInformation getInfos() {
		return infos;
	}

	public static void setInfos(SessionInformation infos) {
		SessionServiceTest.infos = infos;
	}

	public static SessionClient getClient() {
		return client;
	}

	public static void setClient(SessionClient client) {
		SessionServiceTest.client = client;
	}

	public static String input(String header) {
		System.out.print(header);
		return scanner.nextLine(); 
	}
	
	public static void menu() {
		System.out.println();
		System.out.println("0. Izlaz");
		System.out.println("1. Prijava");
		System.out.println("2. Odjava");
		System.out.println("3. Pregled");
		System.out.println(); 
	}
	
	public static void main(String ... args) {
		System.out.println("POZDRAV");
		while(true) {
			menu(); 
			int izbor = -1;
			try {izbor = Integer.parseInt(input("IZBOR : ")); }
			catch(Exception ex) { izbor = -1;  }
			
			if(izbor == 0) break;
			System.out.println(); 
			switch(izbor) {
				case 1: 
					System.out.println("PRIJAVA");
					login(); 
					break; 
				case 2: 
					System.out.println("ODJAVA");
					logout(); 
					break; 
				case 3: 
					System.out.println("PREGLED"); 
					preview(); 
					break;
				 default: 
					 System.out.println("POGRESAN IZBOR");
			}
		}
		System.out.println("ZDRAVO");
	}
	
	public static void login() {
		try {
			if(client.isItLogged()) 
				throw new RuntimeException("LOGGED IN"); 
			String username = input("Korisnicko ime : ");
			String password = input("Lozinka : ");
			
			infos.setUsername(username);
			client.login(infos, password);
			System.out.println("Korisnik je ulogovan.");
		}catch(Exception ex) {
			infos.setUsername("");
			System.out.println("Greska pri logovanju na sesiju.");
			String message = ex.getMessage(); 
			if(message!=null && message.trim().length()!=0)
				System.out.println(ex.getMessage());
		}
	}
	
	public static void logout() {
		try {
			if(client.isNotLogged()) {
				throw new NullPointerException("LOGGED OUT"); 
			}
			client.logout();
			System.out.println("Odjava uspjesna"); 
		}catch(Exception ex) {
			System.out.println("Greska pri odjavi sa sesije.");
			String message = ex.getMessage(); 
			if(message!=null && message.trim().length()!=0)
				System.out.println(ex.getMessage());
		}
	}
	
	public static void preview() {
		try {
			if(client.isNotLogged()) {
				throw new NullPointerException("LOGGED OUT"); 
			}
			SessionInformation info = client.getSessionInfo();
			System.out.println("ID : "+info.getId());
			System.out.println("USERNAME : "+info.getUsername());
			System.out.println("DATABASE : "+info.getDatabase());
			System.out.println("ENGINE : "+info.getEngine());
			System.out.println("SERVICE : "+info.getService());
			System.out.println("TYPE : "+ info.getType());
			System.out.println("FILE : "+ info.getFile()); 
		}catch(Exception ex) {
			System.out.println("Greska pri pregledu o sesiji.");
			String message = ex.getMessage(); 
			if(message!=null && message.trim().length()!=0)
				System.out.println(ex.getMessage());
		}
	}
}

package programiranje.yi.database.test;

import java.util.Scanner;

import programiranje.yi.database.MySQLUserDAO;
import programiranje.yi.database.MySQLUserDTO;
import studiranje.ip.data.RootConnectionPool;

/**
 * Тестирање операција са корисничким профилима. 
 * @author mirko
 * @version 1.0
 */
public class MySQLUserDAOTestOperation {
	public static final Scanner scanner = new Scanner(System.in); 
	public static RootConnectionPool pool = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
	public static MySQLUserDAO dao = new MySQLUserDAO(pool);
	public static final boolean ERROR_REMIX = true;
	
	public static String input(String header) {
		System.out.print(header);
		return scanner.nextLine(); 
	}
	
	public static void menu() {
		System.out.println(); 
		System.out.println("MENI : ");
		System.out.println("0. Izlaz");
		System.out.println("1. Unos");
		System.out.println("2. Brisanje");
		System.out.println("3. Pregled");
	}
	
	public static void main(String ... args) {
		System.out.println("POZDRAV");
		while(true) {
			menu(); 
			int izbor = -1;
			try{ izbor = Integer.parseInt(input("IZBOR : "));}
			catch(Exception ex) {izbor = -1;}
			System.out.println();
			
			if(izbor == 0) break;
			switch(izbor) {
				case 1: 
					System.out.println("Unos korisnickog profila");
					insert(); 
					break;
				case 2: 
					System.out.println("Brisanje korisnickog profila"); 
					delete(); 
					break;
				case 3: 
					System.out.println("Lista korisnika koji imaju profil baze podataka"); 
					list(); 
					break; 
				default: 
					System.out.println("Pogresan izbor"); 
					break; 
			}
			
			System.out.println("ZDRAVO"); 
		}
	}
	
	public static void insert() {
		String username = input("Korisnicko ime : ");
		boolean databaseUser = true; 
		MySQLUserDTO dto = new MySQLUserDTO();
		dto.setUsername(username);
		dto.setDatabaseUser(databaseUser);
		try{dao.put(dto);}
		catch(Exception ex) {
			System.out.println("Greska pri unosu.");
			if(ERROR_REMIX) ex.printStackTrace(System.out);
		}
		System.out.println("Unos zavrsen.");
	}
	
	public static void delete() {
		String username = input("Korisnicko ime : "); 
		try{dao.clean(username);}
		catch(Exception ex) {
			System.out.println("Greska pri brisanje.");
			if(ERROR_REMIX) ex.printStackTrace(System.out);
		}
		System.out.println("Brisanje zavrseno."); 
	}
	
	public static void list() {
		try {
			for(MySQLUserDTO dto : dao.getDBUsers()) {
				System.out.println(); 
				System.out.println("Korisnik : "+dto.getUsername());
				System.out.println("Profil korinika BAP - baze podataka : "+dto.isDatabaseUser()); 
			}
		}catch(Exception ex) {
			System.out.println("Greska pri ocitavanju liste.");
		}
	}
}

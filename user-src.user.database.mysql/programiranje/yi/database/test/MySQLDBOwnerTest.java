package programiranje.yi.database.test;

import java.util.List;
import java.util.Scanner;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.model.DBUserData;

/**
 * Тестирање баратања са базом података. 
 * @author mirko
 * @version 1.0
 */
public class MySQLDBOwnerTest {
	public static final Scanner scanner = new Scanner(System.in);
	public static final RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
	public static final MySQLUserDAO  dao = new  MySQLUserDAO(connections);
	
	public static String input(String header) {
		System.out.print(header);
		return scanner.nextLine(); 
	}
	
	public static void menu() {
		System.out.println();
		System.out.println("0. Izalaz");
		System.out.println("1. Unos");
		System.out.println("2. Brisanje");
		System.out.println("3. Preimenovanje");
		System.out.println("4. Promjena lozinke");
		System.out.println("5. Testiranje lozinke");
		System.out.println("6. Pregled korisnika");
		System.out.println(); 
	}
	
	public static void main(String ... args) {
		while(true) {
			int izbor = -1; 
			
			menu(); 
			
			try{
				izbor = Integer.parseInt(input("IZBOR : "));
			}catch(Exception ex) {
				izbor = -1; 
			}
			
			if(izbor==0) break; 
			System.out.println(); 
			
			switch(izbor) {
				case 1: 
					System.out.println("Unos korisnika");
					add(); 
					break; 
				case 2: 
					System.out.println("Brisanje korisnika"); 
					remove(); 
					break; 
				case 3: 
					System.out.println("Preimenovanje korisnika"); 
					updateUsername(); 
					break; 
				case 4: 
					System.out.println("Promjena lozinke korisnika");
					updatePassword(); 
					break; 
				case 5: 
					System.out.println("Testiranje lozinke korisnika"); 
					testPassword();
					break; 
				case 6: 
					System.out.println("Pregled korisnika");
					listUsernames(); 
					break; 
				default: 
					System.out.println("Pogresan izbor");
			}
		}
	}
	
	public static void add() {
		try {
			String username = input("Korisnicko ime : ");
			String password = input("Lozinka : "); 
			if(dao.getDBOwner(username)!=null) {
				System.out.println("Korisnik postoji od ranije.");
			}else {
				dao.openAsDatabaseUser(username, password);
				System.out.println("Dodavanje korisnika uspjesno."); 
			}
		}catch(Exception ex) {
			System.out.println("Greska pri dodavanju korisnickog imena.");
		}
	}
	
	public static void remove() {
		try {
			String username = input("Korisnicko ime : ");
			if(dao.getDBOwner(username)==null) {
				System.out.println("Korisnik ne postoji.");
			}else {
				dao.closeDatabaseUserProfile(username);
				System.out.println("Brisanje korisnika uspjesno.");
			}
		}catch(Exception ex) {
			System.out.println("Greska pri brisanju korisnickog imena.");
		}
	}
	
	public static void updateUsername() {
		try {
			String username = input("Korisnicko ime : ");
			String neousername = input("Novo korisicko ime : ");
			if(dao.getDBOwner(username)==null) {
				System.out.println("Korisnik ne postoji");
			}else if(dao.getDBOwner(neousername)!=null) {
				System.out.println("Novonaznaceno korisnicko ime zauzeto.");
			}else {
				dao.updateUsernameOfDatabaseUser(username, neousername);
				System.out.println("Korisnicko ime promjenjeno."); 
			}
		}catch(Exception ex) {
			System.out.println("Greska pri izmjeni korisnickog imena.");
		}
	}
	
	public static void updatePassword() {
		try {
			String username = input("Korisnicko ime : ");
			String password = input("Nova sifra : ");
			if(dao.getDBOwner(username)==null) {
				System.out.println("Korisnik ne postoji");
			}else {
				dao.updatePasswordOfDatabaseUser(username, password);
				System.out.println("Sifra uspjesno postavljenja/promjenjena."); 
			}
		}catch(Exception ex) {
			System.out.println("Greska pri izmjeni sifre.");
		}
	}
	
	public static void testPassword() {
		try {
			String username = input("Korisnicko ime : ");
			String password = input("Lozinka : ");
			if(dao.getDBOwner(username)==null)
				System.out.println("Korisnk ne postoji");
			else if(dao.validatePassword(username, password)) 
				System.out.println("Lozinka je validna");
			else 
				System.out.println("Lozinka nije validna");
		}catch(Exception ex) {
			System.out.println("Greska pri validaciji lozinke");
		}
	}
	
	public static void listUsernames() {
		try {
			List<DBUserData> users = dao.getDbRootInformer().getUsers(); 
			for(DBUserData user: users) {
				System.out.println(user.getUserName());
			}
		}catch(Exception ex) {
			System.out.println("Greska pri ocitavanju korisnika."); 
		}
	}
}

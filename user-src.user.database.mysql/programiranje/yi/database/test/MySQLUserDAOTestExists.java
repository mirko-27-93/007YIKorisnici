package programiranje.yi.database.test;

import java.sql.SQLException;
import java.util.Scanner;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.model.DBUserData;

/**
 * Баратање операција са табелом регистровања 
 * корисника базе података, као редовиних 
 * корисничких профила. Тестирање.
 * @author mirko
 * @version 1.0
 */
public class MySQLUserDAOTestExists {
	public final static Scanner scanner = new Scanner(System.in); 
	public static void main(String ... args) throws SQLException {
		RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
		MySQLUserDAO  dao = new  MySQLUserDAO(connections);
		System.out.print("Korisnicko ime : ");
		String username = scanner.nextLine();
		DBUserData dbOwner = dao.getDBOwner(username);
		System.out.println(dao.getDBUser(username));
		System.out.println(dao.countConcreteDBUser(username));
		System.out.println(dbOwner==null?"null":dbOwner.getUserName());
	}
}

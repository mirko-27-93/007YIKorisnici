package programiranje.yi.database.test;

import java.sql.SQLException;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.data.DBRootDAO;
import studiranje.ip.data.RootConnectionPool;

/**
 * Баратање операција са табелом регистровања 
 * корисника базе података, као редовиних 
 * корисничких профила. Тестирање.
 * @author mirko
 * @version 1.0
 */
public class MySQLUserDAOTestList {
	public static void test1(String ... args) throws SQLException {
		RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yimp"); 
		MySQLUserDAO  dao = new  MySQLUserDAO(connections);
		DBRootDAO rdao = dao.getDbRootInformer(); 
		System.out.println(rdao.getDatabases()); 
		System.out.println(dao.databaseManervarable());
	}
	public static void test2(String ... args) throws SQLException {
		RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
		MySQLUserDAO  dao = new  MySQLUserDAO(connections);
		DBRootDAO rdao = dao.getDbRootInformer(); 
		System.out.println(rdao.getDatabases()); 
		System.out.println(dao.databaseManervarable());
	}
	public static void main(String ... args) throws SQLException {
		RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
		MySQLUserDAO  dao = new  MySQLUserDAO(connections);
		System.out.println(dao.getDBUsers());
		System.out.println(dao.countDBUsers());
	}
}

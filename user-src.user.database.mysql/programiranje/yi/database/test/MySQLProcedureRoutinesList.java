package programiranje.yi.database.test;

import java.util.List;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.data.RootConnectionPool;

/**
 * Очитавање процедура у бази података. 
 * @author mirko
 * @version 1.0
 */
public class MySQLProcedureRoutinesList {
	public static final boolean ERROR_REMIX = false; 
	public static void main(String ... argss) {
		try {
			RootConnectionPool connections = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"); 
			MySQLUserDAO  dao = new  MySQLUserDAO(connections);
			List<String> routines = dao.listOfProcedureRoutines();
			System.out.println("MYSQL DB uskladistene procedure : ");
			for(String procedure: routines)
				System.out.println(procedure); 
		}catch(Exception ex) {
			if(ERROR_REMIX) ex.printStackTrace();
			System.out.println("Doslo je do greske pri ocitavanju.");
		}
	}
}

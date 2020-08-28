package programiranje.yi.database.bean;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.AbstractConnectionPool;
import studiranje.ip.data.DBAbstractUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.ConnectionPool;
import studiranje.ip.engine.model.DataSourceUserModel;

/**
 * Зрно које се користи при баратању основама корисничком управљања 
 * базом података. 
 * @author mirko
 * @version 1.0
 */
public class DatabaseUserInfoBean implements Serializable{
	private static final long serialVersionUID = -6158998737253155938L;
	private transient UserGeneralController controller = UserGeneralController.getInstance();
	private transient MySQLUserDAO databaseInfoAdapter;
	
	public String init(HttpSession session) {
		databaseInfoAdapter = null;
		DataSourceUserModel dsm = controller.getRegistrator(session).getUserDataLink(); 
		if(dsm instanceof DBAbstractUserDAO) {
			DBAbstractUserDAO dao = (DBAbstractUserDAO) dsm;
			AbstractConnectionPool pool = dao.getConnections();
			if(pool instanceof RootConnectionPool) {
				RootConnectionPool rpool = (RootConnectionPool) pool; 
				databaseInfoAdapter = new MySQLUserDAO(rpool);
			}
			if(pool instanceof ConnectionPool) {
				RootConnectionPool rpool = RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi");
				databaseInfoAdapter = new MySQLUserDAO(rpool);
			}
		}
		return ""; 
	}
	
	public MySQLUserDAO getDatabaseInfoAdapter() {
		return databaseInfoAdapter;
	}
	public void setDatabaseInfoAdapter(MySQLUserDAO databaseInfoAdapter) {
		this.databaseInfoAdapter = databaseInfoAdapter;
	} 
	
	public boolean databaseManervalble() {
		if(databaseInfoAdapter==null) return false; 
		try {return databaseInfoAdapter.databaseManervarable();}
		catch(Exception ex) {return false;}
	}
	public boolean isDBUser(String dbUser) {
		if(databaseInfoAdapter==null) return false; 
		try { 
			return databaseInfoAdapter.isRealDBUser(dbUser);}
		catch(Exception ex) {
			return false;}
	}
	public boolean isUnreal(String dbUser) {
		if(databaseInfoAdapter==null) return true; 
		try {return !databaseInfoAdapter.isRealDBUser(dbUser) && databaseInfoAdapter.getDBOwner(dbUser)!=null;}
		catch(Exception ex) {return true;}
	}
}

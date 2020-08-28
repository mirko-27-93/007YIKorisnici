package studiranje.ip.controller;

import studiranje.ip.data.DBUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.UserDTO;
import studiranje.ip.datasource.description.util.DatabaseInitializable;
import studiranje.ip.datasource.description.util.SessionInitializable;
import studiranje.ip.engine.controller.DataSourceDescriptor;
import studiranje.ip.engine.model.DataSourceUserModel;
import studiranje.ip.exception.UserDuplicateEmailException;
import studiranje.ip.exception.UserDuplicationException;
import studiranje.ip.model.UserInfo;
import studiranje.ip.model.UserPassword;

/**
 * Контролер регистрације који се користи у режиму разних база података 
 * за корисника које се користе у сесијама.
 * @author mirko
 * @version 1.0
 */
public class UserRegistrationSVCController extends AbstractUserRegsistrationController{
	private DataSourceUserModel userDataLink; 
	
	public  UserRegistrationSVCController(String database, String dbName) {
		RootConnectionPool pool = RootConnectionPool.getConnectionPool(database); 
		userDataLink = new DBUserDAO(pool, dbName);
	}
	
	public  UserRegistrationSVCController(DataSourceDescriptor desc, String svcName) {
		userDataLink = desc.getUserDS();
		if(userDataLink instanceof SessionInitializable) {
			 SessionInitializable ssi = (SessionInitializable) userDataLink;
			 ssi.initializeSession();
		}
		if(userDataLink instanceof DatabaseInitializable) {
			DatabaseInitializable dbi = (DatabaseInitializable) userDataLink;
			dbi.initializeDatabase();
		}
	}
	
	public void register(UserDTO  user) {
		try {
			if(user==null) throw new NullPointerException(); 
			if(userDataLink.existsUser(user.getUser().getUsername())) throw new UserDuplicationException(); 
			if(userDataLink.existsEmail(user.getUser().getEmail()))   throw new UserDuplicateEmailException(); 
			userDataLink.insert(user);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	public void unregister(String username) {
		try {
			userDataLink.delete(username);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	public UserDTO get(String username) {
		try {
			return userDataLink.get(username);
		}catch(Exception ex) {
			throw new RuntimeException(username, ex);
		}
	}
	public UserInfo getInfo(String username) {
		try {
			return userDataLink.getUser(username);
		}catch(Exception ex) {
			throw new RuntimeException(username, ex);
		}
	}
	
	public UserPassword getPassword(String username) {
		try {
			return userDataLink.getPassword(username);
		}catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	public DataSourceUserModel getUserDataLink() {
		return userDataLink;
	}
}

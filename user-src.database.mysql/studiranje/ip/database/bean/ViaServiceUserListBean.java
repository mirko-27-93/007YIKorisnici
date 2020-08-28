package studiranje.ip.database.bean;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpSession;

import studiranje.ip.bean.PageBean;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.database.UserDTO;
import studiranje.ip.engine.DBUserDAOOldDBService;


/**
 * Сервисирање података о корисницима преко сервиса. 
 * ЈСП страница за очитавање корисника. 
 * @author mirko
 * @version 1.0
 */
public class ViaServiceUserListBean implements Serializable{
	private static final long serialVersionUID = -3851006249055355598L;
	
	public int count(HttpSession session) {
		try {
			UserGeneralController controller = UserGeneralController.getInstance(); 
			DBUserDAOOldDBService ds = (DBUserDAOOldDBService) controller.getRegistrator(session).getUserDataLink(); 
			if(ds.getHttpClient()==null) {
				ds.initializeSession();
				ds.initializeDatabase();
			}
			return ds.countUsers();
		}catch(Exception ex) {
			return -1; 
		}
	}
	
	public int count(HttpSession session, PageBean filter) {
		try {
			UserGeneralController controller = UserGeneralController.getInstance(); 
			DBUserDAOOldDBService ds = (DBUserDAOOldDBService) controller.getRegistrator(session).getUserDataLink(); 
			if(ds.getHttpClient()==null) {
				ds.initializeSession();
				ds.initializeDatabase();
			}
			return ds.countUsers(filter);
		}catch(Exception ex) {
			return -1; 
		}
	}
	
	public List<UserDTO> getAllUsers(HttpSession session){
		try {
			UserGeneralController controller = UserGeneralController.getInstance(); 
			DBUserDAOOldDBService ds = (DBUserDAOOldDBService) controller.getRegistrator(session).getUserDataLink(); 
			if(ds.getHttpClient()==null) {
				ds.initializeSession();
				ds.initializeDatabase();
			}
			return ds.getUsers();
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public List<UserDTO> getAllUsers(HttpSession session, PageBean page){
		try {
			UserGeneralController controller = UserGeneralController.getInstance(); 
			DBUserDAOOldDBService ds = (DBUserDAOOldDBService) controller.getRegistrator(session).getUserDataLink(); 
			if(ds.getHttpClient()==null) {
				ds.initializeSession();
				ds.initializeDatabase();
			}
			return ds.getUsers(page);
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
}

package programiranje.yi.user.app.services.information;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.AbstractConnectionPool;
import studiranje.ip.data.DBAbstractUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.ConnectionPool;
import studiranje.ip.database.model.DBUserData;
import studiranje.ip.engine.model.DataSourceUserModel;

/**
 * Сервлет за размјену информација када су у питању 
 * база података и информације корисничког профила на њој. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/DatabaseUserInfoService")
public class DatabaseUserInfoService extends HttpServlet{
	private static final long serialVersionUID = -2697428988589610739L;

	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		try {
			String username = (String) req.getSession().getAttribute(ATTR_SESSION_LOGIN);  
			if(username==null || username.trim().length()==0) throw new RuntimeException(); 
			
			MySQLUserDAO dao = null;
			DataSourceUserModel dsum = controller.getRegistrator(req.getSession()).getUserDataLink(); 
			
			if(dsum instanceof DBAbstractUserDAO) {
				DBAbstractUserDAO basicDAO = (DBAbstractUserDAO) dsum; 
				AbstractConnectionPool pool = basicDAO.getConnections(); 
				if(pool instanceof RootConnectionPool) {
					 dao = new MySQLUserDAO((RootConnectionPool) pool);
				}else if(pool instanceof ConnectionPool) {
					 dao = new MySQLUserDAO(RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"));
				}else throw new RuntimeException("DATABASE ACCESS DENIED");
			}else throw new RuntimeException("DATABASE ACCESS DENIED");
			
			boolean exists = false; 
			for(DBUserData user: dao.getDbRootInformer().getUsers())
				if(username.contentEquals(user.getUserName())) {exists = true; break;}
			
			
			boolean existsYIUser = false;
			try{ existsYIUser = dao.isRealDBUser(username);}catch(Exception ex) {}
			
			JsonArray tables = new JsonArray();
			JsonArray procedures = new JsonArray();
			
			for(String table : dao.getDbRootInformer().getTables(dao.getConnection().getDbName())) 
				tables.add(table);
			
			for(String procedure : dao.listOfProcedureRoutines()) 
				procedures.add(procedure);
			
			JsonObject root = new JsonObject(); 
			root.addProperty("success", true);
			root.addProperty("message", "");
			root.addProperty("username", username);
			root.addProperty("user", exists);
			root.addProperty("yi.user", existsYIUser);
			root.addProperty("database", dao.getConnection().getFullDatabaseInclassURI().toString());
			root.add("tables", tables);
			root.add("procedures", procedures);
			resp.getWriter().println(root.toString()); 
		}catch(Exception ex) {
			JsonObject root = new JsonObject(); 
			root.addProperty("success", false);
			String message = ex.getMessage(); 
			if(message==null) message = ""; 
			root.addProperty("message", message);
			resp.getWriter().println(root.toString()); 
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

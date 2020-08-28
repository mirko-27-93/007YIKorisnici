package programiranje.yi.user.app.services.information;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.AbstractConnectionPool;
import studiranje.ip.data.DBAbstractUserDAO;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.ConnectionPool;
import studiranje.ip.engine.model.DataSourceUserModel;
import studiranje.ip.model.UserPassword;

/**
 * Најосновнији подаци о циљаном кориснику (корисничко име, ако постоји, 
 * база података и сажетак лозинке), дакле без пријаве (За потребе локализовања
 * клијентских и осталих апликативности, на примјер). 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/FastUserInfoService")
public class FastUserInfoService extends HttpServlet{
	private static final long serialVersionUID = 5838502297601497684L;
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String un = req.getParameter("username");
		
		try {
			String database = ""; 
			String username = ""; 
			String userpassword = ""; 
			
			DataSourceUserModel dsum = controller.getRegistrator(req.getSession()).getUserDataLink(); 
			if(dsum instanceof DBAbstractUserDAO) {
				DBAbstractUserDAO dao = (DBAbstractUserDAO) dsum; 
				AbstractConnectionPool pool = dao.getConnections();
				if(pool instanceof ConnectionPool) 
					database = "http://root:root@localhost:3306/yi"; 
				else if(pool instanceof RootConnectionPool) 
					database = ((RootConnectionPool) pool).getFullDatabaseInclassURI(); 
				if(database==null) database = ""; 
			}
			
				
			username = un; 
			if(un!=null) {
				UserPassword pass = controller.getRegistrator(req.getSession()).getUserDataLink().getPassword(un); 
				if(pass!=null && pass.getToPasswordRecord()!=null) userpassword = pass.getToPasswordRecord();
			}
			
			JsonObject root = new JsonObject();
			root.addProperty("success", true);
			root.addProperty("message", "");
			root.addProperty("database", database);
			root.addProperty("user.name", username);
			root.addProperty("user.password", userpassword);
			resp.getWriter().println(root.toString());
		}catch(Exception ex) {
			JsonObject root = new JsonObject();
			root.addProperty("success", false);
			String message = ex.getMessage();
			if(message==null) message="";
			root.addProperty("message", message);
			resp.getWriter().println(root.toString()); 
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}

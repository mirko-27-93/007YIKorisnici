package programiranje.yi.user.app.services.operation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import studiranje.ip.controller.UserGeneralController;

/**
 * Служи за одјаву сервисних сесија. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/LogoutService")
public class LogoutService extends HttpServlet{
	private static final long serialVersionUID = -8902082892380261210L;
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		try {
			String username = (String) req.getSession().getAttribute(ATTR_SESSION_LOGIN); 
			if(username==null || username.trim().length()==0) throw new NullPointerException("NO LOGGED"); 
			controller.getSessions().logout(username, req.getSession());
			JsonObject root = new JsonObject(); 
			root.addProperty("success", true);
			root.addProperty("message", "");
			resp.getWriter().println(root.toString());
		}catch(Exception ex) {
			JsonObject root = new JsonObject(); 
			root.addProperty("success", false);
			String message = ex.getMessage(); 
			if(message == null) message = ""; 
			root.addProperty("message", message);
			resp.getWriter().println(root.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}

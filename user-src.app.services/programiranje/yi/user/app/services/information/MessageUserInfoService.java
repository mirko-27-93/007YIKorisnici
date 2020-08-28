package programiranje.yi.user.app.services.information;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.model.UserRequisit;

/**
 * Сервлет који се односи на захтијев за информације о 
 * дозвољености и поставкама за поруке. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/MessageUserInfoService")
public class MessageUserInfoService extends HttpServlet{
	private static final long serialVersionUID = 1878561874182352494L;
	
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		try {
			String username = (String) req.getSession().getAttribute(ATTR_SESSION_LOGIN);  
			if(username==null || username.trim().length()==0) throw new RuntimeException(); 
			
			UserRequisit requisit = controller.getRegistrator(req.getSession()).getUserDataLink().getRequisit(username);
			
			JsonObject root = new JsonObject(); 
			root.addProperty("success", true);
			root.addProperty("message", "");
			
			root.addProperty("username", username);
			root.addProperty("messages.email", requisit.isEmailNotifications());
			root.addProperty("messages.web", requisit.isWebappNotifications());
			
			resp.getWriter().println(root.toString()); 
		}catch(Exception ex) {
			ex.printStackTrace(System.out);
			
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

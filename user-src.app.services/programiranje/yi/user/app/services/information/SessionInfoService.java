package programiranje.yi.user.app.services.information;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Сервис којим се дају доступне информације о сесији. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/SessionInfoService")
public class SessionInfoService extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		String username = (String) req.getSession().getAttribute("status.logged"); 
		String database = (String) req.getSession().getAttribute("status.database"); 
		String type = (String) req.getSession().getAttribute("status.type"); 
		String engine = (String) req.getSession().getAttribute("status.engine"); 
		String address = (String) req.getSession().getAttribute("status.address"); 
		String file = (String) req.getSession().getAttribute("status.file"); 
		
		if(username==null || username.trim().length()==0) username = ""; 
		if(username==null || username.trim().length()==0) database = ""; 
		if(username==null || username.trim().length()==0) type     = "";
		if(username==null || username.trim().length()==0) engine   = "";
		if(username==null || username.trim().length()==0) address  = "";
		if(username==null || username.trim().length()==0) file     = "";
		
		if(database == null || database.trim().length()==0) database = "";
		if(type == null     || type.trim().length()==0)     type = ""; 
		if(engine == null   || engine.trim().length()==0)   engine = ""; 
		if(address == null  || address.trim().length()==0)  address = ""; 
		if(file == null     || file.trim().length()==0)     file = ""; 
		
		JsonObject object = new JsonObject();
		
		object.addProperty("success", true);
		object.addProperty("message", "");
		object.addProperty("id",   req.getSession().getId());
		object.addProperty("user", username);
		object.addProperty("database", database); 
		object.addProperty("engine", engine);
		object.addProperty("address", address);
		object.addProperty("file", file);
		object.addProperty("type", type);
		
		resp.getWriter().println(object.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

package programiranje.yi.user.app.services.information;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Сервис који враћа серверско вријеме. 
 * @author mirko
 * @version 1.0
 */

@WebServlet("/ServerTimeService")
public class ServerTimeService extends HttpServlet{
	private static final long serialVersionUID = -1182624453683038381L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		JsonObject object = new JsonObject();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
		String timestamp =  sdf.format(new Date()); 
		object.addProperty("timestamp", timestamp);
		resp.getWriter().println(object.toString()); 
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}

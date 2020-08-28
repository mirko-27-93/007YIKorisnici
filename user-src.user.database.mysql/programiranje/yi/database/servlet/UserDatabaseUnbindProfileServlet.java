package programiranje.yi.database.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import programiranje.yi.database.MySQLUserDAO;
import studiranje.ip.bean.InformationBean;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.AbstractConnectionPool;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.ConnectionPool;
import studiranje.ip.lang.UserSessionConstantes;

/**
 * Сервлет брисања корисничког профила на бази података. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/UserDatabaseUnbindProfileServlet")
public class UserDatabaseUnbindProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	private InformationBean gengetUserInformationBean(HttpServletRequest req, HttpServletResponse resp) {
		InformationBean infoBean = (InformationBean) req.getSession().getAttribute(UserSessionConstantes.USER_INFO_BEAN); 
		if(infoBean == null) {
			infoBean = new InformationBean(); 
			req.getSession().setAttribute(UserSessionConstantes.USER_INFO_BEAN, infoBean);
		}
		return infoBean; 
	}
	
    public UserDatabaseUnbindProfileServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		InformationBean msg = gengetUserInformationBean(request, response);
		String username = (String) request.getSession().getAttribute(ATTR_SESSION_LOGIN);
		
		if(username==null) {
			response.sendError(404, "USER NOT LOGED");
			return; 
		}
		
		try {
			AbstractConnectionPool connection = controller.getRegistrator(request.getSession()).getUserDataLink().asDBAbstractUserDAO().getConnections(); 
			MySQLUserDAO dao = null; 
			if(connection instanceof ConnectionPool) dao = new MySQLUserDAO(RootConnectionPool.getConnectionPool("http://root:root@localhost:3306/yi"));
			if(connection instanceof RootConnectionPool) dao = new MySQLUserDAO((RootConnectionPool)connection);
			if(dao == null) {
				msg.setMessage("msg", "Адаптер према иницијализационим профилним баратањима према бази података не постоји.");
				msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
				msg.setAnnotation("DB_USER_PROFILE");
				msg.setException("msg", new RuntimeException("Initial adapter for database profile & profile not found."));
			}else {
				dao.clean(username);
				dao.closeDatabaseUserProfile(username);
				msg.setMessage("msg", "Профил корисника базе података успјешно уклоњен.");
				msg.setMessageSource("/WEB-INF/msg.user/success.jsp");
				msg.setAnnotation("DB_USER_PROFILE");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			msg.setMessage("msg", "Кориснички профил при базама података. Грешка при брисању.");
			msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
			msg.setAnnotation("DB_USER_PROFILE");
			msg.setException("msg", new RuntimeException("Error with erasing database profile for user."));
		}
		
		response.sendRedirect(request.getContextPath()+"/database.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}

package programiranje.yi.database.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import programiranje.yi.database.MySQLUserDAO;
import programiranje.yi.database.MySQLUserDTO;
import programiranje.yi.database.bean.DatabaseUserInfoBean;
import programiranje.yi.database.configuration.DatabaseUserProfileConstants;
import studiranje.ip.bean.InformationBean;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.AbstractConnectionPool;
import studiranje.ip.data.RootConnectionPool;
import studiranje.ip.database.ConnectionPool;
import studiranje.ip.lang.UserSessionConstantes;

/**
 * Сервлет успостављања корисничког профила на бази података. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/UserDatabaseBindProfileServlet")
public class UserDatabaseBindProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
	private DatabaseUserInfoBean gengetDBUIBean(HttpSession session) {
		DatabaseUserInfoBean bean = (DatabaseUserInfoBean) session.getAttribute(DatabaseUserProfileConstants.DATABSE_PROFILE_BEAN); 
		if(bean==null) {
			bean = new DatabaseUserInfoBean(); 
			bean.init(session);
			session.setAttribute(DatabaseUserProfileConstants.DATABSE_PROFILE_BEAN, bean);
		}
		return bean; 
	}
	
	private InformationBean gengetUserInformationBean(HttpServletRequest req, HttpServletResponse resp) {
		InformationBean infoBean = (InformationBean) req.getSession().getAttribute(UserSessionConstantes.USER_INFO_BEAN); 
		if(infoBean == null) {
			infoBean = new InformationBean(); 
			req.getSession().setAttribute(UserSessionConstantes.USER_INFO_BEAN, infoBean);
		}
		return infoBean; 
	}
	
    public UserDatabaseBindProfileServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		DatabaseUserInfoBean bean = gengetDBUIBean(request.getSession());
		InformationBean msg = gengetUserInformationBean(request, response);
		String username = (String) request.getSession().getAttribute(ATTR_SESSION_LOGIN);
		
		if(username==null) {
			response.sendError(404, "USER NOT LOGED");
			return; 
		}
		
		String password = request.getParameter("password"); 
		if(password==null) password = ""; 
		
		if(bean.isUnreal(username)) {
			msg.setMessage("msg", "Корисничко име је заузето, при профилима базе података.");
			msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
			msg.setAnnotation("DB_USER_PROFILE");
			msg.setException("msg", new RuntimeException("Unreal user for database profile."));
		}else {
			if(bean.isDBUser(username)) {
				msg.setMessage("msg", "Кориснички профил при базама података раније успостављен.");
				msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
				msg.setAnnotation("DB_USER_PROFILE");
				msg.setException("msg", new RuntimeException("Exists user for database profile."));
			}else {
				try {
					if(!controller.getRegistrator(request.getSession()).getUserDataLink().getPassword(username).checkPassword(password)) {
						msg.setMessage("msg", "Корисничка лозинка је неправилна.");
						msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
						msg.setAnnotation("DB_USER_PROFILE");
						msg.setException("msg", new RuntimeException("User password for database profile & profile is wrong."));
					}else {
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
							if(dao.getDBUser(username)!=null || dao.getDBOwner(username)!=null) {
								msg.setMessage("msg", "Кориснички профил према бази података је заузет за дато корисничко име.");
								msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
								msg.setAnnotation("DB_USER_PROFILE");
								msg.setException("msg", new RuntimeException("Database user profile alredy in use."));
							}else {
								MySQLUserDTO dto = new MySQLUserDTO(); 
								dto.setUsername(username);
								dto.setDatabaseUser(true);
								dao.insert(dto);
								dao.openAsDatabaseUser(username, password);
								msg.setMessage("msg", "Профил корисника базе података успјешно постављен.");
								msg.setMessageSource("/WEB-INF/msg.user/success.jsp");
								msg.setAnnotation("DB_USER_PROFILE");
							}
						}
					}
				}catch(Exception ex) {
					msg.setMessage("msg", "Кориснички профил при базама података. Грешка при успостављању.");
					msg.setMessageSource("/WEB-INF/msg.user/error.jsp");
					msg.setAnnotation("DB_USER_PROFILE");
					msg.setException("msg", new RuntimeException("Erorr with setting user for database profile."));
				}
			}
		}
		response.sendRedirect(request.getContextPath()+"/database.jsp#database_manervar");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

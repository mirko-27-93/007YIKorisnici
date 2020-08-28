package studiranje.ip.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import studiranje.ip.bean.InformationBean;
import studiranje.ip.bean.UserBean;
import studiranje.ip.configuration.ForbbidenUsernameList;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.data.DBUserDAO;
import studiranje.ip.data.event.UpdateUsernameRunnable;
import studiranje.ip.database.UserDAO;
import studiranje.ip.database.UserDTO;
import studiranje.ip.datasource.description.util.LoginEngine;
import studiranje.ip.engine.DBUserDAOOldDBService;
import studiranje.ip.engine.controller.GeneralService;
import studiranje.ip.engine.model.DataSourceUserModel;
import studiranje.ip.lang.UserSessionConstantes;

/**
 * Сервел разрешавања форми за кориснике. 
 * @author mirko
 * @version 1.0
 * WEB APPLICATION
 */

@WebServlet("/UserResolveServlet/*")
public class UserResolveServlet extends HttpServlet{
	private static final long serialVersionUID = -855859210041197355L;
	
	public static final String URI_SEPARATOR = "/"; 
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
	private void loadUserBeanRegistration(HttpServletRequest req, HttpServletResponse resp) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN);
		UserDTO dto = controller.getRegistrator(req.getSession()).get(userBean.getUsername());
		userBean.reset();
		userBean.setUsername(dto.getUser().getUsername());
		userBean.setFirstname(dto.getUser().getFirstname());
		userBean.setSecondname(dto.getUser().getSecondname());
		userBean.setEmail(dto.getUser().getEmail());
	}
	
	private void initUserBeanRegistration(HttpServletRequest req, HttpServletResponse resp) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		userBean.reset();
		userBean.setUsername(req.getParameter("username"));
		userBean.setFirstname(req.getParameter("firstname"));
		userBean.setSecondname(req.getParameter("secondname"));
		userBean.setPassword(req.getParameter("password"));
		userBean.setEmail(req.getParameter("useremail"));
	}
	
	private void initUserBeanLogin(HttpServletRequest req, HttpServletResponse resp) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		userBean.reset();
		userBean.setUsername(req.getParameter("username"));
		userBean.setPassword(req.getParameter("password"));
	}
	
	
	private void initUserBeanErase(HttpServletRequest req, HttpServletResponse resp) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		userBean.reset();
		userBean.setUsername(req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString());
		userBean.setPassword(req.getParameter("old_password"));
	}
	
	private String reinitUserBeanAndPasswprdDataForUpdate(HttpServletRequest req, HttpServletResponse resp) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		userBean.reset();
		userBean.setUsername(req.getParameter("username"));
		userBean.setFirstname(req.getParameter("firstname"));
		userBean.setSecondname(req.getParameter("secondname"));
		userBean.setPassword(req.getParameter("password"));
		userBean.setEmail(req.getParameter("useremail"));
		String oldPassword = req.getParameter("old_password"); 
		return oldPassword; 
	}
	
	/**
	 * Generate or get. Генерише и поставља или преузима и врћа зрно за информације у ВА (веб апликацији).
	 * @param req захтијев. 
	 * @param resp одговор. 
	 * @return зрно за информације. 
	 */
	private InformationBean gengetUserInformationBean(HttpServletRequest req, HttpServletResponse resp) {
		InformationBean infoBean = (InformationBean) req.getSession().getAttribute(UserSessionConstantes.USER_INFO_BEAN); 
		if(infoBean == null) {
			infoBean = new InformationBean(); 
			req.getSession().setAttribute(UserSessionConstantes.USER_INFO_BEAN, infoBean);
		}
		return infoBean; 
	}
	
	private void initMessages(HttpServletRequest req, HttpServletResponse resp) {
		InformationBean userInfoBean = (InformationBean) req.getSession().getAttribute(UserSessionConstantes.USER_INFO_BEAN); 
		userInfoBean.reset();
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String operation = req.getPathInfo();
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		initMessages(req, resp);
		if(userBean==null) {
			String pathInfo = req.getPathInfo(); 
			if(pathInfo==null) pathInfo = ""; 
			resp.sendError(404,"["+req.getContextPath()+pathInfo+"] Not form found.");
		}else if(operation==null) {
			String pathInfo = req.getPathInfo(); 
			if(pathInfo==null) pathInfo = ""; 
			resp.sendError(404,"["+req.getContextPath()+pathInfo+"] Not found.");
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.LOGIN)) {
			initUserBeanLogin(req, resp);
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)!=null) {resp.sendRedirect("../WEB-PAGES/user/login.jsp"); return;}
			try {
				controller.getSessions().login(userBean.getUsername(), userBean.getPassword(), req.getSession());
				controller.login(userBean, req.getSession());
				req.getSession().setAttribute(ATTR_SESSION_LOGIN, userBean.getUsername());
				loadUserBeanRegistration(req, resp);
				controller.getMessages().setInfoBean(userInfoBean).setLoginSuccessForWeb(req, resp);
				resp.sendRedirect("../UserSwitchServlet");
			}catch(Exception ex) {
				controller.getMessages().setInfoBean(userInfoBean).setLoginGeneralFailureForWeb(req, resp);
				userInfoBean.setException("msg", ex);
				resp.sendRedirect("../WEB-PAGES/user/login.jsp");
			}
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.REGISTER)) {
			initUserBeanRegistration(req, resp);
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)!=null) {resp.sendRedirect("../UserSwitchServlet"); return;}
			try {
				ForbbidenUsernameList list = new ForbbidenUsernameList();
				if(list.getForbiddenUsernames().contains(userBean.getUsername())) 
					throw new RuntimeException("Изабрано је резервисано корисничко име.");
				UserDTO dto = userBean.getAllInfo();
				DataSourceUserModel dsum = controller.getRegistrator(req.getSession()).getUserDataLink(); 
				if(dsum instanceof GeneralService) dto.getPassword().setPlainPassword(userBean.getPassword());
				controller.getRegistrator(req.getSession()).register(dto);
				controller.getSessions().login(userBean.getUsername(), userBean.getPassword(), req.getSession());
				req.getSession().setAttribute(ATTR_SESSION_LOGIN, userBean.getUsername());
				controller.getMessages().setInfoBean(userInfoBean).setRegistrationSuccessForWeb(req, resp);
			}catch(Exception ex) {
				controller.getMessages().setInfoBean(userInfoBean).setRegistrationGeneralFailureForWeb(req, resp);
				userInfoBean.setException("msg", ex);
			}
			resp.sendRedirect("../UserSwitchServlet");
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.LOGOUT)) {
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)==null) {resp.sendRedirect("../UserSwitchServlet"); return;}
			controller.getSessions().logout(req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString(), req.getSession());		
			req.getSession().removeAttribute(ATTR_SESSION_LOGIN);
			userBean.reset();
			controller.getMessages().setInfoBean(userInfoBean).setLogoutSuccessForWeb(req, resp);
			resp.sendRedirect("../UserSwitchServlet");
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.LOGOUT_ALL_SESSIONS_FOR_USER)) {
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)==null) {resp.sendRedirect("../UserSwitchServlet"); return;}
			controller.getSessions().logoutAll(req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString(), req.getSession());
			req.getSession().removeAttribute(ATTR_SESSION_LOGIN);
			userBean.reset();
			controller.getMessages().setInfoBean(userInfoBean).setLogoutSuccessForWeb(req, resp);
			resp.sendRedirect("../UserSwitchServlet");
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.DELETE)){
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)==null) {resp.sendRedirect("../UserSwitchServlet"); return;}
			initUserBeanErase(req,resp);
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			try {
				String username = req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString(); 
				DataSourceUserModel model = controller.getRegistrator(req.getSession()).getUserDataLink(); 
				if(model instanceof DBUserDAOOldDBService) ((DBUserDAOOldDBService) model).setPassword(userBean.getPassword());
				controller.delete(req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString(), userBean.getPassword(), req.getSession());
				req.getSession().removeAttribute(ATTR_SESSION_LOGIN);
				userBean.reset();
				for(HttpSession session: controller.getSessions().getSessionsFor(username)) {
					controller.getSessions().logout(username, session);
				}
				controller.getMessages().setInfoBean(userInfoBean).setDeleteSuccessForWeb(req, resp); 
				resp.sendRedirect("../UserSwitchServlet");
			}catch(Exception ex) {
				userBean.setUsername((req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString()));
				loadUserBeanRegistration(req, resp);
				controller.getMessages().setInfoBean(userInfoBean).setDeleteGeneralFailureForWeb(req, resp);
				userInfoBean.setException("msg", ex);
				resp.sendRedirect("../UserSwitchServlet");
			}finally {
				userBean.setPassword("");
			}
		}else if(operation.contentEquals(URI_SEPARATOR+UserResolveOperation.UPDATE)) {
			String oldPassword = reinitUserBeanAndPasswprdDataForUpdate(req, resp);
			if(req.getSession().getAttribute(ATTR_SESSION_LOGIN)==null) {resp.sendRedirect("../UserSwitchServlet"); return;}
			String oldUsername = req.getSession().getAttribute(ATTR_SESSION_LOGIN).toString();
			InformationBean userInfoBean = gengetUserInformationBean(req, resp);
			try {
				DataSourceUserModel dsum = controller.getRegistrator(req.getSession()).getUserDataLink();
				if(dsum instanceof UserDAO) 
				try {
					UpdateUsernameRunnable m = (UpdateUsernameRunnable)(((UserDAO) dsum).getUpdateUsername().getBefore("user.database")); 
					if(m!=null) {
						if(!oldUsername.contentEquals(userBean.getUsername())) {
							m.setOldUsername(oldUsername);
							m.setNeoUsername(userBean.getUsername());
							m.run();
						}
					}
				}catch(Exception ex) {
					userBean.setUsername(oldUsername);
					loadUserBeanRegistration(req, resp);
					controller.getMessages().setInfoBean(userInfoBean).setUpdateGeneralFailureForWeb(req, resp);
					userInfoBean.setException("msg", ex);
					resp.sendRedirect("../UserSwitchServlet");
					return; 
				}
				if(dsum instanceof DBUserDAO) 
					try {
						UpdateUsernameRunnable m = (UpdateUsernameRunnable)(((DBUserDAO) dsum).getUpdateUsername().getBefore("user.database")); 
						if(m!=null) {
							if(!oldUsername.contentEquals(userBean.getUsername())) {
								m.setOldUsername(oldUsername);
								m.setNeoUsername(userBean.getUsername());
								m.run();
							}
						}
					}catch(Exception ex) {
						userBean.setUsername(oldUsername);
						loadUserBeanRegistration(req, resp);
						controller.getMessages().setInfoBean(userInfoBean).setUpdateGeneralFailureForWeb(req, resp);
						userInfoBean.setException("msg", ex);
						resp.sendRedirect("../UserSwitchServlet");
						return; 
					}
				
				if(dsum instanceof DBUserDAOOldDBService) {
					DBUserDAOOldDBService dsao = (DBUserDAOOldDBService) dsum;
					dsao.setPassword(oldPassword);
				}
				if(dsum instanceof LoginEngine) {
					LoginEngine le = (LoginEngine) dsum;
					if(!le.isLogged()) le.login(oldUsername, oldPassword);
				}
				controller.change(oldUsername, oldPassword, req.getSession(), userBean);
				req.getSession().setAttribute(ATTR_SESSION_LOGIN, userBean.getUsername());
				String username = userBean.getUsername();
				userBean.reset();
				userBean.setUsername(username);
				loadUserBeanRegistration(req, resp);
				
				String oun = oldUsername; 
				String nun = userBean.getUsername();
				
				
				for(HttpSession session : controller.getSessions().getSessionsFor(oun)) {
					if(!session.getId().contentEquals(req.getSession().getId())) controller.getSessions().logout(username, session);
				}
				
				for(HttpSession session : controller.getSessions().getSessionsFor(nun)) {
					if(!session.getId().contentEquals(req.getSession().getId()))  controller.getSessions().logout(username, session);
				}
				
				controller.getMessages().setInfoBean(userInfoBean).setUpdateSuccessForWeb(req, resp);
				resp.sendRedirect("../UserSwitchServlet");
			}catch(Exception ex) {
				System.out.println("EXCEPTION");
				userBean.setUsername(oldUsername);
				loadUserBeanRegistration(req, resp);
				controller.getMessages().setInfoBean(userInfoBean).setUpdateGeneralFailureForWeb(req, resp);
				userInfoBean.setException("msg", ex);
				resp.sendRedirect("../UserSwitchServlet");
			}finally {
				userBean.setPassword("");
			}
		}else {
			String pathInfo = req.getPathInfo(); 
			if(pathInfo==null) pathInfo = ""; 
			resp.sendError(404,"["+req.getContextPath()+pathInfo+"] Not found.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

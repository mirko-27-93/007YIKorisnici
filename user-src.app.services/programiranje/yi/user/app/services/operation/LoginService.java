package programiranje.yi.user.app.services.operation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import studiranje.ip.bean.DatabaseInfoBean;
import studiranje.ip.bean.UserBean;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.database.bean.RootDatabaseInfoBean;
import studiranje.ip.database.bean.RootDatabaseInfoStateBean;
import studiranje.ip.database.bean.RootDatasourceInfoBean;
import studiranje.ip.database.bean.RootDatasourceInfoStateBean;
import studiranje.ip.database.controller.DatabaseSwitchState;
import studiranje.ip.datasource.description.model.DataSourceDescription;
import studiranje.ip.datasource.description.util.BasicString;
import studiranje.ip.datasource.description.util.DatabaseInitializable;
import studiranje.ip.datasource.description.util.SessionInitializable;
import studiranje.ip.engine.controller.BasicEngine;
import studiranje.ip.engine.controller.DataSourceDescriptor;
import studiranje.ip.engine.controller.GeneralDatabase;
import studiranje.ip.engine.controller.GeneralEngine;
import studiranje.ip.engine.controller.GeneralFile;
import studiranje.ip.engine.controller.GeneralService;
import studiranje.ip.lang.UserSessionConstantes;


@WebServlet("/LoginService")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	public static final String ATTR_SESSION_DATABASE = "status.database";
	public static final String ATTR_SESSION_SERVICE = "status.address";
	public static final String ATTR_SESSION_PATH = "status.file";
	public static final String ATTR_SESSION_ENGINE = "status.engine";
	public static final String ATTR_SESSION_TYPE = "status.type";
	
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
    public LoginService() {
        super();
        
    }

    private UserBean initUserBeanLogin(HttpServletRequest req, HttpServletResponse resp, String username, String password) {
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN); 
		if(userBean==null) req.setAttribute(UserSessionConstantes.USER_BEAN, userBean=new UserBean());
		userBean.reset();
		userBean.setUsername(username);
		userBean.setPassword(password);
		return userBean;
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		String serviceName = request.getParameter("name"); 
		String username = request.getParameter("username"); 
		String password = request.getParameter("password"); 
		String database = request.getParameter("database");
		String address = request.getParameter("address"); 
		String type = request.getParameter("type"); 
		String engine = request.getParameter("engine");
		String file = request.getParameter("file"); 
		
		try {
			if(serviceName==null) throw new NullPointerException("Service name not specified.");
			if(username==null) throw new NullPointerException("Username not found.");
			if(password==null) throw new NullPointerException("Password not found.");
			if(address==null) throw new NullPointerException("Service not declared.");
			if(database==null) throw new NullPointerException("Database not specified.");
			if(type==null) throw new NullPointerException("Type not specified."); 
			if(engine==null) throw new NullPointerException("Engine not specified."); 
			if(file==null) throw new NullPointerException("File not found.");
	
			RootDatabaseInfoBean dbStateBean =  gengetDatabaseInfoBean(request, response);
			RootDatabaseInfoStateBean dbInfoBean = gengetDatabaseStateBean(request, response);
			
			RootDatasourceInfoBean dsStateBean =  gengetDatasourceInfoBean(request, response);
			RootDatasourceInfoStateBean dsInfoBean = gengetDatasourceStateBean(request, response);
			
			DatabaseInfoBean dbBean =  gengetDatabaseBean(request, response);
			
			dbInfoBean.setState(DatabaseSwitchState.SWITCH_STATE);
			dsInfoBean.setState(DatabaseSwitchState.SWITCH_STATE);
			
			dbInfoBean.apply();
			dsInfoBean.apply();
			
			if(dsInfoBean.getState() != DatabaseSwitchState.SWITCH_STATE) 
				throw new IllegalAccessException("NOT SWITCH STATE"); 
			
			String name = serviceName; 
			
				DataSourceDescription<BasicString> service = dsInfoBean.getDataSourceDescriptor(name); 
				switch(service.getType()) {
					case DATABASE: 
						String dbAddress = dbInfoBean.getDatabaseAdrressSplited(name).get("address"); 
						String dbName = dbInfoBean.getDatabaseAdrressSplited(name).get("database"); 
						controller.setRegistrator(request.getSession(), dbAddress, dbName); 
						String dbUser = dbInfoBean.getDatabaseAdrressSplited(name).get("user"); 
						String dbPassword = dbInfoBean.getDatabaseAdrressSplited(name).get("password"); 
						String jdbcAddress = dbInfoBean.getJDBCMysqlAddress(name);
						dbInfoBean.setChoosedDatabase(name);
						dbStateBean.apply(dbInfoBean); 
						dbBean.setJdbcURL(jdbcAddress);
						dbBean.setUsername(dbUser);
						dbBean.setPassword(dbPassword);
						dbBean.setDatabase(dbName);
						dsInfoBean.setChoosedDatasource(name);
						dsStateBean.apply(dsInfoBean);
						break;
					case SERVICE: 
						BasicString engine1Desc = service.productDataengine().getEngine(); 
						if(engine1Desc==null) throw new NullPointerException("NO ENGINE SESSION NOT SUPPORTED"); 
						BasicEngine engine1 = new BasicEngine(); 
						engine1.setEngineAddress(engine1Desc.toString());
						Object enginer1 = engine1.getObject(); 
						if(enginer1 instanceof DataSourceDescriptor) {
							DataSourceDescriptor dsd = (DataSourceDescriptor) enginer1; 
							if(dsd instanceof GeneralDatabase) {
								GeneralDatabase gd = (GeneralDatabase) dsd; 
								BasicString db = service.productDatabase().getDatabase();
								if(db!=null) gd.setDatabaseAddress(db.toString()); 
							}
							if(dsd instanceof GeneralService) {
								GeneralService gs = (GeneralService) dsd; 
								BasicString ds = service.productDataservice().getService();
								if(ds!=null) gs.setServiceAddress(ds.toString()); 
							}
							if(dsd instanceof GeneralEngine) {
								GeneralEngine gen = (GeneralEngine) dsd; 
								BasicString den = service.productDataengine().getEngine();
								if(den!=null) gen.setEngineAddress(den.toString()); 
							}
							if(dsd instanceof GeneralFile) {
								GeneralFile gfd = (GeneralFile) dsd; 
								BasicString dfd = service.productDatafiledir().getFileOrDir();
								if(dfd!=null) gfd.setFileAddress(dfd.toString()); 
							}
							if(dsd instanceof SessionInitializable) {
								SessionInitializable init = (SessionInitializable) dsd; 
								init.initializeSession();
							}
							if(dsd instanceof DatabaseInitializable) {
								DatabaseInitializable init = (DatabaseInitializable) dsd; 
								init.initializeDatabase();
							}
							controller.setRegistratorService(request.getSession(), dsd, name); 
							dbInfoBean.setChoosedDatabase("");
							dbStateBean.setDatabase("");
							dbStateBean.setHost("");
							dbStateBean.setPassword("");
							dbStateBean.setPort("");
							dbStateBean.setUser("");
							dbBean.setJdbcURL("");
							dbBean.setUsername("");
							dbBean.setPassword("");
							dbBean.setDatabase("");
							dsInfoBean.setChoosedDatasource(name);
							dsStateBean.apply(dsInfoBean);
						}
						break;
					case ENGINE: 
						throw new RuntimeException("ENGINE DATASOURCES NOT SUPPORTED"); 
					case FILEDIR:
						throw new RuntimeException("FILE/DIRECTORY DATASOURCES NOT SUPPORTED");
				}
		
				
			UserBean uBean =  initUserBeanLogin(request, response, username, password);
			controller.login(uBean, request.getSession());
			
			request.getSession().setAttribute(ATTR_SESSION_LOGIN, username);
			request.getSession().setAttribute(ATTR_SESSION_DATABASE, database);
			request.getSession().setAttribute(ATTR_SESSION_ENGINE, engine);
			request.getSession().setAttribute(ATTR_SESSION_SERVICE, address);
			request.getSession().setAttribute(ATTR_SESSION_PATH, file);
			request.getSession().setAttribute(ATTR_SESSION_TYPE, type);
			
			JsonObject root = new JsonObject(); 
			root.addProperty("success", true);
			root.addProperty("message", "");
			response.getWriter().println(root.toString());
		}catch(Exception ex) {
			ex.printStackTrace();
			JsonObject root = new JsonObject(); 
			root.addProperty("success", false);
			String message = ex.getMessage(); 
			if(message==null) message = ""; 
			root.addProperty("message", message);
			response.getWriter().println(root.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private RootDatasourceInfoBean gengetDatasourceInfoBean(HttpServletRequest req, HttpServletResponse resp) {
		RootDatasourceInfoBean infoBean = (RootDatasourceInfoBean) req.getSession().getAttribute(UserSessionConstantes.DATASOURCE_INFO_BEAN); 
		if(infoBean == null) {
			infoBean = new RootDatasourceInfoBean(); 
			req.getSession().setAttribute(UserSessionConstantes.DATASOURCE_INFO_BEAN, infoBean);
		}
		return infoBean; 
	}
	
	private RootDatasourceInfoStateBean gengetDatasourceStateBean(HttpServletRequest req, HttpServletResponse resp) {
		RootDatasourceInfoStateBean infoBean = (RootDatasourceInfoStateBean) req.getSession().getAttribute(UserSessionConstantes.DATASOURCE_STATE_BEAN); 
		if(infoBean == null) {
			infoBean = new RootDatasourceInfoStateBean(); 
			req.getSession().setAttribute(UserSessionConstantes.DATASOURCE_STATE_BEAN, infoBean);
		}
		return infoBean; 
	}
	
	private DatabaseInfoBean gengetDatabaseBean(HttpServletRequest req, HttpServletResponse resp) {
		DatabaseInfoBean infoBean = (DatabaseInfoBean) req.getSession().getAttribute(UserSessionConstantes.DATABASE_JSTL_JSP_BEAN); 
		if(infoBean == null) {
			infoBean = new DatabaseInfoBean(); 
			req.getSession().setAttribute(UserSessionConstantes.DATABASE_JSTL_JSP_BEAN, infoBean);
		}
		return infoBean; 
	}
	
	private RootDatabaseInfoBean gengetDatabaseInfoBean(HttpServletRequest req, HttpServletResponse resp) {
		RootDatabaseInfoBean infoBean = (RootDatabaseInfoBean) req.getSession().getAttribute(UserSessionConstantes.DATABASE_INFO_BEAN); 
		if(infoBean == null) {
			infoBean = new RootDatabaseInfoBean(); 
			req.getSession().setAttribute(UserSessionConstantes.DATABASE_INFO_BEAN, infoBean);
		}
		return infoBean; 
	}
	
	private RootDatabaseInfoStateBean gengetDatabaseStateBean(HttpServletRequest req, HttpServletResponse resp) {
		RootDatabaseInfoStateBean infoBean = (RootDatabaseInfoStateBean) req.getSession().getAttribute(UserSessionConstantes.DATABASE_STATE_BEAN); 
		if(infoBean == null) {
			infoBean = new RootDatabaseInfoStateBean(); 
			req.getSession().setAttribute(UserSessionConstantes.DATABASE_STATE_BEAN, infoBean);
		}
		return infoBean; 
	}
}

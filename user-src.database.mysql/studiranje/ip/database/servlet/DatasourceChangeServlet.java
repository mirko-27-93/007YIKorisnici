package studiranje.ip.database.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studiranje.ip.bean.DatabaseInfoBean;
import studiranje.ip.bean.InformationBean;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.database.bean.RootDatabaseInfoBean;
import studiranje.ip.database.bean.RootDatabaseInfoStateBean;
import studiranje.ip.database.bean.RootDatasourceInfoStateBean;
import studiranje.ip.database.bean.RootDatasourceInfoBean;
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
import studiranje.ip.lang.UserMessagesSourcesConstants;
import studiranje.ip.lang.UserSessionConstantes;

/**
 * Сервлет за потребе измјене, односно постављања нове базе података. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/DatasourceChangeServlet")
public class DatasourceChangeServlet extends HttpServlet{
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	private static final long serialVersionUID = 8011999633809546895L;
	public static final boolean ERROR_REMIX = false; 
	
	private transient UserGeneralController controller = UserGeneralController.getInstance(); 
	
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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String username = (String) req.getSession().getAttribute(ATTR_SESSION_LOGIN);
		InformationBean info = gengetUserInformationBean(req, resp);
		
		RootDatabaseInfoBean dbStateBean =  gengetDatabaseInfoBean(req, resp);
		RootDatabaseInfoStateBean dbInfoBean = gengetDatabaseStateBean(req, resp);
		
		RootDatasourceInfoBean dsStateBean =  gengetDatasourceInfoBean(req, resp);
		RootDatasourceInfoStateBean dsInfoBean = gengetDatasourceStateBean(req, resp);
		
		DatabaseInfoBean dbBean =  gengetDatabaseBean(req, resp);
		
		if(!(username!=null && username.trim().length()!=0)) {
			try {
				if(dsStateBean == null) throw new NullPointerException("DB STATE BEAN");
				if(dsInfoBean == null) throw new NullPointerException("DB INFO ");
				if(dsInfoBean.getState() != DatabaseSwitchState.SWITCH_STATE) 
					throw new IllegalAccessException("NOT SWITCH STATE"); 
				String name = req.getParameter("ds_name");
				try {
					DataSourceDescription<BasicString> service = dsInfoBean.getDataSourceDescriptor(name); 
					switch(service.getType()) {
						case DATABASE: 
							String dbAddress = dbInfoBean.getDatabaseAdrressSplited(name).get("address"); 
							String dbName = dbInfoBean.getDatabaseAdrressSplited(name).get("database"); 
							controller.setRegistrator(req.getSession(), dbAddress, dbName); 
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
								controller.setRegistratorService(req.getSession(), dsd, name); 
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
					
				}catch(Exception ex) {
					if(ERROR_REMIX) ex.printStackTrace();
					throw new RuntimeException(ex);
				}
				info.reset();
				info.setMessageSource(UserMessagesSourcesConstants.CLASSIC_SUCCESS_MSG_SRC); 
				info.setMessage("msg", "Промјена извора података је успјешна.");
				info.setAnnotation("");
				resp.sendRedirect(req.getContextPath()+"/datasource.jsp");
			}catch(Exception ex) {
				info.reset();
				info.setMessageSource(UserMessagesSourcesConstants.CLASSIC_FAILURE_MSG_SRC); 
				info.setMessage("msg", "Измјена извора података није успјешна.");
				info.setException("msg", ex);
				info.setAnnotation("");
				resp.sendRedirect(req.getContextPath()+"/datasource.jsp");
			}
			return; 
		}else {
			info.reset();
			info.setMessageSource(UserMessagesSourcesConstants.CLASSIC_FAILURE_MSG_SRC); 
			info.setMessage("msg", "Измјена извора података није успјешна. Потребно је радити када корисник није пријављен.");
			info.setAnnotation("");
			resp.sendRedirect(req.getContextPath()+"/datasource.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

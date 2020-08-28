package studiranje.ip.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studiranje.ip.bean.InformationBean;
import studiranje.ip.bean.UserBean;
import studiranje.ip.controller.CountryDefinitionListController;
import studiranje.ip.controller.CountryIESeviceController;
import studiranje.ip.controller.UserGeneralController;
import studiranje.ip.lang.UserFileSystemPathConstants;
import studiranje.ip.lang.UserMessagesSourcesConstants;
import studiranje.ip.lang.UserSessionConstantes;
import studiranje.ip.model.Country;
import studiranje.ip.model.UserRequisit;

/**
 * Сервлет за операцију додавања државе кориснику. Преусмјерава на
 * општу корисничку страницу за измјену података.  
 * @author mirko
 * @version 1.0
 */

@WebServlet("/CountryUserApplyServlet")
public class CountryUserApplyServlet extends HttpServlet{
	private static final long serialVersionUID = -7527764458191491051L;
	public static final String ATTR_SESSION_LOGIN = "status.logged"; 
	
	public static final CountryDefinitionListController cdlc = CountryServiceCenter.cdlc;
	public static final CountryIESeviceController ctrl = CountryServiceCenter.ctrl;
	
	public static final UserGeneralController controller = UserGeneralController.getInstance(); 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		InformationBean msg = (InformationBean) req.getSession().getAttribute("userInfoBean");
		UserBean userBean = (UserBean) req.getSession().getAttribute(UserSessionConstantes.USER_BEAN);
		
		String username = (String)req.getSession().getAttribute(ATTR_SESSION_LOGIN); 
		if(username==null) new RuntimeException("ILLEGAL ACCESS USER"); 
		String a3c = req.getParameter("a3c"); 
		
		if(a3c==null) throw new RuntimeException("ILLEGAL ACCESS A2C"); 
		
		if(a3c.trim().length()==0) {
			String oldCountry = userBean.getRequisit().getCountry(); 
			File oldCountryFlag = userBean.getRequisit().getCountryFlagPicture(); 
			try {
				userBean.getRequisit().setCountry(null);
				userBean.getRequisit().setCountryFlagPicture(null); 
				UserRequisit requisite = controller.getRegistrator(req.getSession()).getUserDataLink().getRequisit(username); 
				requisite.setCountry(null);
				requisite.setCountryFlagPicture(null);
				controller.getRegistrator(req.getSession()).getUserDataLink().updateCountryFlagPicture(username, null);
				controller.getRegistrator(req.getSession()).getUserDataLink().updateRequisite(username, requisite);
				msg.reset();
				msg.setMessage("msg", "Брисање података о држави корисника је успјешно извршено."); 
				msg.setMessageSource(UserMessagesSourcesConstants.CLASSIC_SUCCESS_MSG_SRC);
				msg.setAnnotation("COUNTRY_CHOOSER");
			}catch(Exception ex) {
				userBean.getRequisit().setCountry(oldCountry);
				userBean.getRequisit().setCountryFlagPicture(oldCountryFlag); 
				msg.reset();
				msg.setMessage("msg", "Брисање података о држави корисника није извршено.");
				msg.setException("msg", ex);
				msg.setAnnotation("COUNTRY_CHOOSER");
				msg.setMessageSource(UserMessagesSourcesConstants.CLASSIC_FAILURE_MSG_SRC);
			}
		}else {
			String oldCountry = userBean.getRequisit().getCountry(); 
			File oldCountryFlag = userBean.getRequisit().getCountryFlagPicture(); 
			try {
				Country country = ctrl.getCountry(a3c); 
				userBean.getRequisit().setCountry(country.getName());
				userBean.getRequisit().setCountryFlagPicture(new File(UserFileSystemPathConstants.COUNTRY_FLAG_IMAGES, country.getA2c()+".png")); 
				UserRequisit requisite = controller.getRegistrator(req.getSession()).getUserDataLink().getRequisit(username); 
				requisite.setCountry(country.getName());
				requisite.setCountryFlagPicture(new File(UserFileSystemPathConstants.COUNTRY_FLAG_IMAGES, country.getA2c()+".png"));
				controller.getRegistrator(req.getSession()).getUserDataLink().updateCountryFlagPicture(username, userBean.getRequisit().getCountryFlagPicture().getName());
				controller.getRegistrator(req.getSession()).getUserDataLink().updateRequisite(username, requisite);
				msg.reset();
				msg.setMessage("msg", "Постављање података о држави корисника је успјешно извршенo."); 
				msg.setMessageSource(UserMessagesSourcesConstants.CLASSIC_SUCCESS_MSG_SRC);
				msg.setAnnotation("COUNTRY_CHOOSER");
			}catch(Exception ex) {
				userBean.getRequisit().setCountry(oldCountry);
				userBean.getRequisit().setCountryFlagPicture(oldCountryFlag);
				msg.reset();
				msg.setMessage("msg", "Постављање података о држави корисника није извршено.");
				msg.setException("msg", ex);
				msg.setAnnotation("COUNTRY_CHOOSER");
				msg.setMessageSource(UserMessagesSourcesConstants.CLASSIC_FAILURE_MSG_SRC);
			}
		}
		
		resp.sendRedirect(req.getContextPath()+"/UserSwitchServlet");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

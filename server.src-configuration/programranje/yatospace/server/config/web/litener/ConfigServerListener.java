package programranje.yatospace.server.config.web.litener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import programranje.yatospace.server.config.controller.GeneralConfigController;

/**
 * Ослушкивач сервера, конфигурисања, иницијализације и финализације. 
 * @author mirko
 * @version 1.0
 */
@WebListener
public class ConfigServerListener implements ServletContextListener {

    public ConfigServerListener() {
     
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    	GeneralConfigController.mainAppConfigEngine.finish();
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	GeneralConfigController.mainAppConfigEngine.initalize();
    }
	
}

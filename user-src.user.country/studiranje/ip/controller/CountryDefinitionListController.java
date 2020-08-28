package studiranje.ip.controller;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import programranje.yatospace.server.config.controller.GeneralConfigController;

/**
 * Општа дефинициона листа/мапа за државе и њихове идентификације/кодови.  
 * @author mirko
 * @version 1.0
 */
public interface CountryDefinitionListController extends Serializable{
	public final static File DIR_OF_LCD = new File(GeneralConfigController.mainAppConfigEngine.fsc.getCountryLocalDBDirPath()); 
	public final static File DIR_OF_ECD = new File(GeneralConfigController.mainAppConfigEngine.fsc.getCountryEUServiceDBDirPath());
	public final static File LCD_LIST = new File(DIR_OF_LCD, "countries.info.json");
	public final static File ECD_LIST = new File(DIR_OF_ECD, "countries.info.json");
	public final static File ECD_LIST_EXC = new File(DIR_OF_ECD, "countries.xfilter.info.json");
	
	public Map<String, String> countryMap();
	public Map<String, File>   countryImageMap();
	public void load();
	public void init();
	public void store();
	public long size();
}

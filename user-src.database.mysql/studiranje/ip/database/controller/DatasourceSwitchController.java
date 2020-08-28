package studiranje.ip.database.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studiranje.ip.datasource.description.model.DataSourceDescription;
import studiranje.ip.datasource.description.util.BasicString;

/**
 * Контрола уграђених, сервисних извора података, захедно са базом података. 
 * @author mirko
 * @version 1.0
 */
public class DatasourceSwitchController {
	private DatabaseSwitchConfigurationController controller = new DatabaseSwitchConfigurationController();
	private DatasourceMapController mapController = new DatasourceMapController();
	
	public DatabaseSwitchConfigurationController getController() {
		return controller;
	}

	public DatasourceMapController getMapController() {
		return mapController;
	}
	
	@SuppressWarnings("static-access")
	public String defaultDBName() {
		return mapController.DEFAULT_DATABASE_NAME; 
	}
	
	@SuppressWarnings("static-access")
	public String defaultDBAddress() {
		return mapController.DEFAULT_DATABASE_ADDRESS; 
	}
	
	@SuppressWarnings("static-access")
	public DataSourceType defaultDSType() {
		return mapController.DEFAULT_DATASOURCE_TYPE; 
	}
	
	public List<String> getDatasourceNames(){
		ArrayList<String> names = new ArrayList<>(mapController.getDataDescription().keySet());
		Collections.sort(names);
		return names; 
	}
	
	public DataSourceDescription<BasicString> getDatasourceDescriptor(String name) {
		return mapController.getDataDescription().get(name); 
	}

	public String getDatabaseAddress(String name) {
		DataSourceDescription<BasicString> desc = getDatasourceDescriptor(name); 
		return desc.productDatabase().getDatabase().toString(); 
	}
	
	public String getServiceAddress(String name) {
		DataSourceDescription<BasicString> desc = getDatasourceDescriptor(name); 
		return desc.productDataservice().getService().toString(); 
	}
	
	public String getEngineAddress(String name) {
		DataSourceDescription<BasicString> desc = getDatasourceDescriptor(name); 
		return desc.productDataservice().getService().toString(); 
	}
	
	public String getFileOrDirPath(String name) {
		DataSourceDescription<BasicString> desc = getDatasourceDescriptor(name); 
		return desc.productDatafiledir().getFileOrDir().toString(); 
	}
	
	public Map<String, String> getDatabaseAdrressSplited(String name) {
		HashMap<String, String> map = new HashMap<>(); 
		URI uri = URI.create(getDatabaseAddress(name));  
		map.put("user", uri.getUserInfo().split(":")[0]); 
		map.put("password", uri.getUserInfo().split(":")[1]); 
		map.put("host", uri.getHost()); 
		map.put("port", Integer.toString(uri.getPort()));
		map.put("database", uri.getPath().substring(1).split("/")[0]);
		return map; 
	}
}

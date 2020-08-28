package studiranje.ip.database.bean;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import studiranje.ip.database.controller.DatabaseSwitchState;
import studiranje.ip.database.controller.DatasourceSwitchController;
import studiranje.ip.datasource.description.model.DataSourceDescription;
import studiranje.ip.datasource.description.util.BasicString;

/**
 * Информационо зрно о изворима података. 
 * @author mirko
 * @version 1.0
 */
public class RootDatasourceInfoStateBean implements Serializable{
	private static final long serialVersionUID = 5316027225814876368L;
	private DatabaseSwitchState state = DatabaseSwitchState.DEFAULT_STATE;
	private transient DatasourceSwitchController controller = new DatasourceSwitchController(); 
	
	public DatabaseSwitchState getState() {
		return state;
	}

	public void setState(DatabaseSwitchState state) {
		if(state==null) state = DatabaseSwitchState.DEFAULT_STATE; 
		this.state = state;
	}
	
	private String choosedDatasource = ""; 
	private HashMap<String, DataSourceDescription<BasicString>> datasourceMap = new HashMap<>();

	public String getChoosedDatasource() {
		return choosedDatasource;
	}

	public void setChoosedDatasource(String choosedDatasource) {
		if(choosedDatasource==null) choosedDatasource = this.choosedDatasource;  
		this.choosedDatasource = choosedDatasource;
	}
	
	public synchronized Map<String, DataSourceDescription<BasicString>> getDatasourceMap() {
		return new HashMap<>(datasourceMap);
	}

	public synchronized void setDatasourceMap(Map<String, DataSourceDescription<BasicString>> datasourceMap) {
		if(datasourceMap==null) datasourceMap = new HashMap<>();
		this.datasourceMap = new HashMap<>(datasourceMap);
	} 
	
	public List<String> getDatasourceNames(){
		ArrayList<String> names = new ArrayList<>(datasourceMap.keySet());
		Collections.sort(names);
		return names; 
	}
	
	public DataSourceDescription<BasicString> getDataSourceDescriptor(String name){
		return datasourceMap.get(name); 
	}
	
	public DataSourceDescription<BasicString> getCurrentDataSourceDescriptor() {
		return datasourceMap.get(choosedDatasource);
	}
	
	public String getOriginalDatabaseAddress(String name) {
		try {
			DataSourceDescription<BasicString> desc = getDataSourceDescriptor(name);
			if(desc==null) return ""; 
			BasicString databaseAddress = desc.productDatabase().getDatabase();
			if(databaseAddress==null) return "";
			return databaseAddress.toString(); 
		}catch(Exception ex) {
			return ""; 
		}
	}
	
	public String getOriginalCurrentDatabaseAddress() {
		return getOriginalDatabaseAddress(choosedDatasource);
	}
	
	public String getDatabaseAddress(String name) {
		try {
			URI uri = new URI(getOriginalDatabaseAddress(name));
			String userinfo = uri.getUserInfo();
			if(userinfo!=null && userinfo.split(":").length>1) 
				uri = UriBuilder.fromUri(uri).userInfo(userinfo.split(":")[0]).build();
			return uri.toString();
		}catch(Exception ex) {
			return "";
		}
	}
	
	public String getCurrentDatabaseAddress() {
		return getDatabaseAddress(choosedDatasource);
	}
	
	public String getServiceAddress(String name) {
		try {
			DataSourceDescription<BasicString> desc = getDataSourceDescriptor(name);
			if(desc==null) return ""; 
			BasicString serviceAddress = desc.productDataservice().getService();
			if(serviceAddress==null) return "";
			return serviceAddress.toString();
		}catch(Exception ex) {
			return "";
		}
	}
	
	public String getCurrentServiceAddress() {
		return getServiceAddress(choosedDatasource);
	}
	
	public String getEngineAddress(String name) {
		try {
			DataSourceDescription<BasicString> desc = getDataSourceDescriptor(name);
			if(desc==null) return ""; 
			BasicString engineAddress = desc.productDataengine().getEngine();
			if(engineAddress==null) return "";
			return engineAddress.toString(); 
		}catch(Exception ex) {
			return "";
		}
		
	}
	
	public String getCurrentEngineAddress() {
		return getEngineAddress(choosedDatasource);
	}
	
	public String getFiledirPath(String name) {
		try {
			DataSourceDescription<BasicString> desc = getDataSourceDescriptor(name);
			if(desc==null) return ""; 
			BasicString filedirPath = desc.productDatafiledir().getFileOrDir();
			if(filedirPath==null) return "";
			return filedirPath.toString(); 
		}catch(Exception ex) {
			return "";
		}
	}
	
	public String getCurrentFiledirPath() {
		return getFiledirPath(choosedDatasource);
	}
	
	public String getDSType(String name) {
		try {
			DataSourceDescription<BasicString> desc = getDataSourceDescriptor(name);
			if(desc==null) return ""; 
			String typeOfDS = desc.getType().toString();
			if(typeOfDS==null) return "";
			return typeOfDS; 
		}catch(Exception ex) {
			return "";
		}
	}
	
	public String getCurrentDSType() {
		return getDSType(choosedDatasource);
	}
	
	public DatasourceSwitchController getController() {
		if(controller==null) controller = new DatasourceSwitchController();
		return controller;
	}

	public void setController(DatasourceSwitchController controller) {
		this.controller = controller;
	}
	
	public synchronized String apply() {
		if(controller==null) controller = new DatasourceSwitchController();
		setDatasourceMap(controller.getMapController().getDataDescription());
		state = controller.getController().getState(); 
		if(choosedDatasource.trim().length()==0)
			choosedDatasource = controller.defaultDBName();
		return "";
	}
}

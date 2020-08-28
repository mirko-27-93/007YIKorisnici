package studiranje.ip.database.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import studiranje.ip.datasource.description.model.DataSourceDescription;
import studiranje.ip.datasource.description.util.BasicString;

/**
 * Мапа и управљање описима, односно декларацијама извора података. 
 * @author mirko
 * @version 1.0
 */
public class DatasourceMapController {
	public static final String DEFAULT_DATABASE_NAME = "databaseuser.yi"; 
	public static final String DEFAULT_DATABASE_ADDRESS = "http://root:root@localhost:3306/yi";
	public static final DataSourceType DEFAULT_DATASOURCE_TYPE =  DataSourceType.DATABASE; 
	
	public DatasourceMapController() {
		try {
			load(); 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	private HashMap<String, String> resourceMap = new HashMap<>();
	private HashMap<String, DataSourceDescription<BasicString>> dataDescription = new HashMap<>(); 
	
	public synchronized HashMap<String, String> getResourceMap(){
		return new HashMap<>(resourceMap);  
	}
	
	public synchronized void setResourceMap(HashMap<String, String> map) {
		resourceMap = new HashMap<>(map);
	}
	
	public synchronized String getResource(String name) {
		return resourceMap.get(name); 
	}
	
	
	public synchronized HashMap<String, DataSourceDescription<BasicString>> getDataDescription() {
		return new HashMap<>(dataDescription);
	}

	public synchronized  void setDataDescription(HashMap<String, DataSourceDescription<BasicString>> dataDescription) {
		this.dataDescription = new HashMap<>(dataDescription);
	}
	
	public synchronized DataSourceDescription<BasicString> getDataSourceDescriptor(String name) {
		return dataDescription.get(name); 
	}
	

	public void load() throws IOException  {
		InputStream input = getClass().getResourceAsStream("/studiranje/ip/database/configuration/user.service.map.properties");
		Properties properties = new Properties(); 
		properties.load(input);
		resourceMap.clear(); 
		dataDescription.clear(); 
		for(Map.Entry<Object, Object> me: properties.entrySet()) {
			String key = (String) me.getKey(); 
			String value = (String) me.getValue(); 
			resourceMap.put(key, value); 
			try {
				DataSourceDescription<BasicString> obj = load(key); 
				dataDescription.put(key, obj); 
			}catch(Exception ex) {
				continue; 
			}
		}
	}
	
	
	public DataSourceDescription<BasicString> load(String dsName) throws JsonIOException, JsonSyntaxException, UnsupportedEncodingException{ 
		if(dsName==null) throw new NullPointerException(); 
		if(!resourceMap.keySet().contains(dsName)) throw new RuntimeException("NOT FOUND."); 
		
		String descriptionLocation = resourceMap.get(dsName); 
		
		InputStream input = getClass().getResourceAsStream(descriptionLocation);

		JsonParser json = new JsonParser(); 
		JsonObject object = json.parse(new InputStreamReader(input, "UTF-8")).getAsJsonObject(); 
		DataSourceDescription<BasicString> obj = new DataSourceDescription<BasicString>();
		
		
		String name = null; try{name = object.get("name").getAsString();}catch(Exception ex) {}
		String typeRecord = null; try{ typeRecord = object.get("type").getAsString();} catch(Exception ex) {}
		String database = null; try{database = object.get("database").getAsString();} catch(Exception ex) {}
		String engine = null; try{engine = object.get("engine").getAsString();}catch(Exception ex) {}
		String service = null; try {service = object.get("service").getAsString();}catch(Exception ex) {}
		String file = null; try {file = object.get("filedir").getAsString();}catch(Exception ex) {}
		DataSourceType type = null; 
		
		try {type = DataSourceType.valueOf(typeRecord);}catch(Exception ex) {}
		
		
		BasicString nameBS = new BasicString(); 
		BasicString typeBS = new BasicString(); 
		BasicString databaseBS = new BasicString(); 
		BasicString serviceBS = new BasicString(); 
		BasicString engineBS = new BasicString();
		BasicString fileBS = new BasicString(); 
		
		
		nameBS.fromString(name);
		typeBS.fromString(typeRecord);
		databaseBS.fromString(database);
		serviceBS.fromString(service);
		engineBS.fromString(engine);
		fileBS.fromString(file);
		
		HashMap<String, BasicString> map = new HashMap<>(); 
		
		if(name!=null) map.put("name", nameBS);
		if(type!=null) map.put("type", typeBS);
		if(database!=null) map.put("database", databaseBS);
		if(service!=null)  map.put("service", serviceBS);
		if(engine!=null)   map.put("engine", engineBS); 
		if(file!=null)   map.put("filedir", engineBS); 
		
		obj.set(map);
		
		return obj.productAsType();
	}
}

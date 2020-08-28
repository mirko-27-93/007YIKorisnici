package programiranje.yi.user.app.services.model;

import java.io.Serializable;

/**
 * Модел који се користи као објекат са информацијама о 
 * сесији. 
 * @author mirko
 * @version 1.0
 */
public class SessionInformation implements Serializable{

	private static final long serialVersionUID = 344519749617389926L;
	private String username = "";
	private String database = "http://root:root@localhost:3306/yi"; 
	private String engine = ""; 
	private String file = ""; 
	private String service = "";
	private String type = "DATABASE";
	private String name = "databaseuser.yi"; 
	private String id = ""; 
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if(id==null) id = "";
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username==null) username = ""; 
		this.username = username;
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		if(database==null) database = ""; 
		this.database = database;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name==null) name="";
		this.name = name;
	}
	
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		if(engine==null) engine = "";
		this.engine = engine;
	}
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		if(file==null) file = ""; 
		this.file = file;
	}
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		if(service == null) service = ""; 
		this.service = service;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		if(type == null) type = "";
		this.type = type;
	}
}

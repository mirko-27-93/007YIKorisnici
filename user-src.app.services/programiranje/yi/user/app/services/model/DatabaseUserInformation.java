package programiranje.yi.user.app.services.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Објекти информација о кориснику базе података. 
 * @author mirko
 * @version 1.0
 */
public class DatabaseUserInformation implements Serializable{
	private static final long serialVersionUID = -8674496983261556085L;
	
	private String username = ""; 
	private String database = ""; 
	private boolean dbUserExists = false; 
	private boolean dbInfoExists = false; 
	private ArrayList<String> tables = new ArrayList<>();
	private ArrayList<String> procedures = new ArrayList<>();
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public boolean isDbUserExists() {
		return dbUserExists;
	}
	public void setDbUserExists(boolean dbUserExists) {
		this.dbUserExists = dbUserExists;
	}
	public boolean isDbInfoExists() {
		return dbInfoExists;
	}
	public void setDbInfoExists(boolean dbInfoExists) {
		this.dbInfoExists = dbInfoExists;
	}
	public ArrayList<String> getTables() {
		return new ArrayList<>(tables);
	}
	public ArrayList<String> getProcedures() {
		return new ArrayList<>(procedures);
	} 
	public ArrayList<String> liveTables() {
		return tables;
	}
	public ArrayList<String> liveProcedures() {
		return procedures;
	}
	public void setTables(ArrayList<String> tables) {
		this.tables = tables;
	}
	public void setProcedures(ArrayList<String> procedures) {
		this.procedures = procedures;
	} 
}

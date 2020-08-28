package programiranje.yi.user.app.services.model;

import java.io.Serializable;

/**
 * Кратке информације о серверским креденцијалима, 
 * односно коринику, запису његове лозинке и 
 * бази података из које је узет. 
 * @author mirko
 *
 */
public class ShortCredentialsInformation implements Serializable{
	private static final long serialVersionUID = 5115083454888955815L;
	
	private String database = "";
	private String username = ""; 
	private String userPassword = "";
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	} 
}

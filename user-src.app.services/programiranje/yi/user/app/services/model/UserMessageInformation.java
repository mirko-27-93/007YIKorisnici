package programiranje.yi.user.app.services.model;

import java.io.Serializable;

/**
 * Кориснички манервар управљања порукама. 
 * @author mirko
 * @version 1.0
 */
public class UserMessageInformation implements Serializable{
	private static final long serialVersionUID = 5398102640629502132L;
	private String user = ""; 
	private boolean emailMessages = false; 
	private boolean webMessages = false;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public boolean isEmailMessages() {
		return emailMessages;
	}
	public void setEmailMessages(boolean emailMessages) {
		this.emailMessages = emailMessages;
	}
	public boolean isWebMessages() {
		return webMessages;
	}
	public void setWebMessages(boolean webMessages) {
		this.webMessages = webMessages;
	} 
}

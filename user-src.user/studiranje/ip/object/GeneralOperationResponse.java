package studiranje.ip.object;

import java.io.Serializable;

/**
 * Општи одговор када је у питању сервлет/сервис. 
 * @author mirko
 * @version 1.0
 */
public class GeneralOperationResponse implements Serializable{
	private static final long serialVersionUID = -4949916620967178364L;
	private boolean ssuccess = true; 
	private String message = "";
	
	public boolean isSsuccess() {
		return ssuccess;
	}
	public void setSsuccess(boolean ssuccess) {
		this.ssuccess = ssuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		if(message==null) message = ""; 
		this.message = message;
	}
	
}

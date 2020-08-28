package studiranje.ip.datasource.description.util;

import java.io.IOException;

/**
 * Означава оквир за логовоање, уколико неки објекат 
 * захтијева те функционалности. 
 * @author mirko
 * @version 1.0
 */
public interface LoginEngine {
	public void login(String username, String password) throws IOException; 
	public void logout();
	public void logout(String user); 
	public void logoutAll(); 
	public boolean isLogged(); 
	
	public default LoginEngine asLoginEngine() {
		return (LoginEngine) this;
	}
}

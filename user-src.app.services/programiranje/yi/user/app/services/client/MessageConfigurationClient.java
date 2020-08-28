package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import programiranje.yi.user.app.services.model.UserMessageInformation;

/**
 * Клијент којим се преко сервиса добијају информације 
 * о улогованом кориснику, када су у питању начини 
 * размјена порука, односно опште информације о порукама. 
 * @author mirko
 * @version 1.0
 */
public class MessageConfigurationClient {
	private SessionClient sessionClient; 
		
	public SessionClient getSessionClient() {
		return sessionClient;
	}

	public MessageConfigurationClient setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
		return this;
	}

	public UserMessageInformation getConfigurationsInfo() {
		UserMessageInformation info = new  UserMessageInformation();
		if(sessionClient.getHttpClient() == null) throw new RuntimeException("NO LOGGED");
		try {
			URL url = new URL(sessionClient.getServerAddress()+"/MessageUserInfoService"); 

			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = sessionClient.getHttpClient().execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message); 
			
			String username = root.get("username").getAsString(); 
			boolean emailMessages = root.get("messages.email").getAsBoolean();
			boolean webMessages = root.get("messages.web").getAsBoolean();
			
			info.setUser(username);
			info.setEmailMessages(emailMessages);
			info.setWebMessages(webMessages);
			
			return info; 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
}

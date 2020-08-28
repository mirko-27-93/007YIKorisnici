package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import programiranje.yi.user.app.services.model.ShortCredentialsInformation;

/**
 * Клијент којим се брзо добијају информације о корисничком имену, 
 * лозинки клијента (ако постоји, критеријум постојања). 
 * @author mirko
 * @version 1.0
 */
public class FastUserInfoClient {
	private SessionClient sessionClient; 
	
	public SessionClient getSessionClient() {
		return sessionClient;
	}
	public  FastUserInfoClient setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
		return this; 
	}
	
	public ShortCredentialsInformation get(String username) {
		try {
			ShortCredentialsInformation info = new ShortCredentialsInformation();
			
			URL url = new URL(sessionClient.getServerAddress()+"/FastUserInfoService");  
			HttpPost httpPost = new HttpPost(url.toString());
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();   
			postParameters.add(new BasicNameValuePair("username", username));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = sessionClient.getHttpClient().execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message); 
			
			String database = root.get("database").getAsString();
			String userName = root.get("user.name").getAsString();
			String passWord = root.get("user.password").getAsString();
			
			if(database==null) database = ""; 
			if(userName==null) userName = "";
			if(passWord==null) passWord = ""; 
			
			info.setDatabase(database);
			info.setUsername(userName);
			info.setUserPassword(passWord);
			
			return info;
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public ShortCredentialsInformation get() {
		return get("");
	}
}

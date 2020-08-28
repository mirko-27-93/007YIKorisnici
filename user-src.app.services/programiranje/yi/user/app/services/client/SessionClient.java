package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import programiranje.yi.user.app.services.model.SessionInformation;

/**
 * Контрола сесије код клијената. 
 * @author mirko
 * @version 1.0
 */
public class SessionClient implements AutoCloseable{
	private CloseableHttpClient httpClient; 
	private String sessionId = ""; 
	private String username = "";
	private String serverAddress = "http://localhost:8080/002YIKorisnici"; 
	
	
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	public String getSessionId() {
		return sessionId;
	}
	public String getUsername() {
		return username;
	}

	public void login(SessionInformation sessionData, String password) {
		if(httpClient != null) throw new RuntimeException("LOGIN BEFORE LOGOUT");
		if(sessionData==null) throw new RuntimeException("NO SESSION DATA");
		try {
			URL url = new URL(serverAddress+"/LoginService"); 
			
			CloseableHttpClient httpClient = HttpClients.createDefault(); 
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			   
			postParameters.add(new BasicNameValuePair("name", sessionData.getName()));
			postParameters.add(new BasicNameValuePair("username", sessionData.getUsername()));
			postParameters.add(new BasicNameValuePair("password", password));
			postParameters.add(new BasicNameValuePair("database", sessionData.getDatabase()));
			postParameters.add(new BasicNameValuePair("address", sessionData.getService()));
			postParameters.add(new BasicNameValuePair("type", sessionData.getType()));
			postParameters.add(new BasicNameValuePair("engine", sessionData.getEngine()));
			postParameters.add(new BasicNameValuePair("file", sessionData.getFile()));
			
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			String sessionId = httpResponse.getFirstHeader("Set-Cookie").getValue();
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
			
			this.httpClient = httpClient; 
			this.sessionId = sessionId; 
			this.username = sessionData.getUsername();
		}
		catch(RuntimeException ex) { throw ex; }
		catch(Exception ex) { throw new RuntimeException(ex);}
	}
	
	public void logout() {
		if(httpClient == null) throw new RuntimeException("NOT LOGIN");
		
		try {
			URL url = new URL(serverAddress+"/LogoutService"); 
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString(); 
		
			if(!success) throw new RuntimeException(message);
			logoutLocal(); 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public void logoutLocal() {
		httpClient = null; 
		sessionId = "";
		username = "";
	}
	
	public SessionInformation getSessionInfo() {
		if(httpClient == null) throw new RuntimeException("NOT LOGIN");
		
		try {
			URL url = new URL(serverAddress+"/SessionInfoService"); 
			SessionInformation sessionInfo = new SessionInformation(); 
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message);
			
			String id = root.get("id").getAsString();
			String user = root.get("user").getAsString();
			String engine = root.get("engine").getAsString(); 
			String database = root.get("database").getAsString(); 
			String service = root.get("address").getAsString();
			String type = root.get("type").getAsString();
			String file = root.get("file").getAsString();
			
			sessionInfo.setId(id);
			sessionInfo.setUsername(user);
			sessionInfo.setEngine(engine);
			sessionInfo.setFile(file);
			sessionInfo.setType(type);
			sessionInfo.setDatabase(database);
			sessionInfo.setService(service);
			
			return sessionInfo;
		}
		catch(RuntimeException ex) { throw ex; }
		catch(Exception ex) {throw new RuntimeException(ex);}
	}
	
	public boolean isNotLogged() {
		return username==null || username.trim().length()==0; 
	}
	
	public boolean isItLogged() {
		return username!=null && username.trim().length()!=0; 
	}
	
	@Override
	public void close() throws Exception {
		if(isItLogged()) logout();
	}
}

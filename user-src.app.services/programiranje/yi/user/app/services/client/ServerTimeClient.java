package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Клијентска обрада серверског времена. 
 * @author mirko
 * @version 1.0
 */
public class ServerTimeClient {
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss"); 
	
	private SessionClient sessionClient; 
	private CloseableHttpClient client; 
	
	public SessionClient getSessionClient() {
		return sessionClient;
	}

	public ServerTimeClient setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
		this.client = sessionClient.getHttpClient();
		return this;
	}
	
	public ServerTimeClient tryReinit() {
		if(client==null)
			client = HttpClients.createDefault();
		return this; 
	}

	public ServerTimeClient trySessionSynchronize() {
		if(sessionClient.getHttpClient()!=null)
			client = sessionClient.getHttpClient();
		return this;
	}
	
	public Date getServerTimestamp() {
		try {
			URL url = new URL(sessionClient.getServerAddress()+"/ServerTimeService");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = client.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			return sdf.parse(root.get("timestamp").getAsString()); 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public Date getClientTimestamp() {
		return new Date();
	}
	
	public String convert(Date date) {
		return sdf.format(date);
	}
	
	public String covnvertClient() {
		return convert(getClientTimestamp());
	}
	
	public String covnvertServer() {
		return convert(getServerTimestamp());
	}
}

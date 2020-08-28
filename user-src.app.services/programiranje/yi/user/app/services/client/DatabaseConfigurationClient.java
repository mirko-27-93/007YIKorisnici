package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import programiranje.yi.user.app.services.model.DatabaseUserInformation;

/**
 * Добијају се информације о кориничком профилу 
 * када је у питању декларисана база података, 
 * а враћа се и њено име. 
 * @author mirko
 * @version 1.0
 */
public class DatabaseConfigurationClient {
	private SessionClient sessionClient; 
	
	public SessionClient getSessionClient() {
		return sessionClient;
	}
	public DatabaseConfigurationClient setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
		return this; 
	}

	public DatabaseUserInformation getAllInfo() {
		DatabaseUserInformation info = new DatabaseUserInformation();	
		if(sessionClient.getHttpClient() == null) throw new RuntimeException("NO LOGGED");
		
		try {
			URL url = new URL(sessionClient.getServerAddress()+"/DatabaseUserInfoService"); 
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = sessionClient.getHttpClient().execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message); 
			
			String username = root.get("username").getAsString(); 
			boolean isDBUser = root.get("user").getAsBoolean();
			boolean isYIDBUser = root.get("yi.user").getAsBoolean();
			String database = root.get("database").getAsString();
			JsonArray tablesJSON = root.get("tables").getAsJsonArray();
			JsonArray proceduresJSON = root.get("procedures").getAsJsonArray();
			ArrayList<String> tables= new ArrayList<>(); 
			ArrayList<String> procedures = new ArrayList<>(); 
			
			for(int i=0; i<tablesJSON.size(); i++)
				tables.add(tablesJSON.get(i).getAsString()); 
			
			for(int i=0; i<proceduresJSON.size(); i++)
				procedures.add(proceduresJSON.get(i).getAsString()); 
			
			info.setUsername(username);
			info.setDbUserExists(isDBUser);
			info.setDbInfoExists(isYIDBUser);
			info.setDatabase(database);
			info.setTables(tables);
			info.setProcedures(procedures);
			
			return info; 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
}

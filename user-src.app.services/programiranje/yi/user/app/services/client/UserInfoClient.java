package programiranje.yi.user.app.services.client;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import programiranje.yi.user.app.services.model.UserInformationModel;

/**
 * Клијентска класа за преузимање информација од клијента. 
 * @author mirko
 * @version 1.0
 */
public class UserInfoClient {
	private SessionClient sessionClient; 
	
	public SessionClient getSessionClient() {
		return sessionClient;
	}

	public UserInfoClient setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
		return this;
	}
	
	public UserInformationModel get() {
		 UserInformationModel info = new UserInformationModel();
		 
		 try {
				URL url = new URL(sessionClient.getServerAddress()+"/CountryUserInfoServlet?detail=full"); 

				HttpPost httpPost = new HttpPost(url.toString());
				CloseableHttpResponse httpResponse = sessionClient.getHttpClient().execute(httpPost); 
				
				JsonParser parser = new JsonParser(); 
				JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
				
				String username = root.get("username").getAsString(); 
				String email = root.get("email").getAsString();
				
				if(username==null || username.trim().length()==0) throw new NullPointerException("NO USER");
				
				info.setUsername(username);
				info.setEmail(email);
				
				JsonObject personal = root.getAsJsonObject("personal"); 
				if(personal!=null) {
					JsonObject basic = personal.getAsJsonObject("basic");
					JsonObject extendes = personal.getAsJsonObject("extended"); 
					if(basic!=null) {
						
						String fname = null; try{fname = basic.get("fname").getAsString();}catch(Exception ex) {}
						String sname = null; try{sname = basic.get("sname").getAsString();}catch(Exception ex) {}
						String passwd = null; try{ passwd = basic.get("passwd").getAsString();}catch(Exception ex) {}
						
						if(fname!=null) info.setFirstName(fname);
						if(sname!=null) info.setSecondName(sname);
						if(passwd!=null) info.setPasswordRecord(passwd);
					}
					if(extendes != null) {
						String telephone = null; 
						String address = null; 
						
						try {telephone = extendes.get("telephone").getAsString(); }catch(Exception ex) {}
						try {address = extendes.get("address").getAsString(); }catch(Exception ex) {}
						
						if(telephone !=null) info.setTelephone(telephone);
						if(address != null)  info.setAddress(address);
						
						JsonObject profile = extendes.getAsJsonObject("profile");
						if(profile!=null) {
							String userPict = null; 
							String profilePict = null; 
							try {userPict = profile.get("user_picture").getAsString();} catch(Exception ex) {}
							try {profilePict = profile.get("profile_picture").getAsString();} catch(Exception ex) {}
							
							if(userPict!=null) 		info.setUserImage(userPict);
							if(profilePict!=null)   info.setProfileImage(profilePict);
						}
						
						JsonObject country = extendes.getAsJsonObject("country");
						if(country != null) {
							String countryName = null; 
							String countryPicture = null; 
							try {countryName = country.get("country").getAsString(); } catch(Exception ex) {}
							try {countryPicture = country.get("country_picture").getAsString();} catch(Exception ex) {}
							
							if(countryName!=null) info.getCountry().setCountry(countryName);
							if(countryPicture!=null) info.getCountry().setCountryFlagImage(countryPicture);
							
							JsonObject countryInfo = country.getAsJsonObject("country_info"); 
							if(countryInfo!=null) {
								String a2c = null; 
								String a3c = null; 
								
								try {a2c = countryInfo.get("a2c").getAsString();} catch(Exception ex) {}
								try {a3c = countryInfo.get("a3c").getAsString();} catch(Exception ex) {}
								
								if(a2c!=null) info.getCountry().setA2c(a2c); 
								if(a3c!=null) info.getCountry().setA3c(a3c);
								
								JsonArray ccsJSON = countryInfo.getAsJsonArray("ccs"); 
								JsonArray tldsJSON = countryInfo.getAsJsonArray("tlds"); 
								
								if(ccsJSON!=null) {
									ArrayList<String> ccs = new ArrayList<>();
									for(int i=0; i<ccsJSON.size(); i++) ccs.add(ccsJSON.get(i).getAsString());
									info.getCountry().setCcs(ccs);
								}
								
								if(tldsJSON!=null) {
									ArrayList<String> tlds = new ArrayList<>();
									for(int i=0; i<tldsJSON.size(); i++) tlds.add(tldsJSON.get(i).getAsString());
									info.getCountry().setTlds(tlds);
								}
							}
						}
					}
				}
				
				try {
					URL url1 = new URL(sessionClient.getServerAddress()+"/UserDescriptionGetServlet"); 

					HttpPost httpPost1 = new HttpPost(url1.toString());
					CloseableHttpResponse httpResponse1 = sessionClient.getHttpClient().execute(httpPost1); 
					
					String description = new String(httpResponse1.getEntity().getContent().readAllBytes(),"UTF-8");  
					info.setDescription(description);
				}catch(Exception ex) {}
				
				return info; 
			}catch(RuntimeException ex) {
				throw ex;
			}catch(Exception ex) {
				throw new RuntimeException(ex); 
			}
	}
}

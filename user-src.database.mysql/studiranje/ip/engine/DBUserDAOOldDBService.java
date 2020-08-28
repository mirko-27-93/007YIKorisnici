package studiranje.ip.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import studiranje.ip.bean.PageBean;
import studiranje.ip.database.UserDTO;
import studiranje.ip.database.UserFlagsDTO;
import studiranje.ip.datasource.description.util.DatabaseInitializable;
import studiranje.ip.datasource.description.util.LoginEngine;
import studiranje.ip.datasource.description.util.SessionInitializable;
import studiranje.ip.engine.controller.DatabaseService;
import studiranje.ip.engine.model.DataSourceUserModel;
import studiranje.ip.lang.UserFileSystemPathConstants;
import studiranje.ip.model.UserFlags;
import studiranje.ip.model.UserInfo;
import studiranje.ip.model.UserPassword;
import studiranje.ip.model.UserRequisit;

/**
 * Табела корисника, очитавања и баратање, дакле схема старијих верзија 
 * релационих база података, преко сервиса (003YIKorisnici) 
 * уз прослеђивање апликативне УРИ адресе (од MySQL 8.0)
 * када су у питању базе података.  
 * @author mirko
 * @version 1.0
 */
public class DBUserDAOOldDBService implements DataSourceUserModel, DatabaseService, LoginEngine, DatabaseInitializable, SessionInitializable{ 
	private  CloseableHttpClient httpClient;
	private  String sessionId = "";
	private  String username = "";
	private  String password = "";
	
	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String popPassword() {
		try {
			return password; 
		}finally{
			password = ""; 
		}
	}
	
	public void pushPassword(String password) {
		this.password = password; 
	}

	@Override
	public void initializeSession() {
		if(httpClient!=null) return; 
		try {
			try {
				httpClient = HttpClients.createDefault();
				URL url = new URL(serviceAddress+"/UserResolveServlet");
				
				HttpPost httpPost =  new HttpPost(url.toString());
				httpPost.setEntity(new UrlEncodedFormEntity(new ArrayList<>(), Charset.forName("UTF-8")));
				CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
				sessionId = httpResponse.getFirstHeader("Set-Cookie").getValue(); 
			}catch(Exception ex) {
				throw ex;
			}
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	@Override
	public void initializeDatabase() {
		try {
			URL url = new URL(serviceAddress+"/DatabaseChangeServlet");
			
			HttpPost httpPost =  new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(new ArrayList<>(), Charset.forName("UTF-8")));
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("db_name", databaseAddress));

			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
		
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	
	@Override
	public void login(String username, String password) throws IOException {
		if(this.username!=null && this.username.trim().length()!=0) 
			if(!username.contentEquals(this.username))throw new RuntimeException("DUPLICATE LOGIN"); 
		
		try {
			URL url = new URL(serviceAddress+"/UserResolveServlet/LOGIN");
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			   
			postParameters.add(new BasicNameValuePair("username", username));
			postParameters.add(new BasicNameValuePair("password", password));
			postParameters.add(new BasicNameValuePair("operation", "LOGIN"));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message); 
			
			this.username = username;
		}catch(Exception ex) {
			username = ""; 
			throw ex;
		}
	}
	
	@Override
	public void logout() {
		if(username==null || username.trim().length()==0) {
			boolean x = true; if(x) return; 
			throw new RuntimeException("DUPLICATE LOGOUT"); 
		}
		try {
			URL url = new URL(serviceAddress+"/UserResolveServlet/LOGOUT");
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			   
			postParameters.add(new BasicNameValuePair("operation", "LOGOUT"));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message); 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}finally {
			username = "";
		}
	}
	
	@Override
	public void logout(String user) {
		if(username==null || username.trim().length()==0) throw new RuntimeException("DUPLICATE (GENERAL) LOGOUT"); 
		try {
			URL url = new URL(serviceAddress+"/UserResolveServlet/LOGOUT_ALL_SESSIONS_FOR_USER");
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			   
			postParameters.add(new BasicNameValuePair("operation", "LOGOUT_ALL_SESSIONS_FOR_USER"));
			postParameters.add(new BasicNameValuePair("username", user));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message); 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}finally {
			username = ""; 
		}
	}

	@Override
	public void logoutAll() {
		logout(username);
	}
	
	@Override
	public boolean isLogged(){
		return username != null && username.trim().length() != 0; 
	}
	
	public String loggedUsername() {
		return username;
	}
	
	@Override
	public void insert(UserDTO dto) throws SQLException {
		if(username.trim().length()!=0) {
			throw new RuntimeException("USER DUPLICATE"); 
		}
		String username = dto.getUser().getUsername();
		String password = dto.getPassword().getPlainPassword();
		String password2 = dto.getPassword().getPlainPassword();
		String name = dto.getUser().getFirstname(); 
		String surname = dto.getUser().getSecondname();
		String email = dto.getUser().getEmail(); 
		
		try {
			if(!password.contentEquals(password2)) throw new RuntimeException("PASSWORD 12 MISSMATCH"); 
			URL url = new URL(serviceAddress+"/UserResolveServlet/REGISTER"); 
			
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			   
			postParameters.add(new BasicNameValuePair("username", username));
			postParameters.add(new BasicNameValuePair("password", password));
			postParameters.add(new BasicNameValuePair("operation", "REGISTER"));
			postParameters.add(new BasicNameValuePair("firstname", name));
			postParameters.add(new BasicNameValuePair("secondname", surname));
			postParameters.add(new BasicNameValuePair("useremail", email));
			postParameters.add(new BasicNameValuePair("old_password", password));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			if(success) this.username = username;
			
			if(!success) throw new RuntimeException(root.get("message").getAsString()); 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public void update(String oldUsername, UserDTO neoUser) throws SQLException {
		if(username==null || username.trim().length()==0) 
			throw new RuntimeException("NO LOGGED USER"); 
		
		try {
			URL url = new URL(serviceAddress+"/UserResolveServlet/UPDATE"); 
			HttpPost httpPost = new HttpPost(url.toString());
			
			String neoUsername = neoUser.getUser().getUsername(); 
			String neoName = neoUser.getUser().getFirstname();
			String neoSurname = neoUser.getUser().getSecondname(); 
			String neoEmail = neoUser.getUser().getEmail(); 
			String neoPassword = neoUser.getPassword().getPlainPassword(); 
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("operation", "UPDATE"));
			postParameters.add(new BasicNameValuePair("old_password", password));
			postParameters.add(new BasicNameValuePair("username", neoUsername));
			postParameters.add(new BasicNameValuePair("password", neoPassword));
			postParameters.add(new BasicNameValuePair("firstname", neoName));
			postParameters.add(new BasicNameValuePair("secondname", neoSurname));
			postParameters.add(new BasicNameValuePair("useremail", neoEmail));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message); 
			
			username = neoUsername;
		}
		catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);  
		}
	}

	@Override
	public void delete(String username) throws SQLException {
		try {
			if(username==null || username.trim().length()==0) {
				throw new RuntimeException("USER NOT FOUND"); 
			}
			
			URL url = new URL(serviceAddress+"/UserResolveServlet/DELETE"); 
			HttpPost httpPost = new HttpPost(url.toString());
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("operation", "DELETE"));
			postParameters.add(new BasicNameValuePair("old_password", password));
			postParameters.add(new BasicNameValuePair("username", username));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean();
			String message = root.get("message").getAsString(); 
			
			if(!success) throw new RuntimeException(message); 
			
			username = ""; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public UserDTO getFullDTO(String username) throws SQLException {
		return get(username);
	}

	@Override
	public UserDTO get(String username) throws SQLException {
		try {
		 UserInfo info = getUser(username);
		 UserPassword password = getPassword(username); 
		 UserRequisit requisit = getRequisit(username); 
		 UserDTO dto = new UserDTO(info, password, requisit); 
		 dto.setDescription(getDescription(username));
		 return dto; 
		}catch(Exception ex) {
			return null; 
		}
	}

	@Override
	public UserRequisit getRequisit(String username) throws SQLException {
		UserRequisit ur = new UserRequisit();
		try {
			URL url = new URL(serviceAddress+"/CountryUserInfoServlet?detail=full"); 
			if(username==null || username.trim().length()==0) 
				throw new RuntimeException("NO LOGGED USER");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			String message = root.get("message").getAsString();
			boolean success = root.get("success").getAsBoolean();
			
			if(!success) throw new RuntimeException(message);
			
			JsonObject data = root.get("personal").getAsJsonObject()
					 			  .get("extended").getAsJsonObject()
					              .get("country").getAsJsonObject();
			
			JsonObject info = root.get("personal").getAsJsonObject()
		              			  .get("extended").getAsJsonObject();
			
			JsonObject appResources = 
				  root.get("personal").getAsJsonObject()
		 			  .get("extended").getAsJsonObject()
		              .get("profile").getAsJsonObject();
			
			
			
			String country =  (data.get("country")==null)? "":(data.get("country").isJsonNull()?"":data.get("country").getAsString());
			String telephone = (info.get("telephone")==null)? "":(info.get("telephone").isJsonNull()?"":info.get("telephone").getAsString());
			String city = (info.get("address")==null)?"":(info.get("address").isJsonNull()?"":info.get("address").getAsString());
			
			String countryPict = data.get("country_picture")==null?"":(data.get("country_picture").isJsonNull()?"":data.get("country_picture").getAsString());
			String userPict    = appResources.get("user_picture")==null?"":(appResources.get("user_picture").isJsonNull()?"":appResources.get("user_picture").getAsString()); 
			String profilePict = appResources.get("profile_picture")==null?"":(appResources.get("profile_picture").isJsonNull()?"":appResources.get("profile_picture").getAsString()); 
			
			ur.setUsername(username);
			ur.setCity(city);
			ur.setCountry(country);
			ur.setTelephone(telephone);
			
			if(countryPict.trim().length()>0)
				ur.setCountryFlagPicture(new File(UserFileSystemPathConstants.COUNTRY_FLAG_IMAGES, countryPict));
			
			if(userPict.trim().length()!=0)
				ur.setUserPicture(new File(UserFileSystemPathConstants.USER_IMAGES, userPict));
			
			if(profilePict.trim().length()!=0)
				ur.setProfilePicture(new File(UserFileSystemPathConstants.PROFILE_IMAGES, profilePict));
		
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		try {
			URL url = new URL(serviceAddress+"/UserFlagsGetServlet");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean notificationWeb   = root.get("user.notification.webapp.support").getAsBoolean();
			boolean notificationEmail = root.get("user.notification.email.support").getAsBoolean();
			
			ur.setWebappNotifications(notificationWeb);
			ur.setEmailNotifications(notificationEmail);
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		try {
			URL url = new URL(serviceAddress+"/UserRealPicturesInfoServlet");
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			if(httpResponse.getCode()!=200) {
				throw new RuntimeException("SERVICE NOT FOUND OR ERROR");
			}
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			
			JsonElement countryImage = root.get("image.country");
			JsonElement profileImage = root.get("image.profile");
			JsonElement userImage    = root.get("image.user");
			
			if(countryImage==null) ur.setCountryFlagPicture(null);
			if(profileImage==null) ur.setProfilePicture(null);
			if(userImage==null)	   ur.setUserPicture(null);
			
			if(countryImage!=null) ur.setCountryFlagPicture(new File(UserFileSystemPathConstants.COUNTRY_FLAG_IMAGES, countryImage.getAsString()));
			if(profileImage!=null) ur.setProfilePicture(new File(UserFileSystemPathConstants.PROFILE_IMAGES, profileImage.getAsString()));
			if(userImage!=null)    ur.setUserPicture(new File(UserFileSystemPathConstants.USER_IMAGES, userImage.getAsString()));
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		return ur;
	}

	@Override
	public UserInfo getUser(String username) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/CountryUserInfoServlet?detail=full"); 
			if(username==null || username.trim().length()==0) 
				throw new RuntimeException("NO LOGGED USER");
			
			HttpPost httpPost = new HttpPost(url.toString());
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("username", username));
			
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			String message = root.get("message").getAsString();
			boolean success = root.get("success").getAsBoolean();
			if(!success) throw new RuntimeException(message);
			
			JsonObject info = root.get("personal").getAsJsonObject()
					              .get("basic").getAsJsonObject();
			
			
			String firstname = info.get("fname").isJsonNull()?"":info.get("fname").getAsString(); 
			String secondname = info.get("sname").isJsonNull()?"":info.get("sname").getAsString(); 
			String email = root.get("email").isJsonNull()?"":root.get("email").getAsString(); 
			String usernameCheck = (root.get("username")==null)?"":(root.get("username").isJsonNull()?"":root.get("username").getAsString());
			
			if(usernameCheck.trim().length()==0) return null; 
			UserInfo ui = new UserInfo(username, firstname, secondname, email); 
			
			return ui; 
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public UserPassword getPassword(String username) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/CountryUserInfoServlet?detail=full"); 
			if(username==null || username.trim().length()==0) 
				throw new RuntimeException("NO LOGGED USER");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			String message = root.get("message").getAsString();
			boolean success = root.get("success").getAsBoolean();
			
			if(!success) throw new RuntimeException(message);
			
			JsonObject info = root.get("personal").getAsJsonObject()
					              .get("basic").getAsJsonObject();
			
			
			String passwordRecord = info.get("passwd").getAsString();
			UserPassword up = new UserPassword(passwordRecord, true); 
			return up; 
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getEmail(String username) throws SQLException {
		return getUser(username).getEmail();
	}

	@Override
	public boolean existsUser(String username) throws SQLException {
		try {
			getUser(username); 
			return true;
		}catch(Exception ex) {
			return false; 
		}
	}

	@Override
	public boolean existsEmail(String email) throws SQLException {
		try {
			return getUser(username).getEmail().trim().length()!=0; 
		}catch(Exception ex) {
			return false; 
		}
	}

	@Override
	public void updateProfilePicture(String username, String profilePicturePath) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UpdateProfileImageServlet"); 
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("image_file", profilePicturePath));
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
			
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public void updateUserPicture(String username, String userPicturePath) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UpdateUserImageServlet"); 
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("image_file", userPicturePath));
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
			
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public void updateCountryFlagPicture(String username, String countryFlagImage) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UpdateCountryImageServlet"); 
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("image_file", countryFlagImage));
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
			
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public void updateRequisite(String oldUsername, UserRequisit neoData) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UserRequisiteUpdateServlet"); 
			
			if(neoData==null) neoData = new UserRequisit();
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.addHeader("Accept",  "application/json");
			httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
			httpPost.addHeader("Accept-Encoding", "UTF-8");
			
			Gson gson = new GsonBuilder().create(); 
			String content = gson.toJson(neoData).toString(); 
			httpPost.setEntity(new StringEntity(content, Charset.forName("UTF-8")));
			
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message);
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	@Override
	public String getDescription(String username) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UserDescriptionGetServlet");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			String text = new String(httpResponse.getEntity().getContent().readAllBytes(), "UTF-8"); 
			if(httpResponse.getCode()!=200) throw new RuntimeException("SERVICE ERROR");
			
			return text; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public void setDescription(String username, String description) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UserDescriptionSetServlet");
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();			
			postParameters.add(new BasicNameValuePair("user_description", description));
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message); 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	@Override
	public UserFlagsDTO getUserFlags(String username) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UserFlagsGetServlet");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean notificationWeb   = root.get("user.notification.webapp.support").getAsBoolean();
			boolean notificationEmail = root.get("user.notification.email.support").getAsBoolean();
			boolean userSessionSupport = root.get("user.session.control.support").getAsBoolean();
			
			UserFlagsDTO dto = new UserFlagsDTO();
			UserFlags ufg = new UserFlags(username); 
			
			ufg.setWebNotifications(notificationWeb);
			ufg.setEmailNotifications(notificationEmail);
			ufg.setUserSessionsControl(userSessionSupport);
			
			dto.setConfigurations(ufg);
			return dto; 
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void putUserFlags(UserFlagsDTO dto) throws SQLException {
		try {
			URL url = new URL(serviceAddress+"/UserFlagsSetServlet");
			
			ArrayList<NameValuePair> postParameters = new ArrayList<>();			
			postParameters.add(new BasicNameValuePair("unws", Boolean.toString(dto.getConfigurations().isWebNotifications())));
			postParameters.add(new BasicNameValuePair("unes", Boolean.toString(dto.getConfigurations().isEmailNotifications())));
			postParameters.add(new BasicNameValuePair("uscs", Boolean.toString(dto.getConfigurations().isUserSessionsControl())));
			postParameters.add(new BasicNameValuePair("control", ""));
			
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, Charset.forName("UTF-8")));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean success = root.get("success").getAsBoolean(); 
			String message = root.get("message").getAsString();
			
			if(!success) throw new RuntimeException(message); 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}

	public boolean getSessionsManervarity() {
		try {
			URL url = new URL(serviceAddress+"/UserListServlet");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 
			
			boolean userSessionSupport = root.get("user.session.control.support").getAsBoolean();
			return userSessionSupport;
		}catch(RuntimeException ex) {
			throw ex; 
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private String databaseAddress = ""; 
	private String serviceAddress = ""; 
	
	@Override
	public String getDatabaseAddress() {
		return databaseAddress;
	}

	@Override
	public void setDatabaseAddress(String databaseAddress) {
		if(databaseAddress==null) databaseAddress = "";
		this.databaseAddress = databaseAddress;
		setDatabaseAddress(this.databaseAddress);
		this.initializeSession();
		this.initializeDatabase();
	}
	
	@Override
	public String getServiceAddress() {
		return serviceAddress;
	}
	
	@Override
	public void setServiceAddress(String serviceAddress) {
		if(serviceAddress==null) serviceAddress = ""; 
		this.serviceAddress = serviceAddress;
		this.initializeSession();
		this.initializeDatabase();
	}
	
	@Override
	public boolean exisitsDatabaseAddress() {
		return this.databaseAddress.trim().length()!=0;
	}
	
	@Override
	public boolean existsServiceAddress() {
		return this.serviceAddress.trim().length()!=0;
	}
	
	public DBUserDAOOldDBService(String databaseAddress, String serviceAddress) {
		if(databaseAddress == null) throw new RuntimeException();
		if(serviceAddress==null) throw new RuntimeException();
		this.databaseAddress = databaseAddress; 
		this.serviceAddress = serviceAddress;
	}
	
	public int countUsers() throws IOException {
			URL url = new URL(serviceAddress+"/UserListInfoServlet");
			
			HttpPost httpPost = new HttpPost(url.toString());
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); 
			
			JsonParser parser = new JsonParser(); 
			JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 

			return root.get("count").getAsInt(); 
	}
	
	public int countUsers(PageBean bean) throws IOException {
		URL url = new URL(serviceAddress+"/UserListInfoServlet?start_filter="+URLEncoder.encode(bean.getStartFilter(),"UTF-8"));
		
		HttpGet httpGet = new HttpGet(url.toString());
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet); 
		
		JsonParser parser = new JsonParser(); 
		JsonObject root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonObject(); 

		return root.get("count").getAsInt(); 
	}
	
	public List<UserDTO> getUsers() throws IOException{
		URL url = new URL(serviceAddress+"/UserListServlet");
		
		HttpPost httpPost = new HttpPost(url.toString());
		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		
		JsonParser parser = new JsonParser(); 
		JsonArray root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonArray(); 
		
		ArrayList<UserDTO> users = new ArrayList<>();
		for(int i=0; i<root.size(); i++) {
			JsonObject object = root.get(i).getAsJsonObject();
			String username = (object.get("username")==null)? "":object.get("username").getAsString(); 
			String firstname = (object.get("firstname")==null)? "":object.get("firstname").getAsString(); 
			String secondname = (object.get("secondname")==null)? "":object.get("secondname").getAsString(); 
			String email = (object.get("useremail")==null)? "":object.get("useremail").getAsString();
			String telephone = (object.get("telephone")==null)? "":object.get("telephone").getAsString();
			String country =  (object.get("country")==null)? "":object.get("country").getAsString();  
			String city =  (object.get("city")==null)? "":object.get("city").getAsString();  
			UserInfo ui = new UserInfo(username,firstname, secondname, email); 
			UserRequisit ur = new UserRequisit(); 
			ur.setUsername(username);
			ur.setCity(city);
			ur.setCountry(country);
			ur.setTelephone(telephone);
			UserDTO dto = new UserDTO(ui, null, ur); 
			users.add(dto);
		}
		return users; 
	}
	
	public List<UserDTO> getUsers(PageBean page) throws IOException{
		URL url = new URL(serviceAddress+"/UserListServlet?page_no="+page.getPageNo()+"&page_size="+page.getPageSize()+"&start_filter="+URLEncoder.encode(page.getStartFilter(),"UTF-8"));
		
		HttpGet httpGet = new HttpGet(url.toString());
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		
		JsonParser parser = new JsonParser(); 
		JsonArray root = parser.parse(new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8")).getAsJsonArray(); 
		
		ArrayList<UserDTO> users = new ArrayList<>();
		for(int i=0; i<root.size(); i++) {
			JsonObject object = root.get(i).getAsJsonObject();
			String username = (object.get("username").isJsonNull())? "":object.get("username").getAsString(); 
			String firstname = (object.get("firstname").isJsonNull())? "":object.get("firstname").getAsString(); 
			String secondname = (object.get("secondname").isJsonNull())? "":object.get("secondname").getAsString(); 
			String email = (object.get("useremail").isJsonNull())? "":object.get("useremail").getAsString();
			String telephone = (object.get("telephone").isJsonNull())? "":object.get("telephone").getAsString();
			String country =  (object.get("country").isJsonNull())? "":object.get("country").getAsString();  
			String city =  (object.get("city").isJsonNull())? "":object.get("city").getAsString();  
			UserInfo ui = new UserInfo(username,firstname, secondname, email); 
			UserRequisit ur = new UserRequisit(); 
			ur.setUsername(username);
			ur.setCity(city);
			ur.setCountry(country);
			ur.setTelephone(telephone);
			UserDTO dto = new UserDTO(ui, null, ur); 
			users.add(dto);
		}
		return users; 
	}
}

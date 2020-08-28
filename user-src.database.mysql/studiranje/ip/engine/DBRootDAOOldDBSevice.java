package studiranje.ip.engine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;

import studiranje.ip.database.model.DBRecordInfo;
import studiranje.ip.database.model.DBTableInfo;
import studiranje.ip.database.model.DBUserData;
import studiranje.ip.engine.controller.DatabaseService;
import studiranje.ip.engine.model.DataSourceRootModel;
import studiranje.ip.engine.util.DatabaseServiceException;
import studiranje.ip.service.object.DatabaseListRequest;
import studiranje.ip.service.object.DatabaseListResponse;
import studiranje.ip.service.object.TableDescriptionRequest;
import studiranje.ip.service.object.TableDescriptionResponse;
import studiranje.ip.service.object.TableListRequest;
import studiranje.ip.service.object.TableListResponse;
import studiranje.ip.service.object.UserListRequest;
import studiranje.ip.service.object.UserListResponse;

/**
 * Коријенскa схема, очитавања и баратање, дакле схема старијих верзија 
 * релационих база података, преко сервиса (003YIKorisnici) 
 * уз прослеђивање апликативне УРИ адресе (од MySQL 8.0)
 * када су у питању базе података.  
 * @author mirko
 * @version 1.0
 */
public class DBRootDAOOldDBSevice implements DataSourceRootModel, DatabaseService{ 
	@Override
	public List<String> getDatabases() throws SQLException {
		List<String> result = new ArrayList<>(); 
		DatabaseListRequest request = new DatabaseListRequest();
		request.setDatabaseAPIAddress(databaseAddress);
		ClientConfig config = new ClientConfig()
							.register(DatabaseListRequest.class)
							.register(DatabaseListResponse.class); 
		Client client = ClientBuilder.newClient(config);
		WebTarget resource = client.target(serviceAddress); 
		DatabaseListResponse resp = resource.request(MediaType.APPLICATION_JSON).post(Entity.entity(request, MediaType.APPLICATION_JSON), DatabaseListResponse.class);
		for(String database: resp.getDatabases()) 
			result.add(database); 
		return result; 
	}

	@Override
	public List<String> getTables(String database) throws SQLException {
		List<String> result = new ArrayList<>(); 
		TableListRequest request = new TableListRequest();
		request.setDatabaseAPIAddress(databaseAddress);
		ClientConfig config = new ClientConfig()
				.register(TableListRequest.class)
				.register(TableListResponse.class); 
		Client client = ClientBuilder.newClient(config);
		WebTarget resource = client.target(serviceAddress); 
		try {
			request.setDatabaseName(database);
			TableListResponse resp = resource.request(MediaType.APPLICATION_JSON).post(Entity.entity(request, MediaType.APPLICATION_JSON), TableListResponse.class);
			for(DBTableInfo info: resp.getTableInfo()) 
				result.add(info.getTableName());
			return result; 
		}catch(Exception ex) {
			throw new DatabaseServiceException(ex);  
		}
	}

	@Override
	public DBTableInfo getTableInfo(String database, String tablename) throws SQLException {
		DBTableInfo result = new DBTableInfo(); 
		TableDescriptionRequest request = new TableDescriptionRequest();
		request.setDatabaseAPIAddress(databaseAddress);
		ClientConfig config = new ClientConfig()
				.register(TableDescriptionRequest.class)
				.register(TableDescriptionResponse.class); 
		Client client = ClientBuilder.newClient(config);
		WebTarget resource = client.target(serviceAddress); 
		try {
			request.setDatabaseName(database);
			request.setTableName(tablename);
			TableDescriptionResponse resp = resource.request(MediaType.APPLICATION_JSON).post(Entity.entity(request, MediaType.APPLICATION_JSON), TableDescriptionResponse.class);
			for(DBRecordInfo info: resp.getColumns()) 
				result.add(info.getField(), info);
			return result; 
		}catch(Exception ex) {
			throw new DatabaseServiceException(ex);  
		}
	}

	@Override
	public List<DBUserData> getUsers() throws SQLException {
		UserListRequest request = new UserListRequest(); 
		request.setDatabaseAPIAddress(databaseAddress);
		ClientConfig config = new ClientConfig()
							.register(UserListRequest.class)
							.register(UserListResponse.class); 
		Client client = ClientBuilder.newClient(config);
		WebTarget resource = client.target(serviceAddress); 
		UserListResponse resp = resource.request(MediaType.APPLICATION_JSON).post(Entity.entity(request, MediaType.APPLICATION_JSON), UserListResponse.class);
		return resp.getUsers(); 
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
	}
	
	@Override
	public String getServiceAddress() {
		return serviceAddress;
	}
	
	@Override
	public void setServiceAddress(String serviceAddress) {
		if(serviceAddress==null) serviceAddress = ""; 
		this.serviceAddress = serviceAddress;
	}
	
	@Override
	public boolean exisitsDatabaseAddress() {
		return this.databaseAddress.trim().length()!=0;
	}
	
	@Override
	public boolean existsServiceAddress() {
		return this.serviceAddress.trim().length()!=0;
	}	
	
	public DBRootDAOOldDBSevice(String databaseAddress, String serviceAddress) {
		if(databaseAddress == null) throw new RuntimeException();
		if(serviceAddress==null) throw new RuntimeException();
		this.databaseAddress = databaseAddress; 
		this.serviceAddress = serviceAddress;
	}
}

package studiranje.ip.engine.controller;

import studiranje.ip.engine.DBRootDAOOldDBSevice;
import studiranje.ip.engine.DBUserDAOOldDBService;

/**
 * Опис односно контролна спона за механизам серивисирања, база
 * података, односно старих база података. 
 * @author mirko
 * @version 1.0
 */
public class OldDBServiceDescriptor implements DataSourceDescriptor, DatabaseService{
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
	
	@Override
	public DBRootDAOOldDBSevice getRootDS() {
		return new DBRootDAOOldDBSevice(databaseAddress, serviceAddress);
	}

	@Override
	public DBUserDAOOldDBService getUserDS() {
		return new DBUserDAOOldDBService(databaseAddress, serviceAddress);
	}
}

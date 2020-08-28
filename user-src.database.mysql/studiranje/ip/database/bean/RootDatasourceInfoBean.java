package studiranje.ip.database.bean;

import java.io.Serializable;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

/**
 * Стања при избору извора података. 
 * @author mirko
 * @version 1.0
 */
public class RootDatasourceInfoBean implements Serializable{
	private static final long serialVersionUID = 1782291030007289665L;
	private String databaseAddress = ""; 
	private String serviceAddress = ""; 
	private String filedirPath = "";
	private String engineAddress = ""; 
	private String name = ""; 
	private String type = "";
	
	
	public String getOriginalDatabaseAddress() {
		return databaseAddress;
	}
	public String getDatabaseAddress() {
		try {
			URI uri = new URI(getOriginalDatabaseAddress());
			String userinfo = uri.getUserInfo();
			if(userinfo!=null && userinfo.split(":").length>1) 
				uri = UriBuilder.fromUri(uri).userInfo(userinfo.split(":")[0]).build();
			return uri.toString();
		}catch(Exception ex) {
			return "";
		}
	}
	public void setOriginalDatabaseAddress(String databaseAddress) {
		if(databaseAddress==null) databaseAddress = ""; 
		this.databaseAddress = databaseAddress;
	}
	public void setDatabaseAddress(String databaseAddress) {
		if(databaseAddress==null) databaseAddress = ""; 
		this.databaseAddress = databaseAddress;
	}
	
	public String getServiceAddress() {
		return serviceAddress;
	}
	public void setServiceAddress(String serviceAddress) {
		if(serviceAddress==null) serviceAddress = ""; 
		this.serviceAddress = serviceAddress;
	}
	
	public String getFiledirPath() {
		return filedirPath;
	}
	public void setFiledirPath(String filedirPath) {
		if(filedirPath==null) serviceAddress = ""; 
		this.filedirPath = filedirPath;
	}
	
	public String getEngineAddress() {
		return engineAddress;
	}
	public void setEngineAddress(String engineAddress) {
		if(engineAddress==null) engineAddress = ""; 
		this.engineAddress = engineAddress;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name==null) name=""; 
		this.name = name;
	}
	
	public String getType() {
		if(type==null) type=""; 
		return type;
	}
	public void setType(String type) {
		this.type = type;
	} 
	
	public String apply(RootDatasourceInfoBean bean) {
		if(bean==null) bean = new RootDatasourceInfoBean();
		setName(bean.getName());
		setType(bean.getType());
		setDatabaseAddress(bean.getDatabaseAddress());
		setServiceAddress(bean.getEngineAddress());
		setEngineAddress(bean.getEngineAddress());
		setFiledirPath(bean.getFiledirPath());
		return "";
	}
	
	public String apply(RootDatasourceInfoStateBean bean) {
		if(bean == null) return apply((RootDatasourceInfoBean)null);
		setName(bean.getChoosedDatasource());
		setType(bean.getCurrentDSType());
		setDatabaseAddress(bean.getCurrentDatabaseAddress());
		setServiceAddress(bean.getCurrentServiceAddress());
		setEngineAddress(bean.getCurrentEngineAddress());
		setFiledirPath(bean.getCurrentFiledirPath());
		return "";
	}
}

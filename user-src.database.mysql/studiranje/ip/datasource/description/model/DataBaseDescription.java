package studiranje.ip.datasource.description.model;


import studiranje.ip.database.controller.DataSourceType;
import studiranje.ip.datasource.description.util.Stringizable;

/**
 * Дескриптори за базе података, као типове података. 
 * @author mirko
 * @version 1.0
 */
public class DataBaseDescription<DESC extends Stringizable> extends DataSourceDescription<DESC>{
	private static final long serialVersionUID = 7523523398523264776L;
	
	public DESC getDatabase() {
		DESC database = get("database");
		if(database==null) throw new DataSourceDescriptorException("UNDEFINED DATABASE");
		return database;
	}
	
	public boolean correct() {
		try {getDatabase();} catch(Exception ex) {return false;}
		return super.correct() && getType()==DataSourceType.DATABASE;  
	}
	
	public void exception() {
		if(!correct()) throw new DataSourceDescriptorException("NOT DATABASE"); 
	}
	
}

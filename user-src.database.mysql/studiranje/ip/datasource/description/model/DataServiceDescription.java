package studiranje.ip.datasource.description.model;



import studiranje.ip.database.controller.DataSourceType;
import studiranje.ip.datasource.description.util.Stringizable;

/**
 * Функционалности које су предвиђене за веб сервис као извор података.
 * @author mirko
 * @version 1.0
 */
public class DataServiceDescription<DESC extends Stringizable> extends DataSourceDescription<DESC>{
	private static final long serialVersionUID = -3058121348393744762L;
	
	public DESC getService() {
		DESC database = get("service");
		if(database==null) throw new DataSourceDescriptorException("UNDEFINED SERVICE");
		return database;
	}
	
	public boolean correct() {
		try {getService();}catch(Exception ex) {return false;}
		return super.correct() && getType()==DataSourceType.SERVICE;  
	}
	
	public void exception() {
		if(!correct()) throw new DataSourceDescriptorException("NOT SERVICE"); 
	}
}

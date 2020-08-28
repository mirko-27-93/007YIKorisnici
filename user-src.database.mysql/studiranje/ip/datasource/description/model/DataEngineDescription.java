package studiranje.ip.datasource.description.model;

import studiranje.ip.database.controller.DataSourceType;
import studiranje.ip.datasource.description.util.Stringizable;

/**
 * Опис локалне машине за управљање подацима. 
 * @author mirko
 * @version 1.0
 */
public class DataEngineDescription<DESC extends Stringizable> extends DataSourceDescription<DESC> {
	private static final long serialVersionUID = 7364242125312326686L;
	
	public DESC getEngine() {
		DESC database = get("engine");
		if(database==null) throw new DataSourceDescriptorException("UNDEFINED ENGINE");
		return database;
	}
	
	public boolean correct() {
		try {getEngine();}catch(Exception ex) {return false;}
		return super.correct() && getType()==DataSourceType.ENGINE;  
	}
	
	public void exception() {
		if(!correct()) throw new DataSourceDescriptorException("NOT ENGINE"); 
	}
}

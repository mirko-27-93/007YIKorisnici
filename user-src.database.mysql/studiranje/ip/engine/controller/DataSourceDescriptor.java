package studiranje.ip.engine.controller;

import studiranje.ip.engine.model.DataSourceRootModel;
import studiranje.ip.engine.model.DataSourceUserModel;

/**
 * Описна имена која се користе за спецификацију извора података, 
 * односно генераторе механизама којима им се приступа.  
 * @author mirko
 * @version 1.0
 */
public interface DataSourceDescriptor {
	public DataSourceRootModel getRootDS(); 
	public DataSourceUserModel getUserDS();
}

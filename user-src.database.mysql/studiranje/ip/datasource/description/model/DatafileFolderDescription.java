package studiranje.ip.datasource.description.model;

import studiranje.ip.database.controller.DataSourceType;
import studiranje.ip.datasource.description.util.Stringizable;

/**
 * Модел дескриптора када је извор подарака датотека или директоријум 
 * са системом датотеке, које се у комбинацији са машином и контролером 
 * могу искристити за смјештање и управљање подацима. 
 * @author mirko
 * @version 1.0
 */
public class DatafileFolderDescription <DESC extends Stringizable> extends DataSourceDescription<DESC>{
	private static final long serialVersionUID = -8423625355691586775L;
	
	public DESC getFileOrDir() {
		DESC database = get("filedir");
		if(database==null) throw new DataSourceDescriptorException("UNDEFINED FILE OR DIR PATH");
		return database;
	}
	
	public boolean correct() {
		try {getFileOrDir();}catch(Exception ex) {return false;}
		return super.correct() && getType()==DataSourceType.FILEDIR;  
	}
	
	public void exception() {
		if(!correct()) throw new DataSourceDescriptorException("NOT FILE OR DIR PTAH"); 
	}
}

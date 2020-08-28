package studiranje.ip.datasource.description.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studiranje.ip.database.controller.DataSourceType;
import studiranje.ip.datasource.description.util.Stringizable;

/**
 * Општа варијанта, геренализација описа за извор података. 
 * @author mirko
 * @version 1.0
 */
public class DataSourceDescription<DESC extends Stringizable> implements Serializable{
	private static final long serialVersionUID = -7962643427346607177L;
	protected HashMap<String, DESC> description = new HashMap<>(); 
	
	public synchronized Map<String, DESC> get(){
		return new HashMap<>(description);
	}
	
	public synchronized void set(HashMap<String, DESC> map) {
		this.description = new HashMap<>(map); 
	}
	
	public synchronized DESC get(String field) {
		DESC value =  description.get(field); 
		return value;
	}
	
	public DESC getName() {
		DESC name = get("name");
		if(name==null) throw new DataSourceDescriptorException("UNNDAMED");
		return name;
	}
	
	public DataSourceType getType() {
		try {
			DataSourceType type = DataSourceType.valueOf(get("type").toString());
			if(type==null) throw new NullPointerException(); 
			return type;
		}catch(Exception ex) {
			throw new DataSourceDescriptorException("UNNTYPED");
		}
	}
	
	public boolean correct() {
		try {
			getName();
			getType();
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	public void exception() {
		try {
			getName();
			getType();
		}catch(Exception ex) {
			throw new DataSourceDescriptorException("NOT DATA SOURCE");
		}
	}
	
	public DataSourceDescription<DESC> asDataSource() {
		return (DataSourceDescription<DESC>) this; 
	}
	
	public DataBaseDescription<DESC> asDatabase() {
		try {return (DataBaseDescription<DESC>) this;}
		catch(Exception ex) {return null;}
	}
	
	public boolean isDatabase() {
		try{return getType()==DataSourceType.DATABASE;}
		catch(Exception ex) {return false;}
	}
	
	public synchronized DataBaseDescription<DESC> productDatabase() { 
		DataBaseDescription<DESC> desc = new DataBaseDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public synchronized DataBaseDescription<DESC> productDatabaseIf() {
		if(!isDatabase()) return null; 
		DataBaseDescription<DESC> desc = new DataBaseDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public DataServiceDescription<DESC> asDataservice() {
		try {return (DataServiceDescription<DESC>) this;}
		catch(Exception ex) {return null;}
	}
	
	public boolean isDataservice() {
		try{return getType()==DataSourceType.SERVICE;}
		catch(Exception ex) {return false;}
	}
	
	public synchronized DataServiceDescription<DESC> productDataservice() { 
		DataServiceDescription<DESC> desc = new DataServiceDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public synchronized DataServiceDescription<DESC> productDataserviceIf() {
		if(!isDatabase()) return null; 
		DataServiceDescription<DESC> desc = new DataServiceDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public DataEngineDescription<DESC> asDataengine() {
		try {return (DataEngineDescription<DESC>) this;}
		catch(Exception ex) {return null;}
	}
	
	public boolean isDataengine() {
		try{return getType()==DataSourceType.ENGINE;}
		catch(Exception ex) {return false;}
	}
	
	public synchronized DataEngineDescription<DESC> productDataengine() {
		DataEngineDescription<DESC> desc = new DataEngineDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public synchronized DataEngineDescription<DESC> productDataengineIf() {
		if(!isDatabase()) return null; 
		DataEngineDescription<DESC> desc = new DataEngineDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public DatafileFolderDescription<DESC> asDatafiledir() {
		try {return (DatafileFolderDescription<DESC>) this;}
		catch(Exception ex) {return null;}
	}
	
	public boolean isDatafiledir() {
		try{return getType()==DataSourceType.FILEDIR;}
		catch(Exception ex) {return false;}
	}
	
	public synchronized DatafileFolderDescription<DESC> productDatafiledir() {
		DatafileFolderDescription<DESC> desc = new DatafileFolderDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public synchronized DatafileFolderDescription<DESC> productDatafiledirIf() {
		if(!isDatabase()) return null; 
		DatafileFolderDescription<DESC> desc = new DatafileFolderDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc; 
	}
	
	public synchronized DataSourceDescription<DESC> product(){
		DataSourceDescription<DESC> desc = new DataSourceDescription<>(); 
		desc.description = new HashMap<>(description);
		return desc;
	}
	
	public DataSourceDescription<DESC> product(Class<? extends DataSourceDescription<?>> clazz){
		if(clazz==null) return null;
		if(clazz.getName().contentEquals(DataSourceDescription.class.getName())) return product(); 
		if(clazz.getName().contentEquals(DataServiceDescription.class.getName())) return productDataservice();
		if(clazz.getName().contentEquals(DataEngineDescription.class.getName())) return productDataengine();
		if(clazz.getName().contentEquals(DataBaseDescription.class.getName())) return productDatabase();
		if(clazz.getName().contentEquals(DatafileFolderDescription.class.getName())) return productDatafiledir();
		return null;
	}
	
	public DataSourceDescription<DESC> product(DataSourceType type){
		if(type==null) return null;
		switch(type) {
			case DATABASE: return productDatabase(); 
			case SERVICE: return productDataservice();
			case ENGINE: return productDataengine();
			case FILEDIR : return productDatafiledir();
			default: return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Class<? extends DataSourceDescription<?>>> getClassesByParametersExisting(){
		ArrayList<Class<? extends DataSourceDescription<?>>> types = new ArrayList<>();
		types.add((Class<? extends DataSourceDescription<?>>)DataSourceDescription.class);
		if(get("database")!=null) types.add((Class<? extends DataSourceDescription<?>>) DataBaseDescription.class);
		if(get("service")!=null) types.add((Class<? extends DataSourceDescription<?>>) DataServiceDescription.class);
		if(get("engine")!=null) types.add((Class<? extends DataSourceDescription<?>>) DataServiceDescription.class);
		if(get("filedir")!=null) types.add((Class<? extends DataSourceDescription<?>>) DatafileFolderDescription.class);
		return types; 
	}
	
	public List<DataSourceType> getTypesByParametersExisting(){
		ArrayList<DataSourceType> types = new ArrayList<>();
		if(get("database")!=null) types.add(DataSourceType.DATABASE);
		if(get("service")!=null) types.add(DataSourceType.SERVICE);
		if(get("engine")!=null) types.add(DataSourceType.ENGINE);
		if(get("filedir")!=null) types.add(DataSourceType.FILEDIR);
		return types; 
	}
	
	@SuppressWarnings("unchecked")
	public boolean check(Class<? extends DataSourceDescription<?>> c1, Class<? extends DataSourceDescription<?>> ... cn) {
		List<Class<? extends DataSourceDescription<?>>> active = getClassesByParametersExisting();
		if(!active.contains(c1)) return false;
		if(!active.containsAll(Arrays.asList(cn))) return false;
		return true;
	}
	
	public boolean checkViaClass(Class<? extends DataSourceDescription<?>> c1, Collection<Class<? extends DataSourceDescription<?>>> cn) {
		if(cn.size()==0) throw new NullPointerException("ZERO.EMPTY");
		List<Class<? extends DataSourceDescription<?>>> active = getClassesByParametersExisting();
		if(!active.containsAll(cn)) return false;
		return true;
	}
	
	public boolean check(DataSourceType t1, DataSourceType ... tn) {
		List<DataSourceType> active = getTypesByParametersExisting();
		if(!active.contains(t1)) return false;
		if(!active.containsAll(Arrays.asList(tn))) return false;
		return true;
	}
	
	public boolean checkViaTypes(Collection<DataSourceType> tn) {
		if(tn.size()==0) throw new NullPointerException("ZERO.EMPTY");
		List<DataSourceType> active = getTypesByParametersExisting();
		if(!active.containsAll(tn)) return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> test(Class<? extends DataSourceDescription<?>> ... args){
		HashMap<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(Class<? extends DataSourceDescription<?>> arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			if(product.getClass().equals(arg))
				map.put(arg, product); 
		}
		return map; 
	}
	

	public Map<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> testViaClasses(Collection<Class<? extends DataSourceDescription<?>>> args){
		HashMap<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(Class<? extends DataSourceDescription<?>> arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			if(product.getClass().equals(arg))
				map.put(arg, product); 
		}
		return map; 
	}
	
	public Map<DataSourceType, DataSourceDescription<DESC>> test(DataSourceType ... args){
		HashMap<DataSourceType, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(DataSourceType arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			map.put(arg, product); 
		}
		return map; 
	}
	
	public Map<DataSourceType, DataSourceDescription<DESC>> testViaTypes(Collection<DataSourceType>  args){
		HashMap<DataSourceType, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(DataSourceType arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			map.put(arg, product); 
		}
		return map; 
	}
	
	@SuppressWarnings("unchecked")
	public Map<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> reform(Class<? extends DataSourceDescription<?>> ... args){
		HashMap<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(Class<? extends DataSourceDescription<?>> arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			if(product.getClass().equals(arg)) {
				if(arg.getName().contentEquals(DataBaseDescription.class.getName())) product.description.get("type").fromString(DataSourceType.DATABASE.toString());
				if(arg.getName().contentEquals(DataServiceDescription.class.getName())) product.description.get("type").fromString(DataSourceType.SERVICE.toString());
				if(arg.getName().contentEquals(DataEngineDescription.class.getName())) product.description.get("type").fromString(DataSourceType.ENGINE.toString());
				if(arg.getName().contentEquals(DatafileFolderDescription.class.getName())) product.description.get("type").fromString(DataSourceType.FILEDIR.toString());
				map.put(arg, product); 
			}
		}
		return map; 
	}
	

	public Map<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> reformViaClasses(Collection<Class<? extends DataSourceDescription<?>>> args){
		HashMap<Class<? extends DataSourceDescription<?>>, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(Class<? extends DataSourceDescription<?>> arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			if(product.getClass().equals(arg)) {
				if(arg.getName().contentEquals(DataBaseDescription.class.getName())) product.description.get("type").fromString(DataSourceType.DATABASE.toString());
				if(arg.getName().contentEquals(DataServiceDescription.class.getName())) product.description.get("type").fromString(DataSourceType.SERVICE.toString());
				if(arg.getName().contentEquals(DataEngineDescription.class.getName())) product.description.get("type").fromString(DataSourceType.ENGINE.toString());
				if(arg.getName().contentEquals(DatafileFolderDescription.class.getName())) product.description.get("type").fromString(DataSourceType.FILEDIR.toString());
				map.put(arg, product); 
			}
		}
		return map; 
	}
	
	public Map<DataSourceType, DataSourceDescription<DESC>> reform(DataSourceType ... args){
		HashMap<DataSourceType, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(DataSourceType arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			switch(arg) {
				case DATABASE: 
					 product.description.get("type").fromString(DataSourceType.DATABASE.toString());
					break;
				case ENGINE: 
					 product.description.get("type").fromString(DataSourceType.ENGINE.toString());
					break;
				case SERVICE:
					 product.description.get("type").fromString(DataSourceType.SERVICE.toString());
					break;
				case FILEDIR:
					 product.description.get("type").fromString(DataSourceType.FILEDIR.toString());
					break;
			}
			map.put(arg, product); 
		}
		return map; 
	}
	
	public Map<DataSourceType, DataSourceDescription<DESC>> reformViaTypes(Collection<DataSourceType>  args){
		HashMap<DataSourceType, DataSourceDescription<DESC>> map = new HashMap<>(); 
		for(DataSourceType arg: args) {
			DataSourceDescription<DESC> product = product(arg);
			if(product==null) continue;
			map.put(arg, product); 
			switch(arg) {
				case DATABASE: 
					 product.description.get("type").fromString(DataSourceType.DATABASE.toString());
					break;
				case ENGINE: 
					 product.description.get("type").fromString(DataSourceType.ENGINE.toString());
					break;
				case SERVICE:
					 product.description.get("type").fromString(DataSourceType.SERVICE.toString());
					break;
				case FILEDIR:
					 product.description.get("type").fromString(DataSourceType.FILEDIR.toString());
					break;
			}
		}
		return map; 
	}
	
	public DataSourceDescription<DESC> productAsType(){
		switch(getType()) {
			case DATABASE: 
				 return productDatabase();
			case ENGINE: 
				 return productDataengine(); 
			case SERVICE:
				 return productDataservice();
			case FILEDIR:
				 return productDatafiledir();
			default:  return product(); 
		}
	}
}

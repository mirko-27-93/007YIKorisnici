package studiranje.ip.engine.controller;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Основна апстракција када је у питању баратање са класама. 
 * Иницијално намијењено за класе са функционалностима и извршне, 
 * али уптребљиво за све класе. Баратање преко називима и креирање, 
 * претворбе и провјере објеката. 
 * @author mirko
 * @version 1.0
 */
public class BasicEngine implements GeneralEngine, Serializable{
	private static final long serialVersionUID = 8299481170330041487L;
	private String engineAddress = ""; 
	
	@Override
	public String getEngineAddress() {
		return engineAddress;
	}

	@Override
	public boolean exisitsEngineAddress() {
		return engineAddress.trim().length()>0;
	}

	@Override
	public void setEngineAddress(String engineAddress) {
		if(engineAddress==null) engineAddress = ""; 
		this.engineAddress = engineAddress; 
	}
	
	public Class<?> getClazz() {
		try {
			return Class.forName(engineAddress);
		}catch(Exception ex) {
			return null;
		}
	}
	
	public Object getObject(Object ... params) {
		try {
			Class<?>[] classesParam = new Class<?>[params.length];
			for(int i=0; i<classesParam.length; i++) 
				classesParam[i] = params[i].getClass(); 
			Class<?> klasa = getClazz();
			Constructor<?> construct = klasa.getConstructor(classesParam);
			return construct.newInstance(params);
		}catch(Exception ex) {
			return null; 
		}
	}
	
	public boolean instanceofThis(Object o) {
		Class<?> clazz = getClazz();
		if(clazz==null) return false;
		return clazz.isInstance(o);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T cast(Object o) {
		try {
			return (T) o;
		}catch(Exception ex) {
			return null;
		}
	}
	
	public boolean isSuperClass(Class<?> class2){
		Class<?> clazz = getClazz();
		if(clazz==null) return false;
		if(class2==null) return false;
		while(true) {
			try {
				if(class2.equals(clazz)) return true;
				clazz = clazz.getSuperclass(); 
			}catch(Exception ex) {
				return false;
			}
		}
	}
	
	public boolean isSuperInterface(Class<?> class2) {
		Class<?> clazz = getClazz();
		if(clazz==null) return false;
		if(class2==null) return false;
		if(class2.equals(clazz)) return true;
		for(Class<?> cl: clazz.getInterfaces()) {
			if(class2.equals(clazz)) return true;
			boolean sint = isSuperInterface(cl);
			if(sint==true) return true;
		}
		return false;
	}
	
	public boolean isSuperType(Class<?> class2) {
		return isSuperClass(class2) || isSuperInterface(class2);
	}
}

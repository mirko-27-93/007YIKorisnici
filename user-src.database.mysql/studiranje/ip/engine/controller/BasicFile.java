package studiranje.ip.engine.controller;

import java.io.File;
import java.io.Serializable;

/**
 * Основно окружење када су у питњу дајлови односно путања о тим датотекама.
 * @author mirko
 * @version 1.0
 */
public class BasicFile implements GeneralFile, Serializable{
	private static final long serialVersionUID = 6409470528195718526L;
	private String file = ""; 
	
	@Override
	public String getFileAddress() {
		return file;
	}

	@Override
	public boolean exisitsFileAddress() {
		return file.trim().length()!=0;
	}

	@Override
	public void setFileAddress(String filePath) {
		if(filePath==null) filePath = ""; 
		this.file = filePath;
	}
	
	public File getFile() {
		return new File(file); 
	}
	
	public boolean isSubfilePhysical(File file) {
		try {
			String cp1 = getFile().getCanonicalPath(); 
			String cp2 = file.getCanonicalPath(); 
			return cp2.startsWith(cp1);
		}catch(Exception ex) {
			return false;
		}
	}
	
	public boolean isSubfileLogical(File file) {
		try {
			String cp1 = getFile().getPath(); 
			String cp2 = file.getPath(); 
			return cp2.startsWith(cp1);
		}catch(Exception ex) {
			return false;
		}
	}
}

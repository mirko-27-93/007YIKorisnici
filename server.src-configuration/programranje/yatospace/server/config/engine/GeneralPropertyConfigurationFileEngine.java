package programranje.yatospace.server.config.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import programranje.yatospace.server.config.controller.GeneralInitializerInterface;

/**
 * Општи оквир очитавања својстава из датотеке, односно 
 * у облику формата записа својстава. 
 * @author mirko
 * @version 1.0
 */
public class GeneralPropertyConfigurationFileEngine implements GeneralInitializerInterface{
	private File location; 
	private Properties content = new Properties();
	
	public GeneralPropertyConfigurationFileEngine(File file) {
		if(file == null) throw new NullPointerException("CONFIG FILE DECLARING");
		this.location = file; 
	}
	
	@Override
	public void initalize() {
		try {
			if(!location.exists()) location.createNewFile(); 
			content.clear();
			content.load(new InputStreamReader(new FileInputStream(location),"UTF-8"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	} 
	
	public void loadFromFile() {
		try {
			content.clear();
			content.load(new InputStreamReader(new FileInputStream(location), "UTF-8"));
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void storeToFile() {
		try {
			content.store(new OutputStreamWriter(new FileOutputStream(location),"UTF-8"), "");
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public File getLocation() {
		return location;
	}

	public void setLocation(File location) {
		this.location = location;
	}

	public Properties getContent() {
		return content;
	}

	public void setContent(Properties content) {
		this.content = content;
	}
}

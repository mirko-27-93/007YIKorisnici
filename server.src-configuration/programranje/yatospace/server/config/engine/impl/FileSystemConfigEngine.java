package programranje.yatospace.server.config.engine.impl;

import java.io.File;

import programranje.yatospace.server.config.engine.GeneralPropertyConfigurationResourceEngine;

/**
 * Поставке које се односе на адресе сервиса које се користе у апликацији. 
 * @author mirko
 * @version 1.0
 */
public class FileSystemConfigEngine extends GeneralPropertyConfigurationResourceEngine{

	public FileSystemConfigEngine(String dir) {
		super(new File(dir, "filesystem.properties"), "/programranje/yatospace/server/config/resources/filesystem.properties");
	}
	
	@Override
	public void initalize() {
		super.initalize();
	}
	
	public String getFilesystemPath() {
		return getContent().getProperty("filesystem"); 
	}
	
	public String getCountryLocalDBDirPath() {
		return getContent().getProperty("fileinput.country.local"); 
	}
	
	public String getCountryEUServiceDBDirPath() {
		return getContent().getProperty("fileinput.country.service.eu");
	}
}

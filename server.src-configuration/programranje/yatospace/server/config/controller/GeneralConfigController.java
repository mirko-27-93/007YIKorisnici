package programranje.yatospace.server.config.controller;

import java.io.File;

import programranje.yatospace.server.config.engine.impl.FileSystemConfigEngine;

/**
 * Опште мјесто за конфигурације. 
 * @author mirko
 * @version 1.0
 */
public class GeneralConfigController implements GeneralInitializerInterface, GeneralFinalizerInterface{
	public static final GeneralConfigController mainAppConfigEngine = new GeneralConfigController();
	
	public final static  String GENERAL_FILE_FOLDER_DIR = "C:\\Users\\MV\\Documents\\Eclipse\\eclipse-workspace-2\\007YIKorisnici"; 
	public final static  String GENERAL_CONFIGURATION_DIR = "C:\\Users\\MV\\Documents\\Eclipse\\eclipse-workspace-2\\007YIKorisnici\\configuration"; 
	
	public void initGeneralConfigDir() {
		try {
			File file = new File(GENERAL_CONFIGURATION_DIR);
			if(!file.exists()) file.mkdirs();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean initialized; 
	private boolean finalized; 
	
	@Override
	public void finish() {
		if(finalized) return; 
		finalized = true; 
	}

	@Override
	public void initalize() {
		if(initialized) return; 
		initGeneralConfigDir();
		fsc.initalize();
		initialized = true; 
	} 
	
	public final FileSystemConfigEngine fsc = new FileSystemConfigEngine(GENERAL_CONFIGURATION_DIR);
	
	public boolean isInitialized() {
		return initialized;
	}

	public boolean isFinalized() {
		return finalized;
	}
}

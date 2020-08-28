package programranje.yatospace.server.config.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GeneralPropertyConfigurationResourceEngine extends GeneralPropertyConfigurationFileEngine{
	private String resource; 
	
	public GeneralPropertyConfigurationResourceEngine(File file, String resource) {
		super(file);
		if(resource == null) throw new RuntimeException();
		this.resource = resource; 
	}
	
	@Override
	public void initalize() {
		try {
			if(!getLocation().exists()) {
				getLocation().createNewFile();
				loadFromResource();
				storeToFile();
			}
			getContent().clear();
			getContent().load(new InputStreamReader(new FileInputStream(getLocation()),"UTF-8"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	} 
	
	public void loadFromResource() {
		try {
			InputStream is = getClass().getResourceAsStream(resource);
			getContent().clear();
			getContent().load(new InputStreamReader(is,"UTF-8"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}

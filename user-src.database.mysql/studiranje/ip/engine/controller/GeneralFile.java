package studiranje.ip.engine.controller;

/**
 * Апстракција класа које морају садржавати информације о путањи за 
 * датотеке. Било да је коријенски фолдер или сама информација о 
 * датотеци засебно или намјенски.
 * @author mirko
 * @version 1.0
 */
public interface GeneralFile {
	public String getFileAddress();
	public boolean exisitsFileAddress();
	public void setFileAddress(String databaseAddress);
}

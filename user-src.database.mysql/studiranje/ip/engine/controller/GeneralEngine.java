package studiranje.ip.engine.controller;

/**
 * Општи оквир за класе које садрже информације о класи, њено пуно име 
 * или неком интерфејсу, односно пребројавању и анотацији ... 
 * Обично је извршна или има одређене функционалности, па 
 * је зато назив машина. 
 * @author mirko
 * @version 1.0
 */
public interface GeneralEngine {
	public String getEngineAddress();
	public boolean exisitsEngineAddress();
	public void setEngineAddress(String databaseAddress);
}

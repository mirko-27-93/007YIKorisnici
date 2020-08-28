package studiranje.ip.engine.controller;

/**
 * Општи оквир за објекте који садрже базе података.
 * @author mirko
 * @version 1.0
 */
public interface GeneralDatabase {
	public String getDatabaseAddress();
	public boolean exisitsDatabaseAddress();
	public void setDatabaseAddress(String databaseAddress);
}

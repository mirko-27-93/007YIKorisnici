package studiranje.ip.datasource.description.util;

/**
 * Чува информације о адреси базе података. 
 * @author mirko
 * @version 1.0
 */
public interface DatabaseAddressable {
	public String getDatabaseAddress(); 
	public void setDatabaseAddress(String database); 
	
	public default DatabaseAddressable asDatabaseAddressable() {
		return (DatabaseAddressable) this;
	}
}

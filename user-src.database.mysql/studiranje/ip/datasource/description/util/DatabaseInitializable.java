package studiranje.ip.datasource.description.util;

/**
 * Иницијализационе рутине за удаљене базе података. 
 * @author mirko
 * @version 1.0
 */
public interface DatabaseInitializable {
	public void initializeDatabase();
	
	public default DatabaseInitializable asDatabaseInitializable() {
		return (DatabaseInitializable) this; 
	}
}

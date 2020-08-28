package studiranje.ip.datasource.description.util;

/**
 * Иницијализација сесије на удаљеном сервису. 
 * @author mirko
 * @version 1.0
 */
public interface SessionInitializable {
	public void initializeSession();
	
	public default SessionInitializable asSessionInitializable() {
		return (SessionInitializable) this;
	}
}

package studiranje.ip.datasource.description.model;

/**
 * Изузеци који се бацају услед неправилног дескриптора. 
 * @author mirko
 * @version 1.0
 */
public class DataSourceDescriptorException extends RuntimeException{
	private static final long serialVersionUID = 6055606914272124685L;

	public DataSourceDescriptorException() {
		super();
	}

	public DataSourceDescriptorException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSourceDescriptorException(String message) {
		super(message);
	}

	public DataSourceDescriptorException(Throwable cause) {
		super(cause);
	}
}

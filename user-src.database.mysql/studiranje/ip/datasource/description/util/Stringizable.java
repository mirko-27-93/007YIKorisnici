package studiranje.ip.datasource.description.util;

import java.io.Serializable;

/**
 * Класа којом је предвиђена конверзија из и у стринг. 
 * @author mirko
 * @version 1.0
 */
public interface Stringizable extends Serializable{
	public String toString(); 
	public void fromString(String arg);
}

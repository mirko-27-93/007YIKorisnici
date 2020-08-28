package studiranje.ip.datasource.description.util;

/**
 * Реализација објекта промјењиве стринга. 
 * @author mirko
 *
 */
public final class BasicString implements Stringizable{
	private static final long serialVersionUID = -857690912379803230L;

	private String string = ""; 
	
	@Override
	public void fromString(String arg) {
		if(arg==null) arg = ""; 
		string = arg; 
	}
	
	@Override
	public String toString() {
		return string; 
	}
	
	public final  String originalString() {
		return super.toString(); 
	}
	
	public int originalCode() {
		return super.hashCode(); 
	}
}

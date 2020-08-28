package programiranje.yi.user.app.services.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Модел информација за државу. 
 * @author mirko
 * @version 1.0
 */
public class CountryInformationModel implements Serializable{
	private static final long serialVersionUID = 1889249619564373581L;
	private String country = "";
	private String a2c = ""; 
	private String a3c = ""; 
	private ArrayList<String> ccs = new ArrayList<>(); 
	private ArrayList<String> tlds = new ArrayList<>();
	private String countryFlagImage = ""; 
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getA2c() {
		return a2c;
	}
	public void setA2c(String a2c) {
		this.a2c = a2c;
	}
	public String getA3c() {
		return a3c;
	}
	public void setA3c(String a3c) {
		this.a3c = a3c;
	}
	public ArrayList<String> getCcs() {
		return ccs;
	}
	public void setCcs(ArrayList<String> ccs) {
		this.ccs = ccs;
	}
	public ArrayList<String> getTlds() {
		return tlds;
	}
	public void setTlds(ArrayList<String> tlds) {
		this.tlds = tlds;
	}
	public String getCountryFlagImage() {
		return countryFlagImage;
	}
	public void setCountryFlagImage(String countryFlagImage) {
		this.countryFlagImage = countryFlagImage;
	} 
}

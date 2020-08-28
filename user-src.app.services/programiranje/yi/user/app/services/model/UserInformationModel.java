package programiranje.yi.user.app.services.model;

import java.io.Serializable;

/**
 * Модел информација за корисника. 
 * @author mirko
 * @version 1.0
 */
public class UserInformationModel implements Serializable{
	private static final long serialVersionUID = -1791648634081356451L;
	private String username = ""; 
	private String email = ""; 
	private String firstName = "";
	private String secondName = ""; 
	private String passwordRecord = ""; 
	private String telephone = ""; 
	private String address = ""; 
	private String profileImage = ""; 
	private String userImage = "";
	private String description = "";
	private CountryInformationModel country = new CountryInformationModel(); 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getPasswordRecord() {
		return passwordRecord;
	}
	public void setPasswordRecord(String passwordRecord) {
		this.passwordRecord = passwordRecord;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CountryInformationModel getCountry() {
		return country;
	}
	public void setCountry(CountryInformationModel country) {
		this.country = country;
	} 
}

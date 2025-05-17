package com.medzone.model;
import java.time.LocalDateTime;

public class UserModel {
	private String userName;
	private String firstName;
	private String lastName;
	private String phone;
	private String password;
	private String email;
	private LocalDateTime registrationDate;
	private boolean isAdmin;
	private String imageUrl;
	
	public UserModel() {}

	public UserModel(String userName, String firstName, String lastName, String phone, String password, String email,
			LocalDateTime registrationDate, boolean isAdmin, String imageUrl) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.password = password;
		this.email = email;
		this.registrationDate = registrationDate;
		this.isAdmin = isAdmin;
		this.imageUrl = imageUrl;
	}
	
	public UserModel(String username, String password) {
		super();
		this.userName = username;
		this.password = password;
	}
	
	public UserModel(String firstName, String lastName, boolean is_admin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public UserModel(String userName, String firstName, String lastName, String phone, String email,
			LocalDateTime registrationDate, String imageUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.registrationDate = registrationDate;
		this.imageUrl = imageUrl;
	}
	
	public UserModel(String username, String firstName, String lastName, String phone, String email, String imageUrl) {
		super();
		this.userName = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.imageUrl = imageUrl;
	}
	
	public UserModel(String username, String firstName, String lastName) {
		super();
		this.userName = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserModel(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}

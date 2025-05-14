package com.medzone.model;
import java.time.LocalDate;

public class UserModel {
	private int id;
	private String firstName;
	private String lastName;
	private String userName;
	private String phone;
	private String password;
	private String email;
	private LocalDate registrationDate;
	private boolean isAdmin;
	
	public UserModel() {}

	public UserModel(int id, String firstName, String lastName, String userName, String phone, String password, String email,
			LocalDate registrationDate, boolean isAdmin) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.phone = phone;
		this.password = password;
		this.email = email;
		this.registrationDate = registrationDate;
		this.isAdmin = isAdmin;
	}
	
	public UserModel(String firstName, String lastName, String userName, String phone, String password, String email,
			LocalDate registrationDate, boolean isAdmin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.phone = phone;
		this.password = password;
		this.email = email;
		this.registrationDate = registrationDate;
		this.isAdmin = isAdmin;
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
	
	public UserModel(String firstName, String lastName, String userName, String phone, String email,
			LocalDate registrationDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.registrationDate = registrationDate;
	}
	
	public UserModel(String username, String firstName, String lastName, String phone, String email) {
		super();
		this.userName = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}
	
	public UserModel(String username, String firstName, String lastName) {
		super();
		this.userName = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}

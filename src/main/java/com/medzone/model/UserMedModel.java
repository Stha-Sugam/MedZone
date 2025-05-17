package com.medzone.model;

public class UserMedModel {
	private String username;
	private String medId;
	
	public UserMedModel() {};
	
	public UserMedModel(String username, String medId) {
		super();
		this.username = username;
		this.medId = medId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMedId() {
		return medId;
	}

	public void setMedId(String medId) {
		this.medId = medId;
	}
}

package com.medzone.model;

public class MedicineModel {
	private String name;
	private String brand;
	private String manufacturer;
	private String type;
	private String dosage;
	private String med_usage;
	private UserModel admin_id;
	
	public MedicineModel() {}

	public MedicineModel(String name, String brand, String manufacturer, String type, String dosage, String med_usage,
			UserModel admin_id) {
		super();
		this.name = name;
		this.brand = brand;
		this.manufacturer = manufacturer;
		this.type = type;
		this.dosage = dosage;
		this.med_usage = med_usage;
		this.admin_id = admin_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getMed_usage() {
		return med_usage;
	}

	public void setMed_usage(String med_usage) {
		this.med_usage = med_usage;
	}

	public UserModel getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(UserModel admin_id) {
		this.admin_id = admin_id;
	}
	
	
}

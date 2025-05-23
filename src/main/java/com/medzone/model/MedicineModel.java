package com.medzone.model;

import java.time.LocalDateTime;

public class MedicineModel {
	// attributes for the medicine model
	private String id;
	private String name;
	private String brand;
	private String form;
	private String strength;
	private String usage;
	private LocalDateTime addedDate;
	private String imageUrl;
	
	// empty constructor
	public MedicineModel() {}
	
	// constructors with different parameters
	public MedicineModel(String id, String name, String brand, String form, String strength, String usage, LocalDateTime addedDate, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
		this.addedDate = addedDate;
		this.imageUrl = imageUrl;
	}

	public MedicineModel(String name, String brand, String form, String strength, String usage, LocalDateTime addedDate) {
		super();
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
		this.addedDate = addedDate;
	}
	
	public MedicineModel(String id, String name, String brand, String form, String strength, String usage) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
	}
	
	public MedicineModel(String id, String name, String brand, String form, String strength, String usage, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
		this.imageUrl = imageUrl;
	}
	
	public MedicineModel(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	// getter and settor methods.
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public LocalDateTime getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDateTime addedDate) {
		this.addedDate = addedDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}

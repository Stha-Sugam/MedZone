package com.medzone.model;

import java.time.LocalDate;

public class MedicineModel {
	private String id;
	private String name;
	private String brand;
	private String form;
	private String strength;
	private String usage;
	private LocalDate addedDate;
	
	// empty constructor
	public MedicineModel() {}
	
	public MedicineModel(String id, String name, String brand, String form, String strength, String usage, LocalDate addedDate) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
		this.addedDate = addedDate;
	}

	public MedicineModel(String name, String brand, String form, String strength, String usage, LocalDate addedDate) {
		super();
		this.name = name;
		this.brand = brand;
		this.form = form;
		this.strength = strength;
		this.usage = usage;
		this.addedDate = addedDate;
	}

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

	public LocalDate getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDate addedDate) {
		this.addedDate = addedDate;
	}
	
	

	
}

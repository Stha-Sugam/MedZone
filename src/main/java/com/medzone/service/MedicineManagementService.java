package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.MedicineModel;

public class MedicineManagementService {
	private Connection dbConn;
	private boolean isConnectionError = false;

	// Constructor
	public MedicineManagementService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	public boolean DatabaseError() {
		return isConnectionError;
	}

	public ArrayList<MedicineModel> GetMedDetails() {
		ArrayList<MedicineModel> medicineList = new ArrayList<>();

		if (isConnectionError) {
			System.out.println("Connection Error!");
			return medicineList;
		}

		String extractQuery = "SELECT * FROM medicines";
		try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
			ResultSet medicine = stmt.executeQuery();

			while (medicine.next()) {
				medicineList.add(new MedicineModel(medicine.getString("med_id"), medicine.getString("name"), medicine.getString("brand"),
						medicine.getString("dosage_form"), medicine.getString("dosage_strength"), medicine.getString("med_usage"),
						medicine.getDate("added_date").toLocalDate()));
			}

			return medicineList;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public MedicineModel extractMedicine(String id) {
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String extractIdQuery = "SELECT med_id, name, brand, dosage_form, dosage_strength, med_usage FROM medicines WHERE med_id = ?";
		try (PreparedStatement extractStmt = dbConn.prepareStatement(extractIdQuery)) {
			extractStmt.setString(1, id);

			ResultSet foundMed = extractStmt.executeQuery();

			if (foundMed.next()) {
				return new MedicineModel(foundMed.getString("med_id"), foundMed.getString("name"), foundMed.getString("brand"),
				foundMed.getString("dosage_form"), foundMed.getString("dosage_strength"), foundMed.getString("med_usage"));
			}
		} catch (SQLException e) {
			System.err.println("Error during medicine extraction: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public Boolean addMedicine(MedicineModel medicine) {
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String insertQuery = "INSERT INTO medicines (med_id, name, brand, dosage_form, dosage_strength, med_usage, added_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
			insertStmt.setString(1, medicine.getId());
			insertStmt.setString(2, medicine.getName());
			insertStmt.setString(3, medicine.getBrand());
			insertStmt.setString(4, medicine.getForm());
			insertStmt.setString(5, medicine.getStrength());
			insertStmt.setString(6, medicine.getUsage());
			insertStmt.setObject(7, medicine.getAddedDate());

			return insertStmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error during medicine insertion: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public Boolean updateMedicine(MedicineModel medicine) {
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String updateQuery = "UPDATE medicines SET name = ?, brand = ?, dosage_form = ?, dosage_strength = ?, med_usage = ? WHERE med_id = ?";
		try (PreparedStatement updateStmt = dbConn.prepareStatement(updateQuery)) {
			updateStmt.setString(1, medicine.getName());
			updateStmt.setString(2, medicine.getBrand());
			updateStmt.setString(3, medicine.getForm());
			updateStmt.setString(4, medicine.getStrength());
			updateStmt.setString(5, medicine.getUsage());
			updateStmt.setString(6, medicine.getId());

			return updateStmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error during medicine update: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean deleteMedicine(String id) {
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}
		
		String deleteQuery = "DELETE FROM medicines WHERE med_id = ?";
		try(PreparedStatement deleteStmt = dbConn.prepareStatement(deleteQuery)) {
			deleteStmt.setString(1, id);
			
			int rowsDeleted = deleteStmt.executeUpdate();
			
			return rowsDeleted > 0;
			
		} catch (SQLException e) {
			System.err.println("Error during medicine update: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public Boolean CheckRegisteredId(String id) {
		String checkQuery = "SELECT med_id FROM medicines WHERE med_id = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, id);
			return stmt.executeQuery().next();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
}

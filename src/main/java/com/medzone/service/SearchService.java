package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.MedicineModel;

public class SearchService {
	private Connection dbConn;
	private boolean isConnectionError = false;

	// Constructor
	public SearchService() {
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
	
	public ArrayList<MedicineModel> extractMedSearchedDetails(String searchValue) {
		ArrayList<MedicineModel> medicineList = new ArrayList<>();

		if (isConnectionError) {
			System.out.println("Connection Error!");
			return medicineList;
		}

		String searchQuery = "SELECT * FROM medicines WHERE med_id LIKE ? OR name LIKE ? OR brand LIKE ?";
		try(PreparedStatement stmt = dbConn.prepareStatement(searchQuery)){
			stmt.setString(1, "%" + searchValue + "%");
			stmt.setString(2, "%" + searchValue + "%");
			stmt.setString(3, "%" + searchValue + "%");

			ResultSet foundMed = stmt.executeQuery();
			
			while (foundMed.next()) {
				medicineList.add(new MedicineModel(foundMed.getString("med_id"), foundMed.getString("name"), foundMed.getString("brand"), foundMed.getString("dosage_form"),
						foundMed.getString("dosage_Strength"), foundMed.getString("med_usage"), foundMed.getString("imageUrl")));
			}

			return medicineList;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
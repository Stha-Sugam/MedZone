package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.MedicineModel;
import com.medzone.model.UserModel;

public class DashboardService {
	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public DashboardService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}
	
	public int GetUserCount() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return -1;
		}
		
		String extractUserCountQuery = "SELECT COUNT(*) AS Total_Users FROM Users;";
		try(PreparedStatement stmt = dbConn.prepareStatement(extractUserCountQuery)){
			ResultSet userCount = stmt.executeQuery();

		        if (userCount.next()) {
		            return userCount.getInt("Total_Users");
		        } else {
		            return 0;
		        }
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int GetMedicineCount() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return -1;
		}
		
		String extractMedCountQuery = "SELECT COUNT(*) AS Total_Meds FROM Medicines;";
		try(PreparedStatement stmt = dbConn.prepareStatement(extractMedCountQuery)){
			ResultSet userCount = stmt.executeQuery();

		        if (userCount.next()) {
		            return userCount.getInt("Total_Meds");
		        } else {
		            return 0;
		        }
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public ArrayList<MedicineModel> GetRecentMeds() {
		ArrayList<MedicineModel> medicineList = new ArrayList<>();

		if (isConnectionError) {
			System.out.println("Connection Error!");
			return medicineList;
		}

		String extractQuery = "SELECT med_id, name FROM medicines ORDER BY added_date LIMIT 4";
		try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
			ResultSet medicine = stmt.executeQuery();

			while (medicine.next()) {
				medicineList.add(new MedicineModel(medicine.getString("med_id"), medicine.getString("name")));
			}

			return medicineList;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<UserModel> GetRecentUsers() {
		ArrayList<UserModel> usersList = new ArrayList<>();
		
		if (isConnectionError) {
			System.out.println("Connection Error!");
		}
		
		String extractQuery = "SELECT username, first_name, last_name  FROM users ORDER BY registration_date LIMIT 4";
		try(PreparedStatement stmt = dbConn.prepareStatement(extractQuery)){
			ResultSet user = stmt.executeQuery();
			
			while(user.next()) {
				usersList.add(new UserModel(user.getString("username"), user.getString("first_Name"), user.getString("last_name")));
			}
			
			return usersList;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}


package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.medzone.config.DbConfig;

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
}

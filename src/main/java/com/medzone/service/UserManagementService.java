package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.UserModel;

public class UserManagementService {
	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public UserManagementService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}
	
	public ArrayList<UserModel> GetUserDetails() {
		ArrayList<UserModel> usersList = new ArrayList<>();
		
		if (isConnectionError) {
			System.out.println("Connection Error!");
		}
		
		String extractQuery = "SELECT * FROM users";
		try(PreparedStatement stmt = dbConn.prepareStatement(extractQuery)){
			ResultSet user = stmt.executeQuery();
			
			while(user.next()) {
				usersList.add(new UserModel(user.getString("first_Name"), user.getString("last_name"), user.getString("username"), 
						user.getString("phone_number"), user.getString("email"), user.getDate("registration_date").toLocalDate()));
			}
			
			return usersList;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}

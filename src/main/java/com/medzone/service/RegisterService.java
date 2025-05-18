package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.medzone.model.UserModel;
import com.medzone.config.DbConfig;

/**
 * Service class responsible for handling user registration-related operations.
 * It includes methods for adding a new user and checking the uniqueness of username,
 * email, and phone number.
 */
public class RegisterService {
	
	// Instance variable for database connection
	private Connection dbConn;
	
	/**
	 * Constructor initializes the database connection.
	 * Logs an error if connection fails due to SQLException or ClassNotFoundException.
	 */
	public RegisterService() {
		try {
			// Initiating database connection
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			// Catches possible exceptions: database error or JDBC driver not found
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Adds a new user to the database using the information from a UserModel object.
	 *
	 * @param user the UserModel object containing the user's information
	 * @return true if the user is successfully added; null if an error occurs or connection is unavailable
	 */
	public Boolean addUser(UserModel user) {
		// Check if the database connection is available
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}
		
		// SQL query to insert a new user record into the users table
		String insertQuery = "INSERT INTO users (username, first_name, last_name, phone_number, password, email, registration_date, is_admin, imageUrl)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
			// Set query parameters from the UserModel object
			insertStmt.setString(1, user.getUserName());
			insertStmt.setString(2, user.getFirstName());
			insertStmt.setString(3, user.getLastName());
			insertStmt.setString(4, user.getPhone());
			insertStmt.setString(5, user.getPassword());
			insertStmt.setString(6, user.getEmail());
			insertStmt.setObject(7, user.getRegistrationDate());
			insertStmt.setBoolean(8, user.isAdmin());
			insertStmt.setString(9, user.getImageUrl());
			
			// Execute the insert operation; return true if at least one row is affected
			return insertStmt.executeUpdate() > 0;
		} catch (SQLException e) {
			// Log any SQL error that occurs
			System.err.println("Error during user registration: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks if a username already exists in the users table.
	 *
	 * @param username the username to check
	 * @return true if the username exists, false if not, or null if an error occurs
	 */
	public Boolean checkUsername(String username) {
		String checkQuery = "SELECT username FROM users WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, username);
			return stmt.executeQuery().next(); // returns true if a result is found
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks if an email already exists in the users table.
	 *
	 * @param email the email to check
	 * @return true if the email exists, false if not, or null if an error occurs
	 */
	public Boolean checkEmail(String email) {
		String checkQuery = "SELECT email FROM users WHERE email = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, email);
			return stmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks if a phone number already exists in the users table.
	 *
	 * @param phoneNum the phone number to check
	 * @return true if the phone number exists, false if not, or null if an error occurs
	 */
	public Boolean checkPhoneNum(String phoneNum) {
		String checkQuery = "SELECT phone_number FROM users WHERE phone_number = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, phoneNum);
			return stmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

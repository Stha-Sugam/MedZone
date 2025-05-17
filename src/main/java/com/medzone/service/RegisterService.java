package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.medzone.model.UserModel;

import com.medzone.config.DbConfig;

/**
 * 
 */
public class RegisterService {
	
	// declaring private instance variable for database connection
	private Connection dbConn;
	
	/**
	 * Constructor for class RegisterService
	 */
	public RegisterService() {
		try {
			// Initiating database connection
			this.dbConn = DbConfig.getDbConnection();
		}
		catch(SQLException | ClassNotFoundException ex){
			// catches two possible exceptions i.e; database error or JDBC driver not found
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param user the UserModel object that is to be created and stored in database.
	 * @return true if more than 0 rows were inserted into the database table otherwise null if not inserted.
	 */
	public Boolean addUser(UserModel user) {
		// checks for database connection setup.
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}
		
		// SQL insert query to add a new user to the users table in database.
		String insertQuery = "INSERT INTO users (username, first_name, last_name, phone_number, password, email, registration_date, is_admin, imageUrl)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Preparing the SQL statement using the dbConn database connection
		try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
			// Setting values for each of the placeholder in the query using UserModel getters.
			insertStmt.setString(1, user.getUserName());
			insertStmt.setString(2, user.getFirstName());
			insertStmt.setString(3, user.getLastName());
			insertStmt.setString(4, user.getPhone());
			insertStmt.setString(5, user.getPassword());
			insertStmt.setString(6, user.getEmail());
			insertStmt.setObject(7, user.getRegistrationDate());
			insertStmt.setBoolean(8, user.isAdmin());
			insertStmt.setString(9, user.getImageUrl());
			
			// executes the insert query and throws true if row is inserted.
			return insertStmt.executeUpdate() > 0;
		}
		// Catches and Logs any SQL error during the insertion.
		catch(SQLException e) {
			System.err.println("Error during user registration: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public Boolean checkUsername(String username) {
		String checkQuery = "SELECT username FROM users WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, username);
			return stmt.executeQuery().next();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean checkEmail(String email) {
		String checkQuery = "SELECT email FROM users WHERE email = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, email);
			return stmt.executeQuery().next();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean checkPhoneNum(String phoneNum) {
		String checkQuery = "SELECT phone_number FROM users WHERE phone_number = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(checkQuery)) {
			stmt.setString(1, phoneNum);
			return stmt.executeQuery().next();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

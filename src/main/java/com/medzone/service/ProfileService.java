package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.medzone.config.DbConfig;
import com.medzone.model.UserModel;
import com.medzone.util.PasswordUtil;

public class ProfileService {
	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public ProfileService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}
	
	public boolean DatabaseError(){
		if(isConnectionError) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Validates the user credentials against the database records.
	 *
	 * @param UserModel the UserModel object containing user credentials
	 * @return true if the user credentials are valid, false otherwise; null if a
	 *         connection error occurs
	 */
	public UserModel getUserInfo(UserModel user) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		String extractQuery = "SELECT username, first_name, last_name, phone_number, email, registration_date, imageUrl FROM users WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
			stmt.setString(1, user.getUserName());
			ResultSet foundUser = stmt.executeQuery();
			if (foundUser.next()) {
				return new UserModel(foundUser.getString("username"), foundUser.getString("first_name"), 
						foundUser.getString("last_name"), foundUser.getString("phone_number"),
						foundUser.getString("email"), foundUser.getTimestamp("registration_date").toLocalDateTime(), 
						foundUser.getString("imageUrl"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public Boolean updateUser(UserModel user) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}
		
		String updateQuery = "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, email = ?, imageUrl = ? WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(updateQuery)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getPhone());
			stmt.setString(4, user.getEmail());
			stmt.setString(5, user.getImageUrl());
			stmt.setString(6, user.getUserName());
			
			int rowsUpdated = stmt.executeUpdate();
			
	        return rowsUpdated > 0;
		} catch (SQLException e) {
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
	
	public Boolean checkPassword(UserModel user) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}
		
		String searchQuery = "SELECT username, password FROM users WHERE username = ?";
		try(PreparedStatement stmt = dbConn.prepareStatement(searchQuery)) {
			stmt.setString(1, user.getUserName());
			
			ResultSet foundUser = stmt.executeQuery();
			
			if (foundUser.next()) {
				return validatePassword(foundUser, user);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return false;
	}
	
	public Boolean updatePassword(UserModel user) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}
		
		String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(updateQuery)) {
			stmt.setString(1, user.getPassword());
			stmt.setString(2, user.getUserName());
			
			int rowsUpdated = stmt.executeUpdate();
			// returns true if one or more row affected or updated otherwise false.
	        return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Validates the password retrieved from the database.
	 *
	 * @param result       the ResultSet containing the username and password from
	 *                     the database
	 * @param UserModel the UserModel object containing user credentials
	 * @return true if the passwords match, false otherwise
	 * @throws SQLException if a database access error occurs
	 */
	private boolean validatePassword(ResultSet result, UserModel user) throws SQLException {
		String dbUsername = result.getString("username");
		String dbPassword = result.getString("password");

		return dbUsername.equals(user.getUserName())
				&& PasswordUtil.decrypt(dbPassword, dbUsername).equals(user.getPassword());
	}
}

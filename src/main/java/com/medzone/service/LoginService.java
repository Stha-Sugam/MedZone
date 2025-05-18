package com.medzone.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.medzone.config.DbConfig;
import com.medzone.model.UserModel;
import com.medzone.util.PasswordUtil;

/**
 * Service class for handling login operations. Connects to the database,
 * verifies user credentials, and returns login status.
 */
public class LoginService {

	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor initializes the database connection. Sets the connection error
	 * flag if the connection fails.
	 */
	public LoginService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	/**
	 * Validates the user credentials against the database records.
	 *
	 * @param UserModel the UserModel object containing user credentials
	 * @return true if the user credentials are valid, false otherwise; null if a
	 *         connection error occurs
	 */
	public Boolean loginUser(UserModel user) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		String query = "SELECT username, password, is_admin FROM users WHERE username = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setString(1, user.getUserName());
			ResultSet foundUser = stmt.executeQuery();
			
			if (foundUser.next()) {
				user.setAdmin(foundUser.getBoolean("is_admin"));
				return validatePassword(foundUser, user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return false;
	}
	
	/**
	 * 
	 * @param username username to be checked
	 * @return true if found
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
	
	public String extractUserImage(String username) {
		String extractQuery = "SELECT imageUrl FROM users WHERE username = ?";
		try(PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
			stmt.setString(1, username);
			ResultSet foundImage = stmt.executeQuery();
			if(foundImage.next()) {
				return foundImage.getString("imageUrl");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return null;
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
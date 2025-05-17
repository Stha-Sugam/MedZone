package com.medzone.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.TicketModel;

public class TicketService {
	// declaring private instance variable for database connection
	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor for class RegisterService
	 */
	public TicketService() {
		try {
			// Initiating database connection
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			// catches two possible exceptions i.e; database error or JDBC driver not found
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public Boolean addTicket(TicketModel ticket) {
		// checks for database connection setup.
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String insertQuery = "INSERT INTO tickets (subject, message, created_at, status)" + "VALUES (?, ?, ?, ?)";
		try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
			insertStmt.setString(1, ticket.getSubject());
			insertStmt.setString(2, ticket.getMessage());
			insertStmt.setObject(3, ticket.getCreatedDate());
			insertStmt.setString(4, ticket.getStatus());

			return insertStmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error during user registration: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public Boolean addUserTicket(String username, int ticketId) {
		// checks for database connection setup.
		if (dbConn == null) {
			System.err.println("Database connection is not available.");
			return null;
		}

		String insertQuery = "INSERT INTO user_tickets (username, ticket_id) VALUES (?, ?)";
		try (PreparedStatement stmt = dbConn.prepareStatement(insertQuery)) {
			stmt.setString(1, username);
			stmt.setInt(2, ticketId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error during user registration: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<TicketModel> getTicketsDetails() {
		ArrayList<TicketModel> ticketsList = new ArrayList<>();

		if (isConnectionError) {
			System.out.println("Connection Error!");
		}

		String extractQuery = "SELECT ut.username, t.ticket_Id, t.subject, t.message, t.created_at, t.status FROM tickets t JOIN user_tickets ut "
				+ "ON t.ticket_id = ut.ticket_id";
		try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
			ResultSet ticket = stmt.executeQuery();

			while (ticket.next()) {
				ticketsList.add(new TicketModel(ticket.getString("username"), ticket.getInt("ticket_Id"), ticket.getString("subject"),
						ticket.getString("message"), ticket.getTimestamp("created_at").toLocalDateTime(),
						ticket.getString("status")));
			}

			return ticketsList;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public int getLatestTicketId() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
		}

		String extractQuery = "SELECT ticket_id FROM tickets ORDER BY created_at DESC LIMIT 1";
		try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {

			ResultSet ticket = stmt.executeQuery();
			if(ticket.next()) {
				return ticket.getInt("ticket_id");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return -1;
		}
		return -1;
	}

	public Boolean updateTicketStatus(int ticketId, String status) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
		}
		
		String updateQuery = "Update tickets SET status = ? WHERE ticket_id = ?";
		try(PreparedStatement stmt = dbConn.prepareStatement(updateQuery)){
			stmt.setString(1, status);
			stmt.setInt(2, ticketId);
			
			return stmt.executeUpdate() > 0;
			
		}catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}		
	}
}

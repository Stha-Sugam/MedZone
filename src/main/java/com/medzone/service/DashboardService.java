package com.medzone.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.medzone.config.DbConfig;
import com.medzone.model.MedicineModel;
import com.medzone.model.TicketModel;
import com.medzone.model.UserModel;

/**
 * Service class providing various dashboard-related data retrieval methods
 * from the database such as counts, recent entries, and statistics.
 * Handles connection initialization and basic SQL querying.
 */
public class DashboardService {
	// Database connection instance
    private Connection dbConn;           
    // Flag indicating if DB connection failed
    private boolean isConnectionError = false; 

    /**
     * Constructor initializes the database connection.
     * Sets connection error flag to true if connection fails.
     */
    public DashboardService() {
        try {
            dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    /**
     * Retrieves the total number of users from the database.
     * @return total user count, -1 if connection error or SQL error occurs.
     */
    public int getUserCount() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return -1;
        }

        String extractUserCountQuery = "SELECT COUNT(*) AS Total_Users FROM Users;";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractUserCountQuery)) {
            ResultSet userCount = stmt.executeQuery();

            if (userCount.next()) {
                return userCount.getInt("Total_Users");
            } else {
                return 0; // No users found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the total number of medicines from the database.
     * @return total medicine count, -1 if connection error or SQL error occurs.
     */
    public int getMedicineCount() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return -1;
        }

        String extractMedCountQuery = "SELECT COUNT(*) AS Total_Meds FROM medicines;";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractMedCountQuery)) {
            ResultSet userCount = stmt.executeQuery();

            if (userCount.next()) {
                return userCount.getInt("Total_Meds");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the total number of tickets from the database.
     * @return total ticket count, -1 if connection error or SQL error occurs.
     */
    public int getTicketsCount() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return -1;
        }

        String extractTicketCountQuery = "SELECT COUNT(*) AS Total_Tickets FROM tickets;";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractTicketCountQuery)) {
            ResultSet ticketCount = stmt.executeQuery();

            if (ticketCount.next()) {
                return ticketCount.getInt("Total_Tickets");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the number of open tickets (status = 'Open') from the database.
     * @return count of open tickets, -1 if connection or SQL error occurs.
     */
    public int getOpenTicketsCount() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return -1;
        }

        String extractOpenTicketQuery = "SELECT COUNT(*) AS Total_Open FROM tickets WHERE status = 'Open';";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractOpenTicketQuery)) {
            ResultSet openCount = stmt.executeQuery();

            if (openCount.next()) {
                return openCount.getInt("Total_Open");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves a list of the 4 most recently added medicines.
     * Only includes the medicine ID and name.
     * @return list of MedicineModel objects; empty list if connection error; null if SQL error.
     */
    public ArrayList<MedicineModel> getRecentMeds() {
        ArrayList<MedicineModel> medicineList = new ArrayList<>();

        if (isConnectionError) {
            System.out.println("Connection Error!");
            return medicineList;
        }

        String extractQuery = "SELECT med_id, name FROM medicines ORDER BY added_date DESC LIMIT 4";
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

    /**
     * Retrieves a list of the 4 most recently registered users.
     * Only includes username, first name, and last name.
     * @return list of UserModel objects; empty list if connection error; null if SQL error.
     */
    public ArrayList<UserModel> getRecentUsers() {
        ArrayList<UserModel> usersList = new ArrayList<>();

        if (isConnectionError) {
            System.out.println("Connection Error!");
            return usersList;
        }

        String extractQuery = "SELECT username, first_name, last_name FROM users ORDER BY registration_date DESC LIMIT 4";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
            ResultSet user = stmt.executeQuery();

            while (user.next()) {
                usersList.add(new UserModel(user.getString("username"), user.getString("first_name"), user.getString("last_name")));
            }

            return usersList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a list of the 4 most recent tickets.
     * Includes username, ticket ID, and status.
     * @return list of TicketModel objects; empty list if connection error; null if SQL error.
     */
    public ArrayList<TicketModel> getRecentTickets() {
        ArrayList<TicketModel> ticketsList = new ArrayList<>();

        if (isConnectionError) {
            System.out.println("Connection Error!");
            return ticketsList;
        }

        String extractQuery = "SELECT ut.username, t.ticket_Id, t.status "
                + "FROM tickets t JOIN user_tickets ut ON t.ticket_id = ut.ticket_id ORDER BY t.created_at DESC LIMIT 4";
        try (PreparedStatement stmt = dbConn.prepareStatement(extractQuery)) {
            ResultSet ticket = stmt.executeQuery();

            while (ticket.next()) {
                ticketsList.add(new TicketModel(ticket.getString("username"), ticket.getInt("ticket_Id"), ticket.getString("status")));
            }

            return ticketsList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the name of the most viewed medicine based on user views.
     * @return medicine name if found, otherwise null.
     */
    public String getMostViewedMedicine() {
        String query = "SELECT m.name, COUNT(um.med_id) AS view_count FROM medicines m JOIN user_meds um ON m.med_id = um.med_id "
                + "GROUP BY m.med_id, m.name ORDER BY view_count DESC LIMIT 1";

        try (PreparedStatement stmt = dbConn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves the username of the most active user based on the sum of tickets created
     * and medicine views.
     * @return username if found, otherwise null.
     */
    public String getMostActiveUser() {
        String query = "SELECT ut.username, COUNT(DISTINCT ut.ticket_id) AS ticket_count, COUNT(DISTINCT um.med_id) AS view_count, "
                + "(COUNT(DISTINCT ut.ticket_id) + COUNT(DISTINCT um.med_id)) AS total_activity FROM user_tickets ut "
                + "LEFT JOIN user_meds um ON ut.username = um.username GROUP BY ut.username ORDER BY total_activity DESC "
                + "LIMIT 1";

        try (PreparedStatement stmt = dbConn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the total number of tickets created in the last 7 days.
     * @return count of tickets last week, or 0 if none found or error occurs.
     */
    public int getTicketCountLastWeek() {
        String query = "SELECT COUNT(*) AS total FROM tickets WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";

        try (PreparedStatement stmt = dbConn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

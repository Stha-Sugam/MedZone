package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.TicketService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class ManageTicketController
 * 
 * This servlet handles the management of support tickets. It allows admins to:
 * - View all submitted tickets.
 * - Update the status of a specific ticket.
 */
@WebServlet("/ManageTicket")
public class ManageTicketController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service instance to interact with the ticket database.
	private final TicketService ticketService = new TicketService();

    /**
     * Default constructor.
     */
    public ManageTicketController() {
        super();
    }

	/**
	 * Handles GET requests for displaying all ticket details to the admin.
	 * Retrieves all ticket entries and stores them in the session.
	 * Then forwards to TicketManagement.jsp for display.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Store ticket details in session for view rendering
		SessionUtil.setAttribute(request, "TicketDetails", ticketService.getTicketsDetails());

		// Forward to the JSP page for displaying ticket information
		request.getRequestDispatcher("WEB-INF/pages/TicketManagement.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests for updating a ticket's status.
	 * Receives the ticket ID and new status from the form, updates the database,
	 * and then redirects back to the ticket management page with appropriate messages.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Parse ticket ID and status from the form
			int id = Integer.parseInt(request.getParameter("ticketId"));
			String status = request.getParameter("status");

			// Attempt to update the ticket status in the database
			if (ticketService.updateTicketStatus(id, status)) {
				// On success, store success message and redirect
				request.setAttribute("successMessage", "Ticket updated successfully.");
			} else {
				// On failure, store error message
				request.setAttribute("errorMessage", "Ticket update failed. Try later.");
			}

			// Redirect to refresh ticket list
			response.sendRedirect("ManageTicket");

		} catch (Exception e) {
			e.printStackTrace();
			// Optional: log error or set error attribute if handling on UI
		}
	}
}

package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import com.medzone.model.TicketModel;
import com.medzone.service.TicketService;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class ContactController
 * Handles contact form submissions where users can send messages (tickets) to the support team.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Contact" })
public class ContactController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service responsible for ticket-related database operations
    private final TicketService ticketService = new TicketService();

    /**
     * Default constructor
     */
    public ContactController() {
        super();
    }

    /**
     * Handles GET requests - loads the Contact.jsp page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/Contact.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes the contact form submission.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        try {
            // Retrieve current user's username from session
            String username = (String) SessionUtil.getAttribute(request, "username");

            // Extract form input values
            String subject = request.getParameter("subject");
            String message = request.getParameter("message");

            // Store form input temporarily in session to prefill on validation error
            SessionUtil.setAttribute(request, "subjectValue", subject);
            SessionUtil.setAttribute(request, "messageValue", message);

            // Validate inputs
            String subjectError = validateSubject(request, subject);
            String messageError = validateMessage(request, message);

            // If validation errors exist, redirect back to form with error messages
            if (subjectError != null || messageError != null) {
                handleInputError(request, response, subjectError, messageError);
                return;
            }

            // Create a new Ticket object
            TicketModel ticket = new TicketModel(subject, message, LocalDateTime.now(), "Open");

            // Save ticket to database
            if (ticketService.addTicket(ticket)) {
                // Retrieve the ticket ID of the newly created ticket
                int id = ticketService.getLatestTicketId();

                // Associate the ticket with the current user
                if (ticketService.addUserTicket(username, id)) {
                    // Success: notify user and clear session attributes
                    request.setAttribute("successMessage", "Ticket has been successfully created. Check your email to get further updates.");
                    SessionUtil.removeAttribute(request, "subjectValue");
                    SessionUtil.removeAttribute(request, "messageValue");

                    request.getRequestDispatcher("/WEB-INF/pages/Contact.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            // Handle unexpected errors (e.g. DB issues)
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database connection Error.");
            request.getRequestDispatcher("/WEB-INF/pages/Contact.jsp").forward(request, response);
        }
    }

    /**
     * Validates the subject input field.
     * @param request HttpServletRequest
     * @param subject The subject input
     * @return An error message if invalid, otherwise null
     */
    public String validateSubject(HttpServletRequest request, String subject) {
        if (ValidationUtil.isNullOrEmpty(subject)) {
            return "Subject is Required.";
        }
        return null;
    }

    /**
     * Validates the message input field.
     * @param request HttpServletRequest
     * @param subject The message input
     * @return An error message if invalid, otherwise null
     */
    public String validateMessage(HttpServletRequest request, String subject) {
        if (ValidationUtil.isNullOrEmpty(subject)) {
            return "Message is Required.";
        }
        return null;
    }

    /**
     * Handles input validation errors by setting error messages and returning to the form.
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @param subjectError Error message for subject field
     * @param messageError Error message for message field
     */
    private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String subjectError, String messageError)
        throws ServletException, IOException {
        req.setAttribute("errorSubject", subjectError);
        req.setAttribute("messageError", messageError);
        req.getRequestDispatcher("WEB-INF/pages/Contact.jsp").forward(req, resp);
    }
}

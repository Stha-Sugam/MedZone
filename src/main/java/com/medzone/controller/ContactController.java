package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.model.TicketModel;
import com.medzone.service.TicketService;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;
import java.time.LocalDateTime;
/**
 * Servlet implementation class ContactController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Contact" })
public class ContactController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final TicketService ticketService = new TicketService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/Contact.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String username = (String )SessionUtil.getAttribute(request, "username");
			String subject = request.getParameter("subject");
			String message = request.getParameter("message");
			
			SessionUtil.setAttribute(request, "subjectValue", subject);
			SessionUtil.setAttribute(request, "messageValue", message);
			
			String subjectError = validateSubject(request, subject);
			String messageError = validateMessage(request, message);
			if (subjectError != null || messageError != null) {
				handleInputError(request, response, subjectError, messageError);
				return;
			}
			
			TicketModel ticket = new TicketModel(subject, message, LocalDateTime.now(), "Open");
			
			if (ticketService.addTicket(ticket)) {
				int id = ticketService.getLatestTicketId();
				if(ticketService.addUserTicket(username, id)) {
					request.setAttribute("successMessage", "Ticket has been successfully created. Check your email to get further updates.");
					SessionUtil.removeAttribute(request, "subjectValue");
					SessionUtil.removeAttribute(request, "messageValue");
					request.getRequestDispatcher("/WEB-INF/pages/Contact.jsp").forward(request, response);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Database connection Error.");
			request.getRequestDispatcher("/WEB-INF/pages/Contact.jsp").forward(request, response);
		}
		
		
	}
	
	public String validateSubject(HttpServletRequest request, String subject) {
		// Validation for subject
		if(ValidationUtil.isNullOrEmpty(subject)) {
			return "Subject is Required.";
		}
		return null;
	}
	
	public String validateMessage(HttpServletRequest request, String subject) {
		// Validation for subject
		if(ValidationUtil.isNullOrEmpty(subject)) {
			return "Message is Required.";
		}
		return null;
	}
	
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String subjectError, String messageError) 
			throws ServletException, IOException {
		req.setAttribute("errorSubject", subjectError);
		req.setAttribute("messageError", messageError);
		req.getRequestDispatcher("WEB-INF/pages/Contact.jsp").forward(req, resp);
	}

}

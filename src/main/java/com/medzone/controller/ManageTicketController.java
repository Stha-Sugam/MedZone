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
 */
@WebServlet("/ManageTicket")
public class ManageTicketController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final TicketService ticketService = new TicketService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageTicketController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SessionUtil.setAttribute(request, "TicketDetails", ticketService.getTicketsDetails());
		request.getRequestDispatcher("WEB-INF/pages/TicketManagement.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			int id = Integer.parseInt(request.getParameter("ticketId"));
			String status = request.getParameter("status");
			
			if(ticketService.updateTicketStatus(id, status)) {
				request.setAttribute("successMessage", "Ticket updated successfully.");
				response.sendRedirect("ManageTicket");
			}
			else {
				request.setAttribute("errorMessage", "Ticket update failed. Try later.");
				response.sendRedirect("ManageTicket");
			}
			response.sendRedirect("ManageTicket");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}

package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.UserManagementService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class UserManagementController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/ManageUser" })
public class UserManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserManagementService manageUserService = new UserManagementService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserManagementController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SessionUtil.setAttribute(request, "usersDetails", manageUserService.GetUserDetails());
		request.getRequestDispatcher("WEB-INF/pages/UserManagement.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

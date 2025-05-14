package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.DashboardService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class AdminDashboardController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Admin" })
public class AdminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final DashboardService dashboardService = new DashboardService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminDashboardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SessionUtil.setAttribute(request, "userCount",dashboardService.GetUserCount());
		SessionUtil.setAttribute(request, "medCount", dashboardService.GetMedicineCount());
		SessionUtil.setAttribute(request, "recentMeds", dashboardService.GetRecentMeds());
		SessionUtil.setAttribute(request,"recentUsers", dashboardService.GetRecentUsers());
		request.getRequestDispatcher("WEB-INF/pages/AdminDashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

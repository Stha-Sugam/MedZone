package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.MedicineManagementService;
import com.medzone.service.UserManagementService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class ManageMedController
 */
@WebServlet("/ManageMed")
public class ManageMedController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final MedicineManagementService manageMedicineService = new MedicineManagementService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageMedController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SessionUtil.setAttribute(request, "medicineDetails", manageMedicineService.GetMedDetails());
		request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

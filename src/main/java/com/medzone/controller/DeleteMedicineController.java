package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.MedicineManagementService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class DeleteMedicineController
 */
@WebServlet("/DeleteMed")
public class DeleteMedicineController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final MedicineManagementService medicineService = new MedicineManagementService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteMedicineController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        
		request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String medicineId = request.getParameter("medsId");

        if(medicineService.deleteMedicine(medicineId)){
            SessionUtil.setAttribute(request, "successMessage", "Medicine information has been successfully deleted.");
        }
        else {
            request.setAttribute("errorMessage", "Failed to deleted medicine information.");
        }
        
        response.sendRedirect(request.getContextPath() + "/ManageMed");
	}

}

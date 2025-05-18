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
 * Handles the deletion of medicine records from the system.
 */
@WebServlet("/DeleteMed")
public class DeleteMedicineController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service responsible for medicine-related business logic and database interaction
    private final MedicineManagementService medicineService = new MedicineManagementService();

    /**
     * Default constructor
     */
    public DeleteMedicineController() {
        super();
    }

    /**
     * Handles GET requests - simply forwards to the medicine management page.
     * Typically not used for deletion, but present for completeness or debugging.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - processes deletion of a medicine entry based on provided ID.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        // Retrieve the medicine ID from the form parameter
        String medicineId = request.getParameter("medsId");

        // Attempt to delete the medicine record using the service
        if (medicineService.deleteMedicine(medicineId)) {
            // On successful deletion, store success message in session
            SessionUtil.setAttribute(request, "successMessage", "Medicine information has been successfully deleted.");
        } else {
            // On failure, store error message in request scope (will be discarded on redirect)
            request.setAttribute("errorMessage", "Failed to delete medicine information.");
        }

        // Redirect back to the medicine management page (triggers reload with updated list)
        response.sendRedirect(request.getContextPath() + "/ManageMed");
    }
}

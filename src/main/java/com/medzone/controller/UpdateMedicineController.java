package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.medzone.model.MedicineModel;
import com.medzone.service.MedicineManagementService;

import java.io.IOException;

@WebServlet("/UpdateMed")
public class UpdateMedicineController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MedicineManagementService medicineService = new MedicineManagementService();

    // Handle GET request to load form with existing data
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String medicineId = request.getParameter("medsId");

        if (medicineId != null && !medicineId.trim().isEmpty()) {
            MedicineModel medicine = medicineService.extractMedicine(medicineId);
            System.out.println(medicine);

            if (medicine != null) {
                request.setAttribute("medToUpdate", medicine);
                // The form will be shown because medToUpdate is not empty
            } else {
                request.setAttribute("errorName", "Medicine not found.");
            }
        } else {
            request.setAttribute("errorName", "Invalid medicine ID.");
        }

        request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
    }

    // Handle POST request for updating medicine
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String form = request.getParameter("form");
        String strength = request.getParameter("strength");
        String usage = request.getParameter("usage");

        boolean hasError = false;

        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorName", "Name is required.");
            hasError = true;
        }
        if (brand == null || brand.trim().isEmpty()) {
            request.setAttribute("errorBrand", "Brand is required.");
            hasError = true;
        }
        if (form == null || form.trim().isEmpty()) {
            request.setAttribute("errorForm", "Dosage form is required.");
            hasError = true;
        }
        if (strength == null || strength.trim().isEmpty()) {
            request.setAttribute("errorStrength", "Strength is required.");
            hasError = true;
        }
        if (usage == null || usage.trim().isEmpty()) {
            request.setAttribute("errorUsage", "Usage is required.");
            hasError = true;
        }

        // Create medicine model from form input
        MedicineModel medicine = new MedicineModel(id, name, brand, form, strength, usage, null);

        if (hasError) {
            // Show form again with existing input and error messages
            request.setAttribute("medToUpdate", medicine);
            request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
        } else {
            // Attempt update in service
            boolean updated = medicineService.updateMedicine(medicine);
            if (updated) {
                response.sendRedirect("ManageMed");
            } else {
                request.setAttribute("errorName", "Failed to update medicine. Try again.");
                request.setAttribute("medToUpdate", medicine);
                request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
            }
        }
    }
}
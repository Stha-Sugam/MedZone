package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.medzone.model.MedicineModel;
import com.medzone.service.MedicineManagementService;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

import java.io.IOException;

@WebServlet("/UpdateMed")
public class UpdateMedicineController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MedicineManagementService medicineService = new MedicineManagementService();

    // Handle GET request to load form with existing data
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	SessionUtil.removeAttribute(request, "medToUpdate");
        SessionUtil.removeAttribute(request, "medIdtoUpdate");
        
        String medicineId = request.getParameter("medsId");
        SessionUtil.setAttribute(request, "medIdtoUpdate", medicineId);

        MedicineModel medicine = medicineService.extractMedicine(medicineId);
        SessionUtil.setAttribute(request, "medToUpdate", medicine);
        request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
    }

    // Handle POST request for updating medicine
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	String id = (String) SessionUtil.getAttribute(request, "medIdtoUpdate");
        	MedicineModel orgMed = medicineService.extractMedicine(id);
        	
            String name = request.getParameter("name").trim();
            String brand = request.getParameter("brand").trim();
            String form = request.getParameter("form").trim();
            String strength = request.getParameter("strength").trim();
            String usage = request.getParameter("usage").trim();

            String nameError = validateName(request, name);
            String brandError = validateBrand(request, brand);
            String formError = validateForm(request, form);
            String strengthError = validateStrength(request, strength);
            String usageError = validateUsage(request, usage);

            MedicineModel medicine = new MedicineModel(id, name, brand, form, strength, usage);

            if(nameError != null || brandError != null || formError != null || strengthError != null || usageError != null) {
                request.setAttribute("medToUpdate", medicine);
                handleInputError(request, response, nameError, brandError, formError, strengthError, usageError);
                return;
            }

            if(orgMed != null && name.equals(orgMed.getName()) && brand.equals(orgMed.getBrand()) && form.equals(orgMed.getForm()) && strength.equals(orgMed.getStrength())
                    && usage.equals(orgMed.getUsage())) {
                request.setAttribute("medToUpdate", medicine);
                handleError(request, response, "No changes detected. Please modify some fields before updating.");
                return;
            }

            if(medicineService.updateMedicine(medicine)) {
                SessionUtil.setAttribute(request, "successMessage", "Medicine information has been successfully updated.");
                response.sendRedirect("ManageMed");
            }
            else {
                request.setAttribute("medToUpdate", medicine);
                handleError(request, response, "Failed to update. Try again.");
            }
        } catch(Exception e){
            request.setAttribute("medToUpdate", new MedicineModel(request.getParameter("medsId"), request.getParameter("name"), request.getParameter("brand"), 
            		request.getParameter("form"), request.getParameter("strength"), request.getParameter("usage")));
            e.printStackTrace();
            handleError(request, response, "Cannot Connect to Server. Please try again!");
        }
    }

    private String validateName(HttpServletRequest req, String name) {
        if(ValidationUtil.isNullOrEmpty(name)) {
            return "Required.";
        }
        return null;
    }

    private String validateBrand(HttpServletRequest req, String brand) {
        if(ValidationUtil.isNullOrEmpty(brand)) {
            return "Required.";
        }
        return null;
    }

    private String validateForm(HttpServletRequest req, String form) {
        if(ValidationUtil.isNullOrEmpty(form)) {
            return "Required.";
        }
        return null;
    }

    private String validateStrength(HttpServletRequest req, String strength) {
        if(ValidationUtil.isNullOrEmpty(strength)) {
            return "Required.";
        }
        return null;
    }

    private String validateUsage(HttpServletRequest req, String usage) {
        if(ValidationUtil.isNullOrEmpty(usage)) {
            return "Required.";
        }
        return null;
    }

    private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String nameError, String brandError,
            String formError, String strengthError, String usageError) throws ServletException, IOException {
        req.setAttribute("errorName", nameError);
        req.setAttribute("errorBrand", brandError);
        req.setAttribute("errorForm", formError);
        req.setAttribute("errorStrength", strengthError);
        req.setAttribute("errorUsage", usageError);
        req.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException{
        req.setAttribute("errorMessage", message);
        req.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(req, resp);
    }
}
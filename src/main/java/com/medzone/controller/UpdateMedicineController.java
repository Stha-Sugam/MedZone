package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.medzone.model.MedicineModel;
import com.medzone.service.MedicineManagementService;
import com.medzone.util.ImageUtil;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

import java.io.IOException;
//Specify the servlet endpoint and configure file upload settings
@WebServlet("/UpdateMed")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UpdateMedicineController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Utility for handling image upload
	private final ImageUtil imageUtil = new ImageUtil();

	// Service layer object to manage medicine operations
	private final MedicineManagementService medicineService = new MedicineManagementService();

	// Handle GET request: Load the medicine data into session and forward to the JSP page
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Clear any previously stored update session attributes
		SessionUtil.removeAttribute(request, "medToUpdate");
		SessionUtil.removeAttribute(request, "medIdtoUpdate");

		// Get medicine ID from request and store it in session
		String medicineId = request.getParameter("medsId");
		SessionUtil.setAttribute(request, "medIdtoUpdate", medicineId);

		// Retrieve medicine data by ID and store in session for pre-filling the form
		MedicineModel medicine = medicineService.extractMedicine(medicineId);
		SessionUtil.setAttribute(request, "medToUpdate", medicine);

		// Forward to the medicine management page
		request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
	}

	// Handle POST request: Validate inputs and perform medicine update
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Retrieve original medicine ID and object from session
			String id = (String) SessionUtil.getAttribute(request, "medIdtoUpdate");
			MedicineModel orgMed = medicineService.extractMedicine(id);

			// Original image path
			String imageUrl = orgMed.getImageUrl();

			// Get uploaded image from form
			Part medImage = request.getPart("image");

			// Get updated input fields
			String name = request.getParameter("name").trim();
			String brand = request.getParameter("brand").trim();
			String form = request.getParameter("form").trim();
			String strength = request.getParameter("strength").trim();
			String usage = request.getParameter("usage").trim();

			// Validate each input
			String nameError = validateName(request, name);
			String brandError = validateBrand(request, brand);
			String formError = validateForm(request, form);
			String strengthError = validateStrength(request, strength);
			String usageError = validateUsage(request, usage);
			String imageError = null;

			// Flag to check if a new image is uploaded
			boolean newImageUploaded = false;

			// If image is uploaded, validate and upload it
			if (medImage != null && medImage.getSize() > 0) {
				imageError = validateImage(medImage);
				if (imageError == null) {
					imageUrl = imageUtil.getImageNameFromPart(medImage);
					if (!uploadImage(request)) {
						handleError(request, response, "Could not upload the new image. Please try again later!");
						return;
					}
					newImageUploaded = true;
				}
			}

			// Construct updated medicine object
			MedicineModel medicine = new MedicineModel(id, name, brand, form, strength, usage, imageUrl);

			// If any input validation failed, send back errors and retain user input
			if (nameError != null || brandError != null || formError != null || strengthError != null
					|| usageError != null || imageError != null) {
				request.setAttribute("medToUpdate", medicine);
				handleInputError(request, response, nameError, brandError, formError, strengthError, usageError,
						imageError);
				return;
			}

			// If no changes are made, show appropriate message
			if (!newImageUploaded && orgMed != null && name.equals(orgMed.getName()) && brand.equals(orgMed.getBrand())
					&& form.equals(orgMed.getForm()) && strength.equals(orgMed.getStrength())
					&& usage.equals(orgMed.getUsage())) {
				request.setAttribute("medToUpdate", medicine);
				handleError(request, response, "No changes detected. Please modify some fields before updating.");
				return;
			}

			// If update is successful, try re-uploading image again and redirect
			if (medicineService.updateMedicine(medicine)) {
				try {
					if (medImage != null && medImage.getSize() > 0 && !uploadImage(request)) {
						handleError(request, response, "Could not upload the image. Please try again later!");
					}
					SessionUtil.setAttribute(request, "successMessage", "Medicine information has been successfully updated.");
					response.sendRedirect("ManageMed");
				} catch (Exception e) {
					handleError(request, response, "An error occurred during update.");
					e.printStackTrace();
				}
			} else {
				// If update failed, retain the form and show error
				request.setAttribute("medToUpdate", medicine);
				handleError(request, response, "Failed to update. Try again.");
			}
		} catch (Exception e) {
			// In case of unexpected error, fill medicine object and show error
			request.setAttribute("medToUpdate",
					new MedicineModel(request.getParameter("medsId"), request.getParameter("name"),
							request.getParameter("brand"), request.getParameter("form"),
							request.getParameter("strength"), request.getParameter("usage")));
			e.printStackTrace();
			handleError(request, response, "Cannot Connect to Server. Please try again!");
		}
	}

	// Validate medicine name
	private String validateName(HttpServletRequest req, String name) {
		if (ValidationUtil.isNullOrEmpty(name)) {
			return "Required.";
		}
		return null;
	}

	// Validate brand name
	private String validateBrand(HttpServletRequest req, String brand) {
		if (ValidationUtil.isNullOrEmpty(brand)) {
			return "Required.";
		}
		return null;
	}

	// Validate medicine form (e.g., tablet, syrup)
	private String validateForm(HttpServletRequest req, String form) {
		if (ValidationUtil.isNullOrEmpty(form)) {
			return "Required.";
		}
		return null;
	}

	// Validate strength (e.g., 500mg)
	private String validateStrength(HttpServletRequest req, String strength) {
		if (ValidationUtil.isNullOrEmpty(strength)) {
			return "Required.";
		}
		return null;
	}

	// Validate usage field (e.g., Pain Relief)
	private String validateUsage(HttpServletRequest req, String usage) {
		if (ValidationUtil.isNullOrEmpty(usage)) {
			return "Required.";
		}
		return null;
	}

	// Validate uploaded image extension
	private String validateImage(Part imagePart) {
		if (imagePart != null && imagePart.getSize() > 0 && !ValidationUtil.isValidImageExtension(imagePart)) {
			return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
		}
		return null;
	}

	// Upload image to the server using utility class
	private boolean uploadImage(HttpServletRequest req) throws IOException, ServletException {
		Part image = req.getPart("image");
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "medicine");
	}

	// Send form input error messages to JSP
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String nameError, String brandError,
			String formError, String strengthError, String usageError, String imageError)
			throws ServletException, IOException {
		req.setAttribute("errorName", nameError);
		req.setAttribute("errorBrand", brandError);
		req.setAttribute("errorForm", formError);
		req.setAttribute("errorStrength", strengthError);
		req.setAttribute("errorUsage", usageError);
		req.setAttribute("errorImage", imageError);
		req.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(req, resp);
	}

	// Send general error message to JSP
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("errorMessage", message);
		req.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(req, resp);
	}
}
package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;

import com.medzone.model.MedicineModel;
import com.medzone.service.MedicineManagementService;
import com.medzone.util.ImageUtil;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class UpdateProfileController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/AddMed" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AddMedicineController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ImageUtil imageUtil = new ImageUtil();
	// Creating an object of RegisterService
	private final MedicineManagementService manageMedService = new MedicineManagementService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddMedicineController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			// extracting all the required fields from the inputs of jsp.
			String id = req.getParameter("id");
			String name = req.getParameter("name");
			String brand = req.getParameter("brand");
			String form = req.getParameter("form");
			String strength = req.getParameter("strength");
			String usage = req.getParameter("usage");
			// setting the current time as addedDate
			LocalDateTime addedDate = LocalDateTime.now();

			// validating all the input values of the fields entered by the user
			String idError = validateId(req, id);
			String nameError = validateName(req, name);
			String brandError = validateBrand(req, brand);
			String formError = validateForm(req, form);
			String strengthError = validateStrength(req, strength);
			String usageError = validateUsage(req, usage);
			String imageError = validateImage(req);

			// if any error occurs, proper message is shown using handleInputError() method
			if (idError != null || nameError != null || brandError != null || formError != null || strengthError != null
					|| usageError != null || imageError != null) {
				handleInputError(req, resp, idError, nameError, brandError, formError, strengthError, usageError,
						imageError);
				return;
			}

			// Checks for already registered id and displays proper message if already registered.
			Boolean registeredIdError = manageMedService.checkRegisteredId(id);

			if (registeredIdError != null && registeredIdError) {
				handleError(req, resp, "Medicine with this id is already registered.");
				req.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(req, resp);
				return;
			}

			// gets the image from the user input and extracts the name form that path.
			Part medImage = req.getPart("image");
			String imageUrl = imageUtil.getImageNameFromPart(medImage);

			// new medicine model is created for insertion
			MedicineModel medicine = new MedicineModel(id, name, brand, form, strength, usage, addedDate, imageUrl);

			// Adding the medicine to the database
			if (manageMedService.addMedicine(medicine)) {
				try {
					// if image is uploaded to the folder, then proper success message is shown. else
					if (uploadImage(req)) {
						SessionUtil.setAttribute(req, "successMessage", "Medicine inforamtion has been successfuly added.");
						resp.sendRedirect("ManageMed");
					} else {
						handleError(req, resp, "Could not upload the image. Please try again later!");
					}
				// if any exception occurs, throw an exception and show proper message
				} catch (IOException | ServletException e) {
					handleError(req, resp, "An error occurred while uploading the image. Please try again later!");
					e.printStackTrace();
				}
			} else {
				handleError(req, resp, "Could not add medicine right now.");
			}
		// if any exception occurs, throws error and displays proper error message
		} catch (Exception e) {
			e.printStackTrace();
			handleError(req, resp, "Cannot Connect to Server. Please try again!");
		}
	}

	// method to validate the id of the medicine
	private String validateId(HttpServletRequest req, String id) {
		// Validation for id
		if (ValidationUtil.isNullOrEmpty(id)) {
			return "Required.";
		}
		return null;
	}

	// method to validate the name of the medicine
	private String validateName(HttpServletRequest req, String name) {
		// Validation for name
		if (ValidationUtil.isNullOrEmpty(name)) {
			// returns required if field is empty
			return "Required.";
		}
		return null;
	}

	// method to validate the brand of the medicine
	private String validateBrand(HttpServletRequest req, String brand) {
		// Validation for brand
		if (ValidationUtil.isNullOrEmpty(brand)) {
			// returns required if field empty
			return "Required.";
		}
		return null;
	}

	// method to validate the dosage form of the medicine
	private String validateForm(HttpServletRequest req, String form) {
		// Validation for form
		if (ValidationUtil.isNullOrEmpty(form)) {
			// returns required if the field is empty
			return "Required.";
		}
		return null;
	}

	// method to validate the strength of the medicine
	private String validateStrength(HttpServletRequest req, String strength) {
		// Validation for strength
		if (ValidationUtil.isNullOrEmpty(strength)) {
			// returns required if the field is empty
			return "Required.";
		}
		return null;
	}

	// method to validate usage of the medicine
	private String validateUsage(HttpServletRequest req, String usage) {
		// Validation for usage
		if (ValidationUtil.isNullOrEmpty(usage)) {
			// returns required if the field is empty
			return "Required.";
		}
		return null;
	}

	// method to validate the image
	private String validateImage(HttpServletRequest req) {
		try {
			Part medImage = req.getPart("image");
			if (medImage == null || medImage.getSize() == 0) {
				return "Required";
			} else if (!ValidationUtil.isValidImageExtension(medImage))
				return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
		} catch (IOException | ServletException e) {
			return "Error handling image file. Please ensure the file is valid.";
		}
		return null;
	}

	// method to upload and check if the image is uploaded to the folder.
	private boolean uploadImage(HttpServletRequest req) throws IOException, ServletException {
		// gets the image from the user input
		Part image = req.getPart("image");
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "medicine");
	}

	/**
	 * 
	 * @param req the request sent by the amdin
	 * @param resp the response sent by the system to the admin
	 * @param idError Error message occured in the field id
	 * @param nameError Error message occured in the field name
	 * @param brandError Error message occured in the field brand
	 * @param formError Error message occured in the field form
	 * @param strengthError Error message occured in the field strength
	 * @param usageError Error message occured in the field usage
	 * @param imageError Error message occured in the field image
	 * @throws ServletException exception thrown after database error
	 * @throws IOException 
	 */
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String idError, String nameError,
			String brandError, String formError, String strengthError, String usageError, String imageError)
			throws ServletException, IOException {
		// sets the fields with previous user inputs
		req.setAttribute("id", req.getParameter("id"));
		req.setAttribute("name", req.getParameter("name"));
		req.setAttribute("brand", req.getParameter("brand"));
		req.setAttribute("form", req.getParameter("form"));
		req.setAttribute("strength", req.getParameter("strength"));
		req.setAttribute("usage", req.getParameter("usage"));
		req.setAttribute("image", req.getParameter("image"));

		// sets error message to its corresponding error fields.
		req.setAttribute("errorId", idError);
		req.setAttribute("errorName", nameError);
		req.setAttribute("errorBrand", brandError);
		req.setAttribute("errorForm", formError);
		req.setAttribute("errorStrength", strengthError);
		req.setAttribute("errorUsage", usageError);
		req.setAttribute("errorImage", imageError);
		req.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(req, resp);
	}

	/**
	 * Handles errors by setting an error message as a request attribute
	 * and forwarding the request and response to the AddMedicine.jsp page.
	 * 
	 * @param req     the HttpServletRequest object
	 * @param resp    the HttpServletResponse object
	 * @param message the error message to display on the page
	 * @throws ServletException if forwarding fails due to a servlet error
	 * @throws IOException      if forwarding fails due to an I/O error
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
	        throws ServletException, IOException {
	    req.setAttribute("errorMessage", message);
	    req.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(req, resp);
	}

}

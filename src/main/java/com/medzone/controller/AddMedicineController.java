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
			String id = req.getParameter("id");
			String name = req.getParameter("name");
			String brand = req.getParameter("brand");
			String form = req.getParameter("form");
			String strength = req.getParameter("strength");
			String usage = req.getParameter("usage");
			LocalDateTime addedDate = LocalDateTime.now();

			String idError = validateId(req, id);
			String nameError = validateName(req, name);
			String brandError = validateBrand(req, brand);
			String formError = validateForm(req, form);
			String strengthError = validateStrength(req, strength);
			String usageError = validateUsage(req, usage);
			String imageError = validateImage(req);

			if (idError != null || nameError != null || brandError != null || formError != null || strengthError != null
					|| usageError != null || imageError != null) {
				handleInputError(req, resp, idError, nameError, brandError, formError, strengthError, usageError,
						imageError);
				return;
			}

			Boolean registeredIdError = manageMedService.checkRegisteredId(id);

			if (registeredIdError != null && registeredIdError) {
				req.setAttribute("errorId", "Medicine with this id is already registered.");
				req.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(req, resp);
				return;
			}

			Part medImage = req.getPart("image");
			String imageUrl = imageUtil.getImageNameFromPart(medImage);

			MedicineModel medicine = new MedicineModel(id, name, brand, form, strength, usage, addedDate, imageUrl);

			// Adding the medicine to the database
			if (manageMedService.addMedicine(medicine)) {
				try {
					if (uploadImage(req)) {
						SessionUtil.setAttribute(req, "successMessage",
								"Medicine inforamtion has been successfuly added.");
						resp.sendRedirect("ManageMed");
					} else {
						handleError(req, resp, "Could not upload the image. Please try again later!");
					}
				} catch (IOException | ServletException e) {
					handleError(req, resp, "An error occurred while uploading the image. Please try again later!");
					e.printStackTrace();
				}
			} else {
				handleError(req, resp, "Could not add medicine right now.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleError(req, resp, "Cannot Connect to Server. Please try again!");
		}
	}

	private String validateId(HttpServletRequest req, String id) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(id)) {
			return "Required.";
		}
		return null;
	}

	private String validateName(HttpServletRequest req, String name) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(name)) {
			return "Required.";
		}
		return null;
	}

	private String validateBrand(HttpServletRequest req, String brand) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(brand)) {
			return "Required.";
		}
		return null;
	}

	private String validateForm(HttpServletRequest req, String form) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(form)) {
			return "Required.";
		}
		return null;
	}

	private String validateStrength(HttpServletRequest req, String strength) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(strength)) {
			return "Required.";
		}
		return null;
	}

	private String validateUsage(HttpServletRequest req, String usage) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(usage)) {
			return "Required.";
		}
		return null;
	}

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

	private boolean uploadImage(HttpServletRequest req) throws IOException, ServletException {
		Part image = req.getPart("image");
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "medicine");
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @param firstNameError
	 * @param lastNameError
	 * @param userNameError
	 * @param phoneNumError
	 * @param passwordError
	 * @param cpasswordError
	 * @param emailError
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String idError, String nameError,
			String brandError, String formError, String strengthError, String usageError, String imageError)
			throws ServletException, IOException {
		req.setAttribute("id", req.getParameter("id"));
		req.setAttribute("name", req.getParameter("name"));
		req.setAttribute("brand", req.getParameter("brand"));
		req.setAttribute("form", req.getParameter("form"));
		req.setAttribute("strength", req.getParameter("strength"));
		req.setAttribute("usage", req.getParameter("usage"));
		req.setAttribute("image", req.getParameter("image"));

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
	 * 
	 * @param req
	 * @param resp
	 * @param message
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("errorMessage", message);
		req.getRequestDispatcher("WEB-INF/pages/AddMedicine.jsp").forward(req, resp);
	}
}

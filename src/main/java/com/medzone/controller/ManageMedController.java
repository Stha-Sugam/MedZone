package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.service.MedicineManagementService;
import com.medzone.service.SearchService;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class ManageMedController
 * Handles the display and search operations for the medicine management interface.
 */
@WebServlet("/ManageMed")
public class ManageMedController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Services used for managing medicines and performing search
	private final MedicineManagementService manageMedicineService = new MedicineManagementService();
	private final SearchService searchService = new SearchService();

	/**
	 * Default constructor.
	 */
	public ManageMedController() {
		super();
	}

	/**
	 * Handles GET request to display the medicine management page.
	 * Clears any previous session attributes related to update operations and
	 * loads all medicine details to be displayed.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// Remove any session attributes related to medicine update process
		SessionUtil.removeAttribute(request, "medToUpdate");
		SessionUtil.removeAttribute(request, "medIdtoUpdate");

		// Set all medicine details for listing on the management page
		SessionUtil.setAttribute(request, "medicineDetails", manageMedicineService.getMedDetails());

		// Forward to JSP for rendering the medicine management page
		request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);

		// Clear previously searched details if any
		request.removeAttribute("searchedDetails");
	}

	/**
	 * Handles POST request for performing medicine search.
	 * Validates the search input and displays matched results or error message.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// Get search input from user
		String searchValue = request.getParameter("search");

		// Validate search input
		String searchError = validateSearch(request, searchValue);

		try {
			if (searchError != null) {
				// If validation fails, forward back with error message
				request.setAttribute("searchErrors", searchError);
				request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
				return;
			}

			// Fetch matching medicine details
			if (!searchService.extractMedSearchedDetails(searchValue).isEmpty()) {
				// If results found, show them
				request.setAttribute("searchedDetails", searchService.extractMedSearchedDetails(searchValue));
			} else {
				// If no result found, show error and input value
				request.setAttribute("searchedVal", searchValue);
				request.setAttribute("errorMessage", "medicine id, name or brand not found.");
				request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
				return;
			}

			// Show the searched value to keep user input visible
			request.setAttribute("searchedVal", searchValue);

			// Forward to JSP with search results
			request.getRequestDispatcher("WEB-INF/pages/MedicineManagement.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace(); // Ideally should be logged to a file
		}
	}

	/**
	 * Validates the search input field.
	 * @param req The HTTP request object
	 * @param value The search input value
	 * @return Error message if invalid; otherwise, null
	 */
	private String validateSearch(HttpServletRequest req, String value) {
		if (ValidationUtil.isNullOrEmpty(value)) {
			return "Input the medicine's id, name or brand.";
		}
		return null;
	}
}

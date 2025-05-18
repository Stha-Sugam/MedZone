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
 * Servlet implementation class BrowseController
 * Handles the browsing and searching of medicine information for public users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Browse" })
public class BrowseController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service to manage medicine data
	private final MedicineManagementService manageMedicineService = new MedicineManagementService();

	// Service to handle medicine search logic
	private final SearchService searchService = new SearchService();

	/**
	 * Default constructor
	 */
	public BrowseController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Handles GET requests to load and display all available medicines.
	 * Sets all medicine details in session and forwards to Browse.jsp.
	 * 
	 * @param request HTTP request
	 * @param response HTTP response
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Load all medicine details and set them as a session attribute
		SessionUtil.setAttribute(request, "medicineDetails", manageMedicineService.getMedDetails());

		// Forward request to Browse.jsp for display
		request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);

		// Remove previously searched details to avoid overlapping on refresh
		request.removeAttribute("searchedDetails");
	}

	/**
	 * Handles POST requests for either search or viewing individual medicine details.
	 * Distinguishes actions based on presence of search input.
	 * 
	 * @param request HTTP request
	 * @param response HTTP response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Extracts medicine ID (if user clicks on specific medicine) or search input
		String medId = request.getParameter("medId");
		String searchValue = request.getParameter("search");
		String username = (String) SessionUtil.getAttribute(request, "username");

		// If the user performed a search
		if (searchValue != null) {
			// Validate the search input
			String searchError = validateSearch(request, searchValue);

			// If there's a validation error, forward with error message
			if (searchError != null) {
				request.setAttribute("searchErrors", searchError);
				request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);
				return;
			}

			// If search results found, set them to display
			if (!searchService.extractMedSearchedDetails(searchValue).isEmpty()) {
				request.setAttribute("searchedDetails", searchService.extractMedSearchedDetails(searchValue));
			} else {
				// If no results found, show appropriate message
				request.setAttribute("errorMessage", "medicine id, name or brand not found.");
			}

			// Keep search value in input field after submission
			request.setAttribute("searchedVal", searchValue);
		}
		// If no search input, assume user clicked on a medicine to view details
		else {
			// Extract and set medicine details
			request.setAttribute("med", manageMedicineService.extractMedicine(medId));

			// Record that this user viewed this medicine
			manageMedicineService.addView(username, medId);
		}

		// Forward back to Browse.jsp with relevant data
		request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);
	}

	/**
	 * Validates the search input field.
	 * 
	 * @param req HTTP request
	 * @param value The search string entered by user
	 * @return Returns error message if invalid, null otherwise
	 */
	private String validateSearch(HttpServletRequest req, String value) {
		// Check if input is empty or null
		if (ValidationUtil.isNullOrEmpty(value)) {
			return "Input the medicine's id, name or brand.";
		} else {
			return null;
		}
	}
}

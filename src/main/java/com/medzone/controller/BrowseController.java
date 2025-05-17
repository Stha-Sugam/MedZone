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
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Browse" })
public class BrowseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final MedicineManagementService manageMedicineService = new MedicineManagementService();
	private final SearchService searchService = new SearchService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SessionUtil.setAttribute(request, "medicineDetails", manageMedicineService.getMedDetails());
		
		request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);
		
		request.removeAttribute("searchedDetails");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String medId = (String) request.getParameter("medId");
		String searchValue = request.getParameter("search");
		String username = (String) SessionUtil.getAttribute(request, "username");
		
		if(searchValue != null) {
			String searchError = validateSearch(request, searchValue);
			
			if(searchError != null) {
				request.setAttribute("searchErrors", searchError);
				request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);
				return;
			}
						
			if(!searchService.extractMedSearchedDetails(searchValue).isEmpty()) {
				request.setAttribute("searchedDetails", searchService.extractMedSearchedDetails(searchValue));
			}
			else {
				
				request.setAttribute("errorMessage", "medicine id, name or brand not found.");
			}
			
			request.setAttribute("searchedVal", searchValue);
		}
		else {
			request.setAttribute("med", manageMedicineService.extractMedicine(medId));
			manageMedicineService.addView(username, medId);
		}
		request.getRequestDispatcher("WEB-INF/pages/Browse.jsp").forward(request, response);
	}
	
	private String validateSearch(HttpServletRequest req, String value) {
		if (ValidationUtil.isNullOrEmpty(value)) {
			return "Input the medicine's id, name or brand.";
		} 
		else {
			return null;
		}
	}

}

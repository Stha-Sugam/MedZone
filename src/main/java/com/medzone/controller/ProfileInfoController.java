package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.model.UserModel;
import com.medzone.service.ProfileService;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class ProfileInfoController
 * This servlet handles the retrieval and display of a user's profile information.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/ProfileInfo" })
public class ProfileInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service class to handle profile-related database operations
	private final ProfileService profileService = new ProfileService();
       
    /**
     * Default constructor.
     */
    public ProfileInfoController() {
        super();
    }

	/**
	 * Handles GET requests to load and display profile information of the logged-in user.
	 * If a database error occurs, an error message is stored in the session.
	 * Otherwise, the user's information is retrieved and stored in the session.
	 * Finally, the request is forwarded to the ProfileInfo.jsp page with "info-section" as active.
	 *
	 * @param request  the HttpServletRequest object that contains the request the client made
	 * @param response the HttpServletResponse object that contains the response the servlet returns
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// If there's a database issue, inform the user via session attribute
		if (profileService.DatabaseError()) {
			SessionUtil.setAttribute(request, "errorMessage", "Database Error. Cannot Retrieve Information.");
		} else {
			// Get the username from session and fetch user details
			String username = (String) SessionUtil.getAttribute(request, "username");
			UserModel userInfo = profileService.getUserInfo(new UserModel(username));

			// Store user info in session for use in the JSP
			SessionUtil.setAttribute(request, "user", userInfo);
		}

		// Set the active section so the JSP shows the profile info section
		request.setAttribute("activeSection", "info-section");

		// Forward the request to the profile info JSP page
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}
}

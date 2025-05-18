package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.model.UserModel;
import com.medzone.service.ProfileService;
import com.medzone.util.PasswordUtil;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class UpdatePasswordController
 * Handles the password update functionality for logged-in users.
 */
@WebServlet("/UpdatePassword")
public class UpdatePasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service for profile-related operations such as password validation and update
	private final ProfileService profileService = new ProfileService();

    /**
     * Default constructor.
     */
    public UpdatePasswordController() {
        super();
    }

	/**
	 * Handles the HTTP GET request.
	 * Loads the profile page with the password update section active.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("activeSection", "update-password");
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}

	/**
	 * Handles the HTTP POST request.
	 * Processes the password update form submission.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get current logged-in username from session
		String username = (String) SessionUtil.getAttribute(request, "username");
		// Get form input parameters
		String oldPass = request.getParameter("oldPassword");
		String newPass = request.getParameter("newPassword");
		String confirmPass = request.getParameter("cpassword");

		// Validate the form inputs and collect error messages if any
		String oldPassError = validateOldPassword(request, oldPass);
		String newPassError = validatePassword(request, newPass);
		String cpassError = validateCPassword(request, newPass, confirmPass);

		// If there are validation errors, show them on the form and return
		if(oldPassError != null || newPassError != null || cpassError != null) {
			handleInputError(request, response, oldPassError, newPassError, cpassError);
			return;
		}

		try {
			// Create a UserModel with username and old password to verify correctness
			UserModel user = new UserModel(username, oldPass);
			Boolean correct = profileService.checkPassword(user);

			// If old password is incorrect, show error and return
			if(!correct) {
				request.setAttribute("oldPassword", "Incorrect Password.");
				request.setAttribute("activeSection", "update-password");
				request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
				return;
			}

			// Encrypt the new password before updating
			String newEncryptedPass = PasswordUtil.encrypt(username, newPass);
			UserModel changePassUser = new UserModel(username, newEncryptedPass);

			// Attempt to update the password in the database
			if(profileService.updatePassword(changePassUser)) {
				// On success, set a success message in session and redirect to profile info page (info section active)
				SessionUtil.setAttribute(request, "successMessage", "Your password has been successfully changed.");
				request.setAttribute("activeSection", "info-section");
				response.sendRedirect(request.getContextPath() + "/ProfileInfo");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			// Handle unexpected errors such as server connection issues
			handleError(request, response, "Cannot Connect to Server. Please try again!");
		}
	}

	/**
	 * Validate old password input.
	 */
	private String validateOldPassword(HttpServletRequest req, String oldPassword) {
		if (ValidationUtil.isNullOrEmpty(oldPassword)) {
			return "Required";
		}
		return null;
	}

	/**
	 * Validate new password input.
	 * Checks for presence and complexity requirements.
	 */
	private String validatePassword(HttpServletRequest req, String password) {
		if(ValidationUtil.isNullOrEmpty(password)) {
			return "Required.";
		} else if(!ValidationUtil.validatePassword(password)) {
			return "length > 8, 1 uppercase, 1 number";
		}
		return null;
	}

	/**
	 * Validate confirm password input.
	 * Ensures it matches the new password.
	 */
	private String validateCPassword(HttpServletRequest req, String password, String cpassword) {
		if(ValidationUtil.isNullOrEmpty(cpassword)) {
			return "Required.";
		} else if(!ValidationUtil.matchPasswords(password, cpassword)) {
			return "Passwords do not match.";
		}
		return null;
	}

	/**
	 * Handles general errors by forwarding to profile page with an error message and update-password tab active.
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
		req.setAttribute("errorMessage", message);
		req.setAttribute("activeSection", "update-password");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}

	/**
	 * Handles form input validation errors by setting field-specific error messages
	 * and forwarding back to the profile page with update-password tab active.
	 */
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String oldPassError, String newPassError, String cPassError)
			throws ServletException, IOException {
		req.setAttribute("oldPassword", oldPassError);
		req.setAttribute("newPassword", newPassError);
		req.setAttribute("cpassword", cPassError);
		req.setAttribute("activeSection", "update-password");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
}

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
 */
@WebServlet("/UpdatePassword")
public class UpdatePasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ProfileService profileService = new ProfileService();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePasswordController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setAttribute("activeSection", "update-password");
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String username = (String) SessionUtil.getAttribute(request, "username");
		String oldPass = request.getParameter("oldPassword");
		String newPass = request.getParameter("newPassword");
		String confirmPass = request.getParameter("cpassword");

		String oldPassError = validateOldPassword(request, oldPass);
		String newPassError = validatePassword(request, newPass);
		String cpassError = validateCPassword(request, newPass, confirmPass);

		if(oldPassError != null || newPassError != null || cpassError != null) {
			handleInputError(request, response, oldPassError, newPassError, cpassError);
			return;
		}

		try {
			UserModel user = new UserModel(username, oldPass);
			Boolean correct = profileService.checkPassword(user);

			if(!correct) {
				request.setAttribute("oldPassword", "Incorrect Password.");
				request.setAttribute("activeSection", "update-password");
				request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
				return;
			}

			String newEncryptedPass = PasswordUtil.encrypt(username, newPass);
			UserModel changePassUser = new UserModel(username, newEncryptedPass);
			if(profileService.updatePassword(changePassUser)) {
				SessionUtil.setAttribute(request, "successMessage", "Your password has been successfully changed.");
				request.setAttribute("activeSection", "info-section");
				response.sendRedirect(request.getContextPath() + "/ProfileInfo");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			handleError(request, response, "Cannot Connect to Server. Please try again!");
		}
	}

	private String validateOldPassword(HttpServletRequest req, String oldPassword) {
		if (ValidationUtil.isNullOrEmpty(oldPassword)) {
			
			return "Required";
		}
		return null;
	}

	private String validatePassword(HttpServletRequest req, String password) {
		// Validation for password
		if(ValidationUtil.isNullOrEmpty(password)) {
			return "Required.";
		}
		else if(!ValidationUtil.validatePassword(password)) {
			return "length > 8, 1 uppercase, 1 number";
		}
		return null;
	}

	private String validateCPassword(HttpServletRequest req, String password, String cpassword) {
		// Validation for confirm password
		if(ValidationUtil.isNullOrEmpty(cpassword)) {
			return "Required.";
		}
		else if(!ValidationUtil.matchPasswords(password, cpassword)) {
			return "Passwords do not match.";
		}
		return null;
	}

	/**
	 *
	 * @param req
	 * @param resp
	 * @param message
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException{
		req.setAttribute("errorMessage", message);
		req.setAttribute("activeSection", "update-password");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}

	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String oldPassError, String newPassError, String cPassError)
			throws ServletException, IOException {
		req.setAttribute("oldPassword", oldPassError);
		req.setAttribute("newPassword", newPassError);
		req.setAttribute("cpassword", cPassError);
		req.setAttribute("activeSection", "update-password");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
}
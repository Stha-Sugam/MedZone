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
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class UpdateProfileController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/UpdateProfile" })
public class UpdateProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Creating an object of RegisterService
	private final ProfileService profileService = new ProfileService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProfileController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(profileService.DatabaseError()) {
			SessionUtil.setAttribute(request, "errorMessage", "Database Error. Cannot Retrieve Information.");
		}
		else {
			String username = (String) SessionUtil.getAttribute(request, "username");
			UserModel userInfo = profileService.getUserInfo(new UserModel(username));
			
			SessionUtil.setAttribute(request, "user", userInfo);
		}
		request.setAttribute("activeSection", "edit-profile");
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			UserModel orgUser = (UserModel) SessionUtil.getAttribute(req, "user");
			
			String newFirstName = req.getParameter("first-name");
			String newLastName = req.getParameter("last-name");
			String newPhoneNum = req.getParameter("phone-num");
			String newEmail = req.getParameter("email");
			
			String firstNameError = validateFirstName(req, newFirstName);
			String lastNameError = validateLastName(req, newLastName);
			String phoneNumError = validatePhoneNum(req, newPhoneNum);
			String emailError = validateEmail(req, newEmail);
			
			
			if (firstNameError != null || lastNameError != null || phoneNumError != null || emailError != null) {
				handleInputError(req, resp, firstNameError, lastNameError, phoneNumError, emailError);
				return;
			}
			
			if(newFirstName.equals(orgUser.getFirstName()) && newLastName.equals(orgUser.getLastName()) && newPhoneNum.equals(orgUser.getPhone()) && newEmail.equals(orgUser.getEmail())) {
				handleError(req, resp, "No changes detected. Please modify some fields before updating.");
				return;
			}
			
			String registeredPhone = null;
			String registeredEmail = null;
			
			if(!newPhoneNum.equals(orgUser.getPhone())) {
				registeredPhone = validateRegisteredPhoneNum(req, newPhoneNum);
							
			}
			if(!newEmail.equals(orgUser.getEmail())){
				registeredEmail = validateRegisteredEmail(req, newEmail);
			}
			
			if(registeredPhone != null || registeredEmail != null) {
				handleRegisteredError(req, resp, registeredPhone, registeredEmail);
				return;
			}	
			
			String username = (String) SessionUtil.getAttribute(req, "username");
			
			// Creating a new instance of UserModel
			UserModel updatedUser = new UserModel(username, newFirstName, newLastName, newPhoneNum, newEmail);
			
			// Adding the user to the database
			if (profileService.updateUser(updatedUser)) {
				SessionUtil.setAttribute(req, "successMessage", "Your profile info has been successfuly updated.");
				req.setAttribute("activeSection", "information");
				resp.sendRedirect("ProfileInfo");
			}
			else {
				handleError(req, resp, "Failed to update. Try again.");
			}		
		}
		catch(Exception e) {
			e.printStackTrace();
			handleError(req, resp, "Cannot Connect to Server. Please try again!");
		}
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateFirstName(HttpServletRequest req, String firstName) {
		// Validation for firstName
		if(ValidationUtil.isNullOrEmpty(firstName)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateName(firstName)) {
			return "Should contain only alphabets.";
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateLastName(HttpServletRequest req, String lastName) {		
		// Validation for lastName
		if(ValidationUtil.isNullOrEmpty(lastName)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateName(lastName)) {
			return "Should contain only alphabets.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validatePhoneNum(HttpServletRequest req, String phoneNum) {
		
		// Validation for Phone number
		if(ValidationUtil.isNullOrEmpty(phoneNum)) {
			return "Required.";
		}
		else if(!ValidationUtil.validatePhoneNum(phoneNum)) {
			return "Start with 98 or 97, length: 10.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateEmail(HttpServletRequest req, String email) {		
		// Validation for email
		if(ValidationUtil.isNullOrEmpty(email)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateEmail(email)) {
			return "Invalid email format.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredPhoneNum(HttpServletRequest req, String phoneNum) {
		if (profileService.checkPhoneNum(phoneNum)) {
			return "phone number already exists";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredEmail(HttpServletRequest req, String email) {		
		if (profileService.checkEmail(email)) {
			return "email already exists";
		}
		return null;
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
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String firstNameError, String lastNameError,
			String phoneNumError, String emailError) throws ServletException, IOException {
		req.setAttribute("firstName", firstNameError);
		req.setAttribute("lastName", lastNameError);
		req.setAttribute("phoneNum", phoneNumError);
		req.setAttribute("email", emailError);
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
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
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @param usernameError
	 * @param phoneNumError
	 * @param emailError
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String phoneNumError, String emailError) 
			throws ServletException, IOException{
		req.setAttribute("phoneNum", phoneNumError);
		req.setAttribute("email", emailError);
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
}

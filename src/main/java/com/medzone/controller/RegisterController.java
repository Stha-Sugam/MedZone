package com.medzone.controller;

import java.io.IOException;

import java.time.LocalDate;
import com.medzone.model.UserModel;
import com.medzone.service.RegisterService;
import com.medzone.util.PasswordUtil;
import com.medzone.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *  implementation class RegisterController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Register" })
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Creating an object of RegisterService
	private final RegisterService registerService = new RegisterService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			
			String firstNameError = validateFirstName(req);
			String lastNameError = validateLastName(req);
			String userNameError = validateUserName(req);
			String phoneNumError = validatePhoneNum(req);
			String passwordError = validatePassword(req);
			String cpasswordError = validateCPassword(req);
			String emailError = validateEmail(req);
			
			
			if (firstNameError != null || lastNameError != null || userNameError != null || phoneNumError != null || passwordError != null
					|| cpasswordError != null || emailError != null) {
				handleInputError(req, resp, firstNameError, lastNameError, userNameError, phoneNumError, passwordError, cpasswordError, emailError);
				return;
			}
			
			String registeredUsername = validateRegisteredUsername(req);
			String registeredPhoneNum = validateRegisteredPhoneNum(req);
			String registeredEmail = validateRegisteredEmail(req);
			
			if (registeredUsername != null || registeredPhoneNum != null || registeredEmail != null) {
				handleRegisteredError(req, resp, registeredUsername, registeredPhoneNum, registeredEmail);
				return;
			}
			
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String encryptedPass = PasswordUtil.encrypt(username, password);
			
			// Creating a new instance of UserModel
			UserModel user = new UserModel(
				req.getParameter("first-name"), req.getParameter("last-name"), username,
				req.getParameter("phone-num"), encryptedPass, req.getParameter("email"),
				LocalDate.now(), false
			);
			
			// Adding the user to the database
			if (registerService.addUser(user)) {
				resp.sendRedirect("Login");
			}
			else {
				handleError(req, resp, "Failed to register. Try again.");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			handleError(req, resp, "Internal server error occured.");
		}
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateFirstName(HttpServletRequest req) {
		String firstName = req.getParameter("first-name");
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
	private String validateLastName(HttpServletRequest req) {
		String lastName = req.getParameter("last-name");
		
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
	private String validateUserName(HttpServletRequest req) {
		String userName = req.getParameter("username");
		
		// Validation for userName
		if(ValidationUtil.isNullOrEmpty(userName)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateUserName(userName)) {
			return "Start with a letter, use '_' or '.' only.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validatePhoneNum(HttpServletRequest req) {
		String phonenum = req.getParameter("phone-num");
		
		// Validation for Phone number
		if(ValidationUtil.isNullOrEmpty(phonenum)) {
			return "Required.";
		}
		else if(!ValidationUtil.validatePhoneNum(phonenum)) {
			return "Start with 98 or 97, length: 10.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validatePassword(HttpServletRequest req) {
		String password = req.getParameter("password");
		
		// Validation for password
		if(ValidationUtil.isNullOrEmpty(password)) {
			return "Required.";
		}
		else if(!ValidationUtil.validatePassword(password)) {
			return "length > 8, 1 uppercase, 1 number";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateCPassword(HttpServletRequest req) {
		String cpassword = req.getParameter("cpassword");
		String password = req.getParameter("password");
		
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
	 * @return
	 */
	private String validateEmail(HttpServletRequest req) {
		String email = req.getParameter("email");
		
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
	private String validateRegisteredUsername(HttpServletRequest req) {
		String username = req.getParameter("username");
		RegisterService registerService = new RegisterService();
		
		if (registerService.checkUsername(username)) {
			return "username already exists";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredPhoneNum(HttpServletRequest req) {
		String phoneNum = req.getParameter("phone-num");
		RegisterService registerService = new RegisterService();
		
		if (registerService.checkPhoneNum(phoneNum)) {
			return "phone number already exists";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredEmail(HttpServletRequest req) {
		String email = req.getParameter("email");
		RegisterService registerService = new RegisterService();
		
		if (registerService.checkEmail(email)) {
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
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String firstNameError, String lastNameError, String userNameError,
			String phoneNumError, String passwordError, String cpasswordError, String emailError) throws ServletException, IOException {
		req.setAttribute("firstName", firstNameError);
		req.setAttribute("lastName", lastNameError);
		req.setAttribute("userName", userNameError);
		req.setAttribute("phoneNum", phoneNumError);
		req.setAttribute("password", passwordError);
		req.setAttribute("cpassword", cpasswordError);
		req.setAttribute("email", emailError);
		req.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(req, resp);
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
		req.setAttribute("error", message);
		req.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(req, resp);
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
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String usernameError, String phoneNumError, String emailError) 
			throws ServletException, IOException{
		req.setAttribute("userName", usernameError);
		req.setAttribute("phoneNum", phoneNumError);
		req.setAttribute("email", emailError);
		req.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(req, resp);
	}
	

}

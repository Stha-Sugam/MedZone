package com.medzone.controller;

import java.io.IOException;

import java.time.LocalDateTime;
import com.medzone.model.UserModel;
import com.medzone.service.RegisterService;
import com.medzone.util.ImageUtil;
import com.medzone.util.PasswordUtil;
import com.medzone.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * implementation class RegisterController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ImageUtil imageUtil = new ImageUtil();
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String username = req.getParameter("username");
			String firstname = req.getParameter("firstName");
			String lastname = req.getParameter("lastName");
			String phonenum = req.getParameter("phoneNum");
			String password = req.getParameter("password");
			String cpassword = req.getParameter("cpassword");
			String email = req.getParameter("email");
			Part userImage = req.getPart("image");
			
			String userNameError = validateUserName(req, username);
			String firstNameError = validateFirstName(req, firstname);
			String lastNameError = validateLastName(req, lastname);
			String phoneNumError = validatePhoneNum(req, phonenum);
			String passwordError = validatePassword(req, password);
			String cpasswordError = validateCPassword(req, password, cpassword);
			String emailError = validateEmail(req, email);
			String imageError = null;
			
			String imageUrl = "default.png";
			
			if (userImage != null && userImage.getSize() > 0) {
				imageError = validateImage(userImage);
				if (imageError == null) {
					imageUrl = imageUtil.getImageNameFromPart(userImage);
					if (!uploadImage(req, userImage)) {
						handleError(req, resp, "Could not upload the new image. Please try again later!");
						return;
					}
				}
			}

			if (userNameError != null || firstNameError != null || lastNameError != null || phoneNumError != null
					|| passwordError != null || cpasswordError != null || emailError != null || imageError != null) {
				handleInputError(req, resp, userNameError, firstNameError, lastNameError, phoneNumError, passwordError,
						cpasswordError, emailError, imageError);
				return;
			}

			String registeredUsername = validateRegisteredUsername(req, username);
			String registeredPhoneNum = validateRegisteredPhoneNum(req, phonenum);
			String registeredEmail = validateRegisteredEmail(req, email);

			if (registeredUsername != null || registeredPhoneNum != null || registeredEmail != null) {
				handleRegisteredError(req, resp, registeredUsername, registeredPhoneNum, registeredEmail);
				return;
			}
			
			String encryptedPass = PasswordUtil.encrypt(username, password);

			// Creating a new instance of UserModel
			UserModel user = new UserModel(username, firstname, lastname, phonenum, encryptedPass, email, LocalDateTime.now(),
					false, imageUrl);

			// Adding the user to the database
			if (registerService.addUser(user)) {
				req.getSession().setAttribute("successRegister", true);
				resp.sendRedirect("Login");
			} else {
				handleError(req, resp, "Failed to register. Try again.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			handleError(req, resp, "Cannot Connect to Server. Please try again!");
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateUserName(HttpServletRequest req, String username) {
		// Validation for userName
		if (ValidationUtil.isNullOrEmpty(username)) {
			return "Required.";
		} else if (!ValidationUtil.validateUserName(username)) {
			return "Start with a letter, use '_' or '.' only.";
		}
		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateFirstName(HttpServletRequest req, String firstname) {
		// Validation for firstName
		if (ValidationUtil.isNullOrEmpty(firstname)) {
			return "Required.";
		} else if (!ValidationUtil.validateName(firstname)) {
			return "Should contain only alphabets.";
		}

		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateLastName(HttpServletRequest req, String lastname) {
		// Validation for lastName
		if (ValidationUtil.isNullOrEmpty(lastname)) {
			return "Required.";
		} else if (!ValidationUtil.validateName(lastname)) {
			return "Should contain only alphabets.";
		}
		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validatePhoneNum(HttpServletRequest req, String phonenum) {
		// Validation for Phone number
		if (ValidationUtil.isNullOrEmpty(phonenum)) {
			return "Required.";
		} else if (!ValidationUtil.validatePhoneNum(phonenum)) {
			return "Start with 98 or 97, length: 10.";
		}
		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validatePassword(HttpServletRequest req, String password) {
		// Validation for password
		if (ValidationUtil.isNullOrEmpty(password)) {
			return "Required.";
		} else if (!ValidationUtil.validatePassword(password)) {
			return "length > 8, 1 uppercase, 1 number";
		}
		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateCPassword(HttpServletRequest req, String password, String cpassword) {
		// Validation for confirm password
		if (ValidationUtil.isNullOrEmpty(cpassword)) {
			return "Required.";
		} else if (!ValidationUtil.matchPasswords(password, cpassword)) {
			return "Passwords do not match.";
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
		if (ValidationUtil.isNullOrEmpty(email)) {
			return "Required.";
		} else if (!ValidationUtil.validateEmail(email)) {
			return "Invalid email format.";
		}
		return null;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredUsername(HttpServletRequest req, String username) {
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
	private String validateRegisteredPhoneNum(HttpServletRequest req, String phonenum) {
		RegisterService registerService = new RegisterService();

		if (registerService.checkPhoneNum(phonenum)) {
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
		RegisterService registerService = new RegisterService();

		if (registerService.checkEmail(email)) {
			return "email already exists";
		}
		return null;
	}

	private String validateImage(Part imagePart) {
		if (imagePart != null && imagePart.getSize() > 0 && !ValidationUtil.isValidImageExtension(imagePart)) {
			return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
		}
		return null;
	}

	private boolean uploadImage(HttpServletRequest req, Part image) throws IOException, ServletException {
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "user");
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
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String userNameError,
			String firstNameError, String lastNameError, String phoneNumError, String passwordError,
			String cpasswordError, String emailError, String imageError) throws ServletException, IOException {
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("regUserName", req.getParameter("usename"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("cpassword", req.getParameter("cpassword"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("userNameErrors", userNameError);
		req.setAttribute("firstNameErrors", firstNameError);
		req.setAttribute("lastNameErrors", lastNameError);
		req.setAttribute("phoneNumErrors", phoneNumError);
		req.setAttribute("passwordErrors", passwordError);
		req.setAttribute("cpasswordErrors", cpasswordError);
		req.setAttribute("emailErrors", emailError);
		req.setAttribute("imageErrors", imageError);
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
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("regUserName", req.getParameter("username"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("cpassword", req.getParameter("cpassword"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("errorMessage", message);
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
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String usernameError,
			String phoneNumError, String emailError) throws ServletException, IOException {
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("regUserName", req.getParameter("username"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("cpassword", req.getParameter("cpassword"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("userNameErrors", usernameError);
		req.setAttribute("phoneNumErrors", phoneNumError);
		req.setAttribute("emailErrors", emailError);
		req.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(req, resp);
	}
}

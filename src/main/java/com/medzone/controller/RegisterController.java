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
 * Servlet implementation class RegisterController
 * Handles the registration of new users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Register" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Utility for handling image uploads
	private final ImageUtil imageUtil = new ImageUtil();

	// Service to handle registration logic (database interaction)
	private final RegisterService registerService = new RegisterService();

	public RegisterController() {
		super();
	}

	// Show the registration form (GET request)
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/pages/Register.jsp").forward(request, response);
	}

	// Handle registration form submission (POST request)
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// Get form data
			String username = req.getParameter("username");
			String firstname = req.getParameter("firstName");
			String lastname = req.getParameter("lastName");
			String phonenum = req.getParameter("phoneNum");
			String password = req.getParameter("password");
			String cpassword = req.getParameter("cpassword");
			String email = req.getParameter("email");
			Part userImage = req.getPart("image");

			// Validate user input
			String userNameError = validateUserName(req, username);
			String firstNameError = validateFirstName(req, firstname);
			String lastNameError = validateLastName(req, lastname);
			String phoneNumError = validatePhoneNum(req, phonenum);
			String passwordError = validatePassword(req, password);
			String cpasswordError = validateCPassword(req, password, cpassword);
			String emailError = validateEmail(req, email);
			String imageError = null;

			// default image if user didn't upload any
			String imageUrl = "default.png"; 

			// Handle image upload and validation
			if (userImage != null && userImage.getSize() > 0) {
				imageError = validateImage(userImage);
				if (imageError == null) {
					imageUrl = imageUtil.getImageNameFromPart(userImage);
					if (!uploadImage(req, userImage)) {
						handleError(req, resp, "Could not upload the image. Please try again later!");
						return;
					}
				}
			}
			// If there are any input validation errors, return to form
			if (userNameError != null || firstNameError != null || lastNameError != null || phoneNumError != null
					|| passwordError != null || cpasswordError != null || emailError != null || imageError != null) {
				handleInputError(req, resp, userNameError, firstNameError, lastNameError, phoneNumError, passwordError,
						cpasswordError, emailError, imageError);
				return;
			}
			// Check if username, phone, or email already exists
			String registeredUsername = validateRegisteredUsername(req, username);
			String registeredPhoneNum = validateRegisteredPhoneNum(req, phonenum);
			String registeredEmail = validateRegisteredEmail(req, email);

			if (registeredUsername != null || registeredPhoneNum != null || registeredEmail != null) {
				handleRegisteredError(req, resp, registeredUsername, registeredPhoneNum, registeredEmail);
				return;
			}
			// Encrypt password before saving
			String encryptedPass = PasswordUtil.encrypt(username, password);
			// Create new user model instance
			UserModel user = new UserModel(username, firstname, lastname, phonenum, encryptedPass, email, LocalDateTime.now(),
					false, imageUrl);
			// Add user to database
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

	private String validateUserName(HttpServletRequest req, String username) {
		if (ValidationUtil.isNullOrEmpty(username)) {
			return "Required.";
		} else if (!ValidationUtil.validateUserName(username)) {
			return "Start with a letter, use '_' or '.' only.";
		}
		return null;
	}

	private String validateFirstName(HttpServletRequest req, String firstname) {
		if (ValidationUtil.isNullOrEmpty(firstname)) {
			return "Required.";
		} else if (!ValidationUtil.validateName(firstname)) {
			return "Should contain only alphabets.";
		}
		return null;
	}

	private String validateLastName(HttpServletRequest req, String lastname) {
		if (ValidationUtil.isNullOrEmpty(lastname)) {
			return "Required.";
		} else if (!ValidationUtil.validateName(lastname)) {
			return "Should contain only alphabets.";
		}
		return null;
	}

	private String validatePhoneNum(HttpServletRequest req, String phonenum) {
		if (ValidationUtil.isNullOrEmpty(phonenum)) {
			return "Required.";
		} else if (!ValidationUtil.validatePhoneNum(phonenum)) {
			return "Start with 98 or 97, length: 10.";
		}
		return null;
	}

	private String validatePassword(HttpServletRequest req, String password) {
		if (ValidationUtil.isNullOrEmpty(password)) {
			return "Required.";
		} else if (!ValidationUtil.validatePassword(password)) {
			return "length > 8, 1 uppercase, 1 number";
		}
		return null;
	}

	private String validateCPassword(HttpServletRequest req, String password, String cpassword) {
		if (ValidationUtil.isNullOrEmpty(cpassword)) {
			return "Required.";
		} else if (!ValidationUtil.matchPasswords(password, cpassword)) {
			return "Passwords do not match.";
		}
		return null;
	}

	private String validateEmail(HttpServletRequest req, String email) {
		if (ValidationUtil.isNullOrEmpty(email)) {
			return "Required.";
		} else if (!ValidationUtil.validateEmail(email)) {
			return "Invalid email format.";
		}
		return null;
	}

	private String validateRegisteredUsername(HttpServletRequest req, String username) {
		if (registerService.checkUsername(username)) {
			return "username already exists";
		}
		return null;
	}

	private String validateRegisteredPhoneNum(HttpServletRequest req, String phonenum) {
		if (registerService.checkPhoneNum(phonenum)) {
			return "phone number already exists";
		}
		return null;
	}

	private String validateRegisteredEmail(HttpServletRequest req, String email) {
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

	// Handles form input validation errors
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String userNameError,
			String firstNameError, String lastNameError, String phoneNumError, String passwordError,
			String cpasswordError, String emailError, String imageError) throws ServletException, IOException {

		// Preserve previously entered values
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("regUserName", req.getParameter("username"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("cpassword", req.getParameter("cpassword"));
		req.setAttribute("email", req.getParameter("email"));

		// Set error messages
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

	// Handles unexpected server errors or image upload failures
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {

		// Preserve previously entered values
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

	// Handles registration conflicts (e.g., username/email/phone already exists)
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String usernameError,
			String phoneNumError, String emailError) throws ServletException, IOException {

		// Preserve previously entered values
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

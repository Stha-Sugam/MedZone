package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import com.medzone.model.UserModel;
import com.medzone.service.ProfileService;
import com.medzone.util.ImageUtil;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class UpdateProfileController
 * 
 * Handles both GET and POST requests for updating user profile information including
 * first name, last name, phone number, email, and profile image upload.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/UpdateProfile" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB threshold after which files will be written to disk
    maxFileSize = 1024 * 1024 * 10,       // Max single file size 10MB
    maxRequestSize = 1024 * 1024 * 50     // Max request size 50MB
)
public class UpdateProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Service class instance to handle profile-related business logic and database operations
	private final ProfileService profileService = new ProfileService();
	
	// Utility class for handling image upload and processing
	private final ImageUtil imageUtil = new ImageUtil();

    /**
     * Default constructor
     */
    public UpdateProfileController() {
        super();
    }

	/**
	 * Handles GET requests to load existing user info to edit profile page.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check if there is a database error retrieving user info
		if(profileService.DatabaseError()) {
			// Set error message in session scope if database is unreachable or error occurs
			SessionUtil.setAttribute(request, "errorMessage", "Database Error. Cannot Retrieve Information.");
		}
		else {
			// Retrieve the username from session
			String username = (String) SessionUtil.getAttribute(request, "username");
			// Get the current user's info from the database
			UserModel userInfo = profileService.getUserInfo(new UserModel(username));
			// Store the user info object back into session for use in JSP
			SessionUtil.setAttribute(request, "user", userInfo);
		}
		// Set the active tab/section on profile page to 'edit-profile'
		request.setAttribute("activeSection", "edit-profile");
		// Forward the request to ProfileInfo.jsp to render the page
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests to update the user's profile info including image upload.
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// Retrieve the original user info stored in session (before update)
			UserModel orgUser = (UserModel) SessionUtil.getAttribute(req, "user");
			
			// Current profile image URL before update
			String imageUrl = orgUser.getImageUrl();
			
			// Flag to check if a new image was uploaded
			boolean newImageUpload = false;
			
			// Get updated input values from the form and trim whitespace
			String newFirstName = req.getParameter("firstName").trim();
			String newLastName = req.getParameter("lastName").trim();
			String newPhoneNum = req.getParameter("phoneNum").trim();
			String newEmail = req.getParameter("email").trim();
			
			// Retrieve the image file part from multipart/form-data request
			Part newUserImage = req.getPart("newImage");

			// Validate the input fields and image
			String firstNameError = validateFirstName(req, newFirstName);
			String lastNameError = validateLastName(req, newLastName);
			String phoneNumError = validatePhoneNum(req, newPhoneNum);
			String emailError = validateEmail(req, newEmail);
			String imageError = null;
			
			// If a new image is uploaded, validate it and upload if valid
			if (newUserImage != null && newUserImage.getSize() > 0) {
				imageError = validateImage(newUserImage);
				if (imageError == null) {
					// Generate image filename
					imageUrl = imageUtil.getImageNameFromPart(newUserImage);
					// Upload image to server directory
					if (!uploadImage(req, newUserImage)) {
						// Handle upload failure gracefully
						handleError(req, resp, "Could not upload the new image. Please try again later!");
						return;
					}
					newImageUpload = true;
				}
			}

			// If there are any validation errors, forward back to the form with error messages
			if (firstNameError != null || lastNameError != null || phoneNumError != null || emailError != null || imageError != null) {
				handleInputError(req, resp, firstNameError, lastNameError, phoneNumError, emailError, imageError);
				return;
			}

			// Check if any changes were actually made
			if(!newImageUpload && newFirstName.equals(orgUser.getFirstName()) && newLastName.equals(orgUser.getLastName()) && 
					newPhoneNum.equals(orgUser.getPhone()) && newEmail.equals(orgUser.getEmail())) {
				handleError(req, resp, "No changes detected. Please modify some fields before updating.");
				return;
			}

			// Validate if the new phone number or email already exists in the database for other users
			String registeredPhone = null;
			String registeredEmail = null;

			if(!newPhoneNum.equals(orgUser.getPhone())) {
				registeredPhone = validateRegisteredPhoneNum(req, newPhoneNum);
			}
			if(!newEmail.equals(orgUser.getEmail())){
				registeredEmail = validateRegisteredEmail(req, newEmail);
			}

			// If phone or email already exists, send error back to form
			if(registeredPhone != null || registeredEmail != null) {
				handleRegisteredError(req, resp, registeredPhone, registeredEmail);
				return;
			}

			// Get username from session for the update
			String username = (String) SessionUtil.getAttribute(req, "username");

			// Create a new UserModel object with updated info
			UserModel updatedUser = new UserModel(username, newFirstName, newLastName, newPhoneNum, newEmail, imageUrl);

			// Attempt to update the user profile in the database
			if (profileService.updateUser(updatedUser)) {
				// On success, set a success message in session and redirect to profile info page
				SessionUtil.setAttribute(req, "successMessage", "Your profile info has been successfuly updated.");
				req.setAttribute("activeSection", "info-section");
				resp.sendRedirect("ProfileInfo");
			}
			else {
				// If update fails, forward back with error message
				handleError(req, resp, "Failed to update. Try again.");
			}
		}
		catch(Exception e) {
			// Handle any unexpected exceptions
			e.printStackTrace();
			handleError(req, resp, "Cannot Connect to Server. Please try again!");
		}
	}

	/**
	 * Validates the first name field.
	 */
	private String validateFirstName(HttpServletRequest req, String firstName) {
		if(ValidationUtil.isNullOrEmpty(firstName)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateName(firstName)) {
			return "Should contain only alphabets.";
		}
		return null;
	}

	/**
	 * Validates the last name field.
	 */
	private String validateLastName(HttpServletRequest req, String lastName) {
		if(ValidationUtil.isNullOrEmpty(lastName)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateName(lastName)) {
			return "Should contain only alphabets.";
		}
		return null;
	}

	/**
	 * Validates the phone number field.
	 */
	private String validatePhoneNum(HttpServletRequest req, String phoneNum) {
		if(ValidationUtil.isNullOrEmpty(phoneNum)) {
			return "Required.";
		}
		else if(!ValidationUtil.validatePhoneNum(phoneNum)) {
			return "Start with 98 or 97, length: 10.";
		}
		return null;
	}

	/**
	 * Validates the email field.
	 */
	private String validateEmail(HttpServletRequest req, String email) {
		if(ValidationUtil.isNullOrEmpty(email)) {
			return "Required.";
		}
		else if(!ValidationUtil.validateEmail(email)) {
			return "Invalid email format.";
		}
		return null;
	}

	/**
	 * Checks if the new phone number is already registered by another user.
	 */
	private String validateRegisteredPhoneNum(HttpServletRequest req, String phoneNum) {
		if (profileService.checkPhoneNum(phoneNum)) {
			return "phone number already exists";
		}
		return null;
	}

	/**
	 * Checks if the new email is already registered by another user.
	 */
	private String validateRegisteredEmail(HttpServletRequest req, String email) {
		if (profileService.checkEmail(email)) {
			return "email already exists";
		}
		return null;
	}
	
	/**
	 * Validates the uploaded image part for allowed extensions.
	 */
	private String validateImage(Part imagePart) {
		if (imagePart != null && imagePart.getSize() > 0 && !ValidationUtil.isValidImageExtension(imagePart)) {
			return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
		}
		return null;
	}

	/**
	 * Uploads the image to the server folder.
	 */
	private boolean uploadImage(HttpServletRequest req, Part image) throws IOException, ServletException {
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "user");
	}

	/**
	 * Handles forwarding to the edit profile section with validation error messages and
	 * pre populates the form fields.
	 */
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String firstNameError, String lastNameError,
			String phoneNumError, String emailError, String imageError) throws ServletException, IOException {
		req.setAttribute("firstNameErrors", firstNameError);
		req.setAttribute("lastNameErrors", lastNameError);
		req.setAttribute("phoneNumErrors", phoneNumError);
		req.setAttribute("emailErrors", emailError);
		req.setAttribute("imageErrors", imageError);
		
		// Also re-populate form fields so user doesn't lose input
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}

	/**
	 * Handles general error forwarding with a message and active section set to edit-profile.
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException{
		req.setAttribute("errorMessage", message);
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}

	/**
	 * Handles the case when phone number or email already exists in the database,
	 * forwards with respective error messages.
	 */
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String phoneNumError, String emailError)
			throws ServletException, IOException{
		req.setAttribute("phoneNum", phoneNumError);
		req.setAttribute("email", emailError);
		
		// Re-populate form fields so user input is preserved
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
}

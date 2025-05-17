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
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/UpdateProfile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UpdateProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Creating an object of RegisterService
	private final ProfileService profileService = new ProfileService();
	
	private final ImageUtil imageUtil = new ImageUtil();

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
			
			String imageUrl = orgUser.getImageUrl();
			
			boolean newImageUpload = false;
			String newFirstName = req.getParameter("firstName").trim();
			String newLastName = req.getParameter("lastName").trim();
			String newPhoneNum = req.getParameter("phoneNum").trim();
			String newEmail = req.getParameter("email").trim();
			Part newUserImage = req.getPart("newImage");

			String firstNameError = validateFirstName(req, newFirstName);
			String lastNameError = validateLastName(req, newLastName);
			String phoneNumError = validatePhoneNum(req, newPhoneNum);
			String emailError = validateEmail(req, newEmail);
			String imageError = null;
			
			if (newUserImage != null && newUserImage.getSize() > 0) {
				imageError = validateImage(newUserImage);
				if (imageError == null) {
					imageUrl = imageUtil.getImageNameFromPart(newUserImage);
					if (!uploadImage(req, newUserImage)) {
						handleError(req, resp, "Could not upload the new image. Please try again later!");
						return;
					}
					newImageUpload = true;
				}
			}


			if (firstNameError != null || lastNameError != null || phoneNumError != null || emailError != null || imageError != null) {
				handleInputError(req, resp, firstNameError, lastNameError, phoneNumError, emailError, imageError);
				return;
			}

			if(!newImageUpload && newFirstName.equals(orgUser.getFirstName()) && newLastName.equals(orgUser.getLastName()) && 
					newPhoneNum.equals(orgUser.getPhone()) && newEmail.equals(orgUser.getEmail())) {
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
			UserModel updatedUser = new UserModel(username, newFirstName, newLastName, newPhoneNum, newEmail, imageUrl);

			// updating the user to the database
			if (profileService.updateUser(updatedUser)) {
				SessionUtil.setAttribute(req, "successMessage", "Your profile info has been successfuly updated.");
				req.setAttribute("activeSection", "info-section");
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
	
	private String validateImage(Part imagePart) {
		if (imagePart != null && imagePart.getSize() > 0 && !ValidationUtil.isValidImageExtension(imagePart)) {
			return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
		}
		return null;
	}

	private boolean uploadImage(HttpServletRequest req, Part image) throws IOException, ServletException {
		return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "user");
	}

	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String firstNameError, String lastNameError,
			String phoneNumError, String emailError, String imageError) throws ServletException, IOException {
		req.setAttribute("firstNameErrors", firstNameError);
		req.setAttribute("lastNameErrors", lastNameError);
		req.setAttribute("phoneNumErrors", phoneNumError);
		req.setAttribute("emailErrors", emailError);
		req.setAttribute("imageErrors", imageError);
		
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}

	
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
		
		req.setAttribute("firstName", req.getParameter("firstName"));
		req.setAttribute("lastName", req.getParameter("lastName"));
		req.setAttribute("phoneNum", req.getParameter("phoneNum"));
		req.setAttribute("email", req.getParameter("email"));
		
		req.setAttribute("activeSection", "edit-profile");
		req.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(req, resp);
	}
}
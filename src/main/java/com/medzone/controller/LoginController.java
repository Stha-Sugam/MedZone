package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.model.UserModel;
import com.medzone.service.LoginService;
import com.medzone.util.CookieUtil;
import com.medzone.util.SessionUtil;
import com.medzone.util.ValidationUtil;

/**
 * Servlet implementation class LoginController
 * Handles user login logic for both admin and regular users.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Login", "/" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service that handles login validation and user authentication
    private final LoginService loginService = new LoginService();

    /**
     * Default constructor
     */
    public LoginController() {
        super();
    }

    /**
     * Handles GET request to show the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(request, response);
    }

    /**
     * Handles POST request to perform login logic and redirect user accordingly.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            // Extract form parameters
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            // Validate user input
            String userNameError = validateUserName(req, username);
            String passwordError = validatePassword(req, password);

            // If any validation error, return to login page with error messages
            if (userNameError != null || passwordError != null) {
                handleInputError(req, resp, userNameError, passwordError);
                return;
            }

            // Check if username exists in database
            String registeredUsername = validateRegisteredUsername(req, username);
            if (registeredUsername != null) {
                handleRegisteredError(req, resp, registeredUsername);
                return;
            }

            // Create user model and validate login
            UserModel user = new UserModel(username, password);
            Boolean loginStatus = loginService.loginUser(user);

            if (loginStatus != null && loginStatus) {
                // If login successful
            	// Store username in session
                SessionUtil.setAttribute(req, "username", username); 
             // Store user profile image
                SessionUtil.setAttribute(req, "userImage", loginService.extractUserImage(username)); 

                // Redirect based on user role
                if (user.isAdmin()) {
                	// Set role cookie
                    CookieUtil.addCookie(resp, "role", "admin", 5 * 40); 
                    resp.sendRedirect(req.getContextPath() + "/Admin");
                } else {
                	// Set role cookie
                    CookieUtil.addCookie(resp, "role", "customer", 5 * 150); 
                    resp.sendRedirect(req.getContextPath() + "/Home");
                }
                return;

            } else if (loginStatus != null && !loginStatus) {
                // If password mismatch
                req.setAttribute("passwordErrors", "Password mismatch. Please try again.");
                req.setAttribute("logUserName", req.getParameter("username"));
                req.setAttribute("password", req.getParameter("password"));
                req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
                return;

            } else {
                // Login failed for unknown reasons
                req.setAttribute("logUserName", req.getParameter("username"));
                req.setAttribute("errorMessage", "Failed to Login. Please try again later!");
                req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "An unexpected error occurred during login.");
            req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
        }
    }

    /**
     * Validates the username input.
     * @param req the request
     * @param username the entered username
     * @return error message or null if valid
     */
    private String validateUserName(HttpServletRequest req, String username) {
        if (ValidationUtil.isNullOrEmpty(username)) {
            return "Username is Required.";
        }
        return null;
    }

    /**
     * Validates the password input.
     * @param req the request
     * @param password the entered password
     * @return error message or null if valid
     */
    private String validatePassword(HttpServletRequest req, String password) {
        if (ValidationUtil.isNullOrEmpty(password)) {
            return "Password is Required.";
        }
        return null;
    }

    /**
     * Checks if the username exists in the database.
     * @param req the request
     * @param username the entered username
     * @return error message if not found or null if exists
     */
    private String validateRegisteredUsername(HttpServletRequest req, String username) {
        LoginService loginService = new LoginService();
        if (!loginService.checkUsername(username)) {
            return "Username does not exist.";
        }
        return null;
    }

    /**
     * Handles input validation errors and forwards back to login page.
     */
    private void handleInputError(HttpServletRequest req, HttpServletResponse resp, 
            String userNameError, String passwordError) throws ServletException, IOException {
        req.setAttribute("logUserName", req.getParameter("username"));
        req.setAttribute("logUserNameErrors", userNameError);
        req.setAttribute("passwordErrors", passwordError);
        req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
    }

    /**
     * Handles case when username is not found in the system.
     */
    private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, 
            String usernameError) throws ServletException, IOException {
        req.setAttribute("logUserName", req.getParameter("username"));
        req.setAttribute("logUserNameErrors", usernameError);
        req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
    }
}

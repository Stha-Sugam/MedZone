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
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Login","/" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final LoginService loginService = new LoginService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			String userNameError = validateUserName(req, username);
			String passwordError = validatePassword(req, password);
			
			if (userNameError != null || passwordError != null) {
				handleInputError(req, resp, userNameError, passwordError);
				return;
			}
			
			String registeredUsername = validateRegisteredUsername(req, username);
			
			if (registeredUsername != null) {
				handleRegisteredError(req, resp, registeredUsername);
				return;
			}
			
			UserModel user = new UserModel(username, password);
			
			Boolean loginStatus = loginService.loginUser(user);
			
			if (loginStatus != null && loginStatus) {
				SessionUtil.setAttribute(req, "username", username);
				if (user.isAdmin()) {
					CookieUtil.addCookie(resp, "role", "admin", 5 * 40);
					resp.sendRedirect(req.getContextPath() + "/Admin");
				} else {
					CookieUtil.addCookie(resp, "role", "customer", 5 * 150);
					resp.sendRedirect(req.getContextPath() + "/Home");
				}
				return;
			} else if (loginStatus != null && !loginStatus) {
				req.setAttribute("password", "Password mismatch. Please try again.");
				req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
				return;
			} else {
				req.setAttribute("errorMessage", "Failed to Login. Please try again later!");
				req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			req.setAttribute("errorMessage", "Not empty");
			req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
		}
		
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateUserName(HttpServletRequest req, String username) {
		
		// Validation for userName
		if(ValidationUtil.isNullOrEmpty(username)) {
			return "Username is Required.";
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
		if(ValidationUtil.isNullOrEmpty(password)) {
			return "Password is Required.";
		}
		return null;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	private String validateRegisteredUsername(HttpServletRequest req, String username) {
		LoginService loginService = new LoginService();
		
		if (!loginService.checkUsername(username)) {
			return "Username does not exist";
		}
		return null;
	}
	
	private void handleInputError(HttpServletRequest req, HttpServletResponse resp, String userNameError, String passwordError) throws ServletException, IOException {
		req.setAttribute("userName", userNameError);
		req.setAttribute("password", passwordError);
		req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
	}
	
	private void handleRegisteredError(HttpServletRequest req, HttpServletResponse resp, String usernameError) 
			throws ServletException, IOException{
		req.setAttribute("userName", usernameError);
		req.getRequestDispatcher("WEB-INF/pages/Login.jsp").forward(req, resp);
	}

}

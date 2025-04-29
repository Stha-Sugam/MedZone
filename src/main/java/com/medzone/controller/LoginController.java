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
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		if(ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)) {
			handleLoginError(req, resp, "Fill all the fields");
			return;
		}
		UserModel user = new UserModel(username, password);
		System.out.print("user is Admin: " + user.isAdmin() + "\n");
		Boolean loginStatus = loginService.loginUser(user);
		
		if (loginStatus != null && loginStatus) {
			SessionUtil.setAttribute(req, "username", username);
			if (user.isAdmin()) {
				System.out.println("inside admin");
				CookieUtil.addCookie(resp, "role", "admin", 5 * 50);
				resp.sendRedirect(req.getContextPath() + "/Admin");
			} else {
				CookieUtil.addCookie(resp, "role", "customer", 5 * 50);
				resp.sendRedirect(req.getContextPath() + "/Home");
			}
			return;
		} else {
			handleLoginFailure(req, resp, loginStatus);
		}
	}
	
	/**
	 * Handles login failures by setting attributes and forwarding to the login
	 * page.
	 *
	 * @param req         HttpServletRequest object
	 * @param resp        HttpServletResponse object
	 * @param loginStatus Boolean indicating the login status
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
			throws ServletException, IOException {
		String errorMessage;
		if (loginStatus == null) {
			errorMessage = "Our server is under maintenance. Please try again later!";
		} else {
			errorMessage = "User credential mismatch. Please try again!";
		}
		req.setAttribute("errorMessage", errorMessage);
		req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
	}
	
	private void handleLoginError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("errorMessage", message);
		req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
	}

}

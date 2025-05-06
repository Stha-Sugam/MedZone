package com.medzone.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.medzone.util.CookieUtil;
import com.medzone.util.SessionUtil;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/Logout"})
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Invalidate session
	    SessionUtil.invalidateSession(request);
	    // Delete cookies (role cookie here)
	    CookieUtil.deleteCookie(response, "role");
	    
	    // Redirect to Login page
	    response.sendRedirect(request.getContextPath() + "/Login");
	}
}
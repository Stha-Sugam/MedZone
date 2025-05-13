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

/**
 * Servlet implementation class ProfileController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/ProfileInfo" })
public class ProfileInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ProfileService profileService = new ProfileService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileInfoController() {
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
		request.getRequestDispatcher("WEB-INF/pages/ProfileInfo.jsp").forward(request, response);
	}
}

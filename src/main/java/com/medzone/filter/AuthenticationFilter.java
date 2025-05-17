package com.medzone.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.medzone.util.CookieUtil;
import com.medzone.util.SessionUtil;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class AuthenticationFilter extends HttpFilter implements Filter {
    
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN = "/Login";
	private static final String REGISTER = "/Register";
	private static final String HOME = "/Home";
	private static final String ROOT = "/";
	private static final String DASHBOARD = "/Admin";
	private static final String ABOUT = "/About";
	private static final String CONTACT = "/Contact";
	private static final String BROWSE = "/Browse";
	private static final String PROFILEINFO = "/ProfileInfo";
	private static final String EDITPROFILE = "/UpdateProfile";
	private static final String EDITPASS = "/UpdatePassword";
	private static final String LOGOUT = "/Logout";
	
	private static final String MANAGEMEDS = "/ManageMed";
	private static final String ADDMED = "/AddMed";
	private static final String UPDATEMED = "/UpdateMed";
	private static final String DELETEMED = "/DeleteMed";
	private static final String MANAGETICKETS = "/ManageTicket";
	private static final String MANAGEUSERS = "/ManageUser";
	

    /**
     * @see HttpFilter#HttpFilter()
     */
    public AuthenticationFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		
		// Allow access to resources
		if (uri.endsWith(".css") || uri.endsWith(".png")){
			chain.doFilter(request, response);
			return;
		}
		
		boolean isLoggedIn = SessionUtil.getAttribute(req, "username") != null;
		
		String userRole = CookieUtil.getCookie(req, "role") != null ? CookieUtil.getCookie(req, "role").getValue(): null;
		if (isLoggedIn) {
	        request.setAttribute("role", userRole);
	    }
		
		if ("admin".equals(userRole)) {
			System.out.println(isLoggedIn);
			if(isLoggedIn) {
				// Admin is logged in
				if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
					res.sendRedirect(req.getContextPath() + DASHBOARD);
				} else if (uri.endsWith(DASHBOARD) || uri.endsWith(HOME) || uri.endsWith(LOGOUT) || uri.endsWith(MANAGEUSERS) || uri.endsWith(MANAGETICKETS)
						|| uri.endsWith(MANAGEMEDS) || uri.endsWith(ADDMED) || uri.endsWith(UPDATEMED) || uri.endsWith(DELETEMED) || uri.endsWith(MANAGETICKETS)){
					chain.doFilter(request, response);
				} else if (uri.endsWith(PROFILEINFO) || uri.endsWith(EDITPROFILE) || uri.endsWith(EDITPASS) || uri.endsWith(BROWSE) || uri.endsWith(CONTACT) || uri.endsWith(ABOUT)) {
					res.sendRedirect(req.getContextPath() + DASHBOARD);
				} else {
					res.sendRedirect(req.getContextPath() + DASHBOARD);
				}
			}
			else {
				// Not logged in
				if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
					chain.doFilter(request, response);
				} else {
					res.sendRedirect(req.getContextPath() + ROOT);
				}
			}
		} else if ("customer".equals(userRole)) {
			if(isLoggedIn) {
				// User is logged in
				if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
					res.sendRedirect(req.getContextPath() + HOME);
				} else if (uri.endsWith(HOME) || uri.endsWith(ABOUT) || uri.endsWith(PROFILEINFO) || uri.endsWith(EDITPROFILE) || uri.endsWith(EDITPASS)
						|| uri.endsWith(CONTACT) || uri.endsWith(BROWSE) || uri.endsWith(LOGOUT)){
					chain.doFilter(request, response);
				} else if (uri.endsWith(DASHBOARD)) {
					res.sendRedirect(req.getContextPath() + HOME);
				} else {
					res.sendRedirect(req.getContextPath() + HOME);
				}
			}else {
				// Not logged in
				if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
					chain.doFilter(request, response);
				} else {
					res.sendRedirect(req.getContextPath() + ROOT);
				}
			}
		}else {
			// Not logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath() + ROOT);
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}

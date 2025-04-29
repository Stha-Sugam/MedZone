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
	private static final String PROFILE = "/Profile";

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
		System.out.println("Username: " + SessionUtil.getAttribute(req, "username"));
		System.out.println("user logged in: " + isLoggedIn);
		String userRole = CookieUtil.getCookie(req, "role") != null ? CookieUtil.getCookie(req, "role").getValue(): null;
		System.out.println("Cookie: " + CookieUtil.getCookie(req, "role"));
		System.out.println("Role of user: " + userRole);
		
		if ("admin".equals(userRole)) {
			// Admin is logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
				res.sendRedirect(req.getContextPath() + DASHBOARD);
			} else if (uri.endsWith(DASHBOARD) || uri.endsWith(HOME) || uri.endsWith(ROOT)) {
				chain.doFilter(request, response);
			} else if (uri.endsWith(PROFILE) || uri.endsWith(BROWSE) || uri.endsWith(CONTACT) || uri.endsWith(ABOUT)) {
				res.sendRedirect(req.getContextPath() + DASHBOARD);
			} else {
				res.sendRedirect(req.getContextPath() + DASHBOARD);
			}
		} else if ("customer".equals(userRole)) {
			// User is logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER)) {
				res.sendRedirect(req.getContextPath() + HOME);
			} else if (uri.endsWith(HOME) || uri.endsWith(ROOT) || uri.endsWith(ABOUT) || uri.endsWith(PROFILE)
					|| uri.endsWith(CONTACT) || uri.endsWith(BROWSE)) {
				chain.doFilter(request, response);
			} else if (uri.endsWith(DASHBOARD)) {
				res.sendRedirect(req.getContextPath() + HOME);
			} else {
				res.sendRedirect(req.getContextPath() + HOME);
			}
		} else {
			// Not logged in
			if (uri.endsWith(LOGIN) || uri.endsWith(REGISTER) || uri.endsWith(ROOT)) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath() + LOGIN);
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

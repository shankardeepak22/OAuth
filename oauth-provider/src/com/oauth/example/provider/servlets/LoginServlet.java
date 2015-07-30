package com.oauth.example.provider.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oauth.example.dao.UserDao;
import com.oauth.example.modal.User;
import com.oauth.example.modal.UserRoles;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDao = new UserDao();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = null;
		UserRoles role = null;
		String userName = null;
		String encPassword = null;
		String userRole = null;
		HttpSession session = request.getSession();

		if (request.getParameter("userName") != null && request.getParameter("password") != null) {
			userName = request.getParameter("userName").toString();
			encPassword = String.valueOf(request.getParameter("password").hashCode());
		}

		user = userDao.getAuthorizedUser(userName, encPassword);
		role = userDao.getUserRole(user);

		if (user != null && user.getUserName().equals(userName)) {
			session.setAttribute("username", userName);

			if (role != null) {
				userRole = role.getRoles().getName();
				session.setAttribute("role", userRole);
			}
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/signinerror.jsp").forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

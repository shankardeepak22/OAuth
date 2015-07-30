package com.oauth.example.provider.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oauth.example.dao.UserDao;
import com.oauth.example.modal.Roles;
import com.oauth.example.modal.User;
import com.oauth.example.modal.UserCredentials;
import com.oauth.example.modal.UserRoles;

/**
 * Servlet implementation class AdminCreationServlet
 */
@WebServlet("/AdminCreationServlet")
public class AdminCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminCreationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			String userName = request.getParameter("userName");
			String userEmail = request.getParameter("email");
			String encPassword = String.valueOf(request.getParameter("password").hashCode());
			HttpSession session = request.getSession();

			User user = new User();
			user.setEmailId(userEmail);
			user.setUserName(userName);

			UserCredentials credentials = new UserCredentials();
			credentials.setUser(user);
			credentials.setPassword(encPassword);

			UserRoles roles = new UserRoles();
			roles.setUser(user);
			roles.setRoles(Roles.ROLE_ADMIN);
			UserDao userDao = new UserDao();
			user = userDao.save(user, credentials, roles);
			if (user != null && user.getId() != null) {
				session.setAttribute("username", userName);
				session.setAttribute("role", roles.getRoles().getName());
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			} else {
				request.setAttribute("isNewRegUser", new String("false").trim());
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
		} catch (Exception e) {

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

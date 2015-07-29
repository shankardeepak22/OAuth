package com.oauth.example.provider.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.example.crypto.Secret;
import com.oauth.example.dao.UserDao;
import com.oauth.example.modal.Roles;
import com.oauth.example.modal.User;
import com.oauth.example.modal.UserCredentials;
import com.oauth.example.modal.UserRoles;

/**
 * Servlet implementation class UserCreationServlet
 */
@WebServlet("/UserCreationServlet")
public class UserCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserCreationServlet() {
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

			User user = new User();
			user.setEmailId(userEmail);
			user.setUserName(userName);

			UserCredentials credentials = new UserCredentials();
			credentials.setUser(user);
			credentials.setPassword(encPassword);

			UserRoles roles = new UserRoles();
			roles.setUser(user);
			roles.setRoles(Roles.ROLE_USER);
			UserDao userDao = new UserDao();
			user = userDao.save(user, credentials, roles);
			if (user != null && user.getId() != null) {
				request.setAttribute("isNewRegUser", new String("true").trim());
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

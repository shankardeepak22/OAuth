package com.oauth.example.provider.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.example.dao.AppDao;
import com.oauth.example.exceptions.BusinessException;
import com.oauth.example.modal.App;
import com.oauth.example.provider.SampleOAuthProvider;

/**
 * Servlet implementation class AppCreationServlet
 */
@WebServlet(description = "Servelt used to create the Apps Authorized by the provider", urlPatterns = {
		"/AppCreationServlet" })
public class AppCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AppCreationServlet() {
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

			String appName = request.getParameter("appName");
			String appDesc = request.getParameter("appDesc");
			String callBackUrl = request.getParameter("callbackUrl");

			App app = new App();
			app.setAppName(appName);
			app.setAppDescription(appDesc);
			app.setCallbackUrl(callBackUrl);

			AppDao appdao = new AppDao();
			app = appdao.save(app);
			if (app != null && app.getAppId() != null && app.getAppSecret() != null) {

				request.setAttribute("appId", app.getAppId().toString());
				request.setAttribute("appSecret", app.getAppSecret());
				request.setAttribute("appDesc", app.getAppDescription());
				request.setAttribute("appCallback", app.getCallbackUrl());
				request.setAttribute("appName", app.getAppName());

				request.getRequestDispatcher("/app.jsp").forward(request, response);
			} else {
				throw new BusinessException("Could not create App!");
			}

		} catch (BusinessException e) {

			SampleOAuthProvider.handleException(e, request, response, true);

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

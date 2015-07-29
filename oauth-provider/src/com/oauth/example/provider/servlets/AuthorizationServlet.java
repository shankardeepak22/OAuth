package com.oauth.example.provider.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthMessage;
import com.oauth.example.provider.SampleOAuthProvider;
import com.oauth.provider.server.OAuthServlet;

/**
 * Servlet implementation class AuthorizationServlet
 */
@WebServlet("/authorize")
public class AuthorizationServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// nothing at this point
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);

			OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);

			if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
				// already authorized send the user back
				returnToConsumer(request, response, accessor);
			} else {
				sendToAuthorizePage(request, response, accessor);
			}

		} catch (Exception e) {
			SampleOAuthProvider.handleException(e, request, response, true);
		}

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);

			OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);

			String userName = request.getParameter("userName");

			String encPassword = String.valueOf(request.getParameter("password").hashCode());
			if (userName == null) {
				sendToAuthorizePage(request, response, accessor);
			}

			// set userId in accessor and mark it as authorized

			SampleOAuthProvider.markAuthority(accessor, userName, encPassword);

			returnToConsumer(request, response, accessor);

		} catch (Exception e) {
			SampleOAuthProvider.handleException(e, request, response, true);
		}
	}

	private void sendToAuthorizePage(HttpServletRequest request, HttpServletResponse response, OAuthAccessor accessor)
			throws IOException, ServletException {
		String callback = request.getParameter("oauth_callback");
		if (callback == null || callback.length() <= 0) {
			callback = "none";
		}
		String consumer_name = (String) accessor.consumer.getProperty("name");
		request.setAttribute("CONS_DESC", consumer_name);
		request.setAttribute("CALLBACK", callback);
		request.setAttribute("TOKEN", accessor.requestToken);
		request.getRequestDispatcher //
		("/authorize.jsp").forward(request, response);

	}

	private void returnToConsumer(HttpServletRequest request, HttpServletResponse response, OAuthAccessor accessor)
			throws IOException, ServletException {
		// send the user back to site's callBackUrl
		String callback = request.getParameter("oauth_callback");
		if ("none".equals(callback) && accessor.consumer.callbackURL != null
				&& accessor.consumer.callbackURL.length() > 0) {
			// first check if we have something in our properties file
			callback = accessor.consumer.callbackURL;
		}

		if ("none".equals(callback)) {
			// no call back it must be a client
			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println("You have successfully authorized '" + accessor.consumer.getProperty("description")
					+ "'. Please close this browser window and click continue" + " in the client.");
			out.close();
		} else {
			// if callback is not passed in, use the callback from config
			if (callback == null || callback.length() <= 0)
				callback = accessor.consumer.callbackURL;
			String token = accessor.requestToken;
			String userName = null;
			String userRole = null;
			if (accessor.getProperty("user") != null && accessor.getProperty("userRole") != null) {
				userName = (String) accessor.getProperty("user").toString();
				userRole = (String) accessor.getProperty("userRole").toString();

			}

			if (token != null) {
				callback = OAuth.addParameters(callback, "oauth_token", token, "username", userName, "userRole",
						userRole);
			}

			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);

			response.setHeader("Location", callback);
		}
	}

	private static final long serialVersionUID = 1L;

}

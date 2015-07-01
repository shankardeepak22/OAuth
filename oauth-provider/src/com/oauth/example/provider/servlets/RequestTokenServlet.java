package com.oauth.example.provider.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthMessage;
import com.oauth.example.provider.SampleOAuthProvider;
import com.oauth.provider.server.OAuthServlet;

/**
 * Servlet implementation class RequestTokenServlet
 */
@WebServlet("/request_token")
public class RequestTokenServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// nothing at this point
		try {
			SampleOAuthProvider.loadConsumers(config);
		} catch (IOException e) {
			throw new ServletException(e.getMessage());
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		processRequest(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		processRequest(request, response);
	}

	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try {
			OAuthMessage requestMessage = OAuthServlet
					.getMessage(request, null);

			OAuthConsumer consumer = SampleOAuthProvider
					.getConsumer(requestMessage);

			OAuthAccessor accessor = new OAuthAccessor(consumer);
			SampleOAuthProvider.VALIDATOR.validateMessage(requestMessage,
					accessor);
			{
				// Support the 'Variable Accessor Secret' extension
				// described in http://oauth.pbwiki.com/AccessorSecret
				String secret = requestMessage
						.getParameter("oauth_accessor_secret");
				if (secret != null) {
					accessor.setProperty(OAuthConsumer.ACCESSOR_SECRET, secret);
				}
			}
			// generate request_token and secret
			SampleOAuthProvider.generateRequestToken(accessor);

			response.setContentType("text/plain");
			OutputStream out = response.getOutputStream();
			OAuth.formEncode(OAuth.newList("oauth_token",
					accessor.requestToken, "oauth_token_secret",
					accessor.tokenSecret), out);
			out.close();

		} catch (Exception e) {
			SampleOAuthProvider.handleException(e, request, response, true);
		}

	}

	private static final long serialVersionUID = 1L;

}

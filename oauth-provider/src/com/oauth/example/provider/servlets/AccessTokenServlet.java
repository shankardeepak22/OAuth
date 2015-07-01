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
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.OAuthProblemException;
import com.oauth.example.provider.SampleOAuthProvider;
import com.oauth.provider.server.OAuthServlet;

/**
 * Servlet implementation class AccessTokenServlet
 */
@WebServlet("/access_token")
public class AccessTokenServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// nothing at this point
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

			OAuthAccessor accessor = SampleOAuthProvider
					.getAccessor(requestMessage);
			SampleOAuthProvider.VALIDATOR.validateMessage(requestMessage,
					accessor);

			// make sure token is authorized
			if (!Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
				OAuthProblemException problem = new OAuthProblemException(
						"permission_denied");
				throw problem;
			}
			// generate access token and secret
			SampleOAuthProvider.generateAccessToken(accessor);

			response.setContentType("text/plain");
			OutputStream out = response.getOutputStream();
			OAuth.formEncode(OAuth.newList("oauth_token", accessor.accessToken,
					"oauth_token_secret", accessor.tokenSecret), out);
			out.close();

		} catch (Exception e) {
			SampleOAuthProvider.handleException(e, request, response, true);
		}
	}

	private static final long serialVersionUID = 1L;

}

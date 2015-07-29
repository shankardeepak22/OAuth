package com.oauth.example.consumer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.OAuthProblemException;
import com.oauth.provider.server.OAuthServlet;

/**
 * Servlet implementation class CallBack
 */
@WebServlet("/Callback")
public class Callback extends HttpServlet {
	public static final String PATH = "/Callback";

	protected final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Exchange an OAuth request token for an access token, and store the latter
	 * in cookies.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OAuthConsumer consumer = null;
		try {
			final OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
			requestMessage.requireParameters("consumer", "username", "userRole");
			final String consumerName = requestMessage.getParameter("consumer");
			final String userName = requestMessage.getParameter("username");
			final String userRole = requestMessage.getParameter("userRole");
			HttpSession session = request.getSession();
			session.setAttribute("username", userName);
			session.setAttribute("role", userRole);
			System.out.println("Consumer Name: " + consumerName);
			System.out.println("User Name: " + userName);
			consumer = CookieConsumer.getConsumer(consumerName, null);
			final CookieMap cookies = new CookieMap(request, response);

			final OAuthAccessor accessor = CookieConsumer.newAccessor(consumer, cookies);
			final String expectedToken = accessor.requestToken;
			String requestToken = requestMessage.getParameter(OAuth.OAUTH_TOKEN);
			if (requestToken == null || requestToken.length() <= 0) {
				log.warning(request.getMethod() + " " + OAuthServlet.getRequestURL(request));
				requestToken = expectedToken;
				if (requestToken == null) {
					OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
					problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
					throw problem;
				}
			} else if (!requestToken.equals(expectedToken)) {
				OAuthProblemException problem = new OAuthProblemException("token_rejected");
				problem.setParameter("oauth_rejected_token", requestToken);
				problem.setParameter("oauth_expected_token", expectedToken);
				throw problem;
			}
			List<OAuth.Parameter> parameters = null;
			String verifier = requestMessage.getParameter(OAuth.OAUTH_VERIFIER);
			if (verifier != null) {
				parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, verifier);
			}
			OAuthMessage result = CookieConsumer.CLIENT.getAccessToken(accessor, null, parameters);
			if (accessor.accessToken != null) {
				String returnTo = requestMessage.getParameter("returnTo");

				if (returnTo == null) {
					returnTo = request.getContextPath(); // home page
				}
				cookies.remove(consumerName + ".requestToken");
				cookies.put(consumerName + ".accessToken", accessor.accessToken);
				cookies.put(consumerName + ".tokenSecret", accessor.tokenSecret);

				request.getRequestDispatcher("/welcomeUser.jsp").forward(request, response);
			} else {
				OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
				problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
				problem.getParameters().putAll(result.getDump());
				throw problem;
			}
		} catch (Exception e) {
			e.printStackTrace();
			CookieConsumer.handleException(e, request, response, consumer);
		}
	}

	private static final long serialVersionUID = 1L;
}

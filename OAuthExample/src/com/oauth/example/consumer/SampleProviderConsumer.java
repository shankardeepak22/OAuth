package com.oauth.example.consumer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthException;
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.ParameterStyle;
import com.oauth.provider.server.HttpRequestMessage;

/**
 * Servlet implementation class SampleProviderConsumer
 */
@WebServlet("/SampleProvider")
public class SampleProviderConsumer extends HttpServlet {
	private static final String NAME = "sample";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OAuthConsumer consumer = null;
		try {
			consumer = CookieConsumer.getConsumer(NAME, getServletContext());
			OAuthAccessor accessor = CookieConsumer.getAccessor(request, response, consumer);
			Collection<OAuth.Parameter> parameters = HttpRequestMessage.getParameters(request);

			response.setContentType("text/plain");
			PrintWriter out = response.getWriter();
			out.println("Sample Provider said:");

			String messageBody = invoke(accessor, parameters);
			String username = request.getParameter("username");
			System.out.println(username);
			request.setAttribute("access", accessor.accessToken);
			request.setAttribute("secret", accessor.tokenSecret);
			request.setAttribute("user", username);
			request.getRequestDispatcher("/welcomeUser.jsp").forward(request, response);
		} catch (Exception e) {
			CookieConsumer.handleException(e, request, response, consumer);
		}
	}

	private String invoke(OAuthAccessor accessor, Collection<? extends Map.Entry> parameters)
			throws OAuthException, IOException, URISyntaxException {
		URL baseURL = (URL) accessor.consumer.getProperty("serviceProvider.baseURL");
		if (baseURL == null) {
			baseURL = new URL("http://192.168.1.2:8484/oauth-provider/");
		}
		OAuthMessage request = accessor.newRequestMessage("POST", (new URL(baseURL, "echo")).toExternalForm(),
				parameters);
		OAuthMessage response = CookieConsumer.CLIENT.invoke(request, ParameterStyle.AUTHORIZATION_HEADER);
		String responseBody = response.readBodyAsString();
		return responseBody;
	}

	private static final long serialVersionUID = 1L;

}

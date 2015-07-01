package com.oauth.example.consumer;

import java.io.IOException;

import static com.oauth.commons.OAuth.HMAC_SHA1;
import static com.oauth.commons.OAuth.OAUTH_SIGNATURE_METHOD;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.ParameterStyle;

/**
 * Servlet implementation class GoogleContactsConsumer
 */
@WebServlet("/GoogleContactsConsumer")
public class GoogleContactsConsumer extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		OAuthConsumer consumer = null;
		try {
			consumer = CookieConsumer.getConsumer("googleContacts",
					getServletContext());
			OAuthAccessor accessor = CookieConsumer.getAccessor(request,
					response, consumer);
			// You can switch to a different signature method:
			accessor.consumer.setProperty(OAUTH_SIGNATURE_METHOD, HMAC_SHA1);
			// HMAC uses the access token secret as a factor,
			// and it's a little less compute-intensive than RSA.
			OAuthMessage message = accessor.newRequestMessage(OAuthMessage.GET,
					"http://www.google.com/m8/feeds/contacts/default/full",
					null);
			OAuthMessage result = CookieConsumer.CLIENT.invoke(message,
					ParameterStyle.AUTHORIZATION_HEADER);
			// Simply pass the data through to the browser:
			CookieConsumer.copyResponse(result, response);
		} catch (Exception e) {
			CookieConsumer.handleException(e, request, response, consumer);
		}
	}

	private static final long serialVersionUID = 1L;
}

/**
 * 
 */
package com.oauth.example.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthException;
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.OAuthProblemException;
import com.oauth.example.dao.AppDao;
import com.oauth.example.dao.UserDao;
import com.oauth.example.modal.App;
import com.oauth.example.modal.User;
import com.oauth.example.modal.UserRoles;
import com.oauth.provider.OAuthValidator;
import com.oauth.provider.SimpleOAuthValidator;
import com.oauth.provider.server.OAuthServlet;

/**
 * @author India
 *
 */
public class SampleOAuthProvider {
	public static final OAuthValidator VALIDATOR = new SimpleOAuthValidator();

	private static final Map<String, OAuthConsumer> ALL_CONSUMERS = Collections
			.synchronizedMap(new HashMap<String, OAuthConsumer>(10));

	private static final Collection<OAuthAccessor> ALL_TOKENS = new HashSet<OAuthAccessor>();

	private static AppDao appDao = new AppDao();

	private static UserDao userDao = new UserDao();

	public static synchronized void loadConsumers(ServletConfig config) throws IOException {

		List<App> apps = appDao.getAllConsumers();

		// for each entry in the properties file create a OAuthConsumer
		for (App app : apps)

		{
			String consumer_key = app.getAppId().toString();
			String consumer_secret = app.getAppSecret();

			if (consumer_secret != null) {
				String consumer_description = app.getAppDescription();
				String consumer_callback_url = app.getCallbackUrl();
				// Create OAuthConsumer w/ key and secret
				OAuthConsumer consumer = new OAuthConsumer(consumer_callback_url, consumer_key, consumer_secret, null);
				consumer.setProperty("name", app.getAppName());
				consumer.setProperty("description", consumer_description);
				ALL_CONSUMERS.put(consumer_key, consumer);
			}

		}

	}

	public static synchronized OAuthConsumer getConsumer(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException {

		OAuthConsumer consumer = null;
		// try to load from local cache if not throw exception
		String consumer_key = requestMessage.getConsumerKey();

		consumer = SampleOAuthProvider.ALL_CONSUMERS.get(consumer_key);

		if (consumer == null) {
			OAuthProblemException problem = new OAuthProblemException("token_rejected");
			throw problem;
		}

		return consumer;
	}

	/**
	 * Get the access token and token secret for the given oauth_token.
	 */
	public static synchronized OAuthAccessor getAccessor(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException {

		// try to load from local cache if not throw exception
		String consumer_token = requestMessage.getToken();
		OAuthAccessor accessor = null;
		for (OAuthAccessor a : SampleOAuthProvider.ALL_TOKENS) {
			if (a.requestToken != null) {
				if (a.requestToken.equals(consumer_token)) {
					accessor = a;
					break;
				}
			} else if (a.accessToken != null) {
				if (a.accessToken.equals(consumer_token)) {
					accessor = a;
					break;
				}
			}
		}

		if (accessor == null) {
			OAuthProblemException problem = new OAuthProblemException("token_expired");
			throw problem;
		}

		return accessor;
	}

	/**
	 * Set the access token
	 */
	public static synchronized void markAuthority(OAuthAccessor accessor, String userName, String encPassword)
			throws OAuthException {

		User user = new User();
		UserRoles userRole = null;
		Boolean isAuthorized = false;
		;
		// first remove the accessor from cache
		ALL_TOKENS.remove(accessor);

		user = isAuthorized(userName, encPassword);

		if (user != null && user.getUserName().equals(userName)) {
			isAuthorized = true;
		}
		accessor.setProperty("user", userName);
		accessor.setProperty("authorized", isAuthorized);
		accessor.setProperty("userDetails", user);
		if (isAuthorized) {
			userRole = getRoles(user);
		}
		if (userRole != null) {
			accessor.setProperty("userRole", userRole.getRoles().getName());
		}

		// update token in local cache
		ALL_TOKENS.add(accessor);
	}

	protected static synchronized User isAuthorized(String userName, String encPassword) {
		User user = null;
		user = userDao.getAuthorizedUser(userName, encPassword);
		if (user != null && user.getUserName().equals(userName)) {
			return user;
		}

		return null;
	}

	protected static synchronized UserRoles getRoles(User user) {
		UserRoles userRole = null;
		userRole = userDao.getUserRole(user);
		return userRole;
	}

	/**
	 * Generate a fresh request token and secret for a consumer.
	 * 
	 * @throws OAuthException
	 */
	public static synchronized void generateRequestToken(OAuthAccessor accessor) throws OAuthException {

		// generate oauth_token and oauth_secret
		String consumer_key = (String) accessor.consumer.getProperty("name");
		// generate token and secret based on consumer_key

		// for now use md5 of name + current time as token
		String token_data = consumer_key + System.nanoTime();
		String token = DigestUtils.md5Hex(token_data);
		// for now use md5 of name + current time + token as secret
		String secret_data = consumer_key + System.nanoTime() + token;
		String secret = DigestUtils.md5Hex(secret_data);

		accessor.requestToken = token;
		accessor.tokenSecret = secret;
		accessor.accessToken = null;

		// add to the local cache
		ALL_TOKENS.add(accessor);

	}

	/**
	 * Generate a fresh request token and secret for a consumer.
	 * 
	 * @throws OAuthException
	 */
	public static synchronized void generateAccessToken(OAuthAccessor accessor) throws OAuthException {

		// generate oauth_token and oauth_secret
		String consumer_key = (String) accessor.consumer.getProperty("name");
		// generate token and secret based on consumer_key

		// for now use md5 of name + current time as token
		String token_data = consumer_key + System.nanoTime();
		String token = DigestUtils.md5Hex(token_data);
		// first remove the accessor from cache
		ALL_TOKENS.remove(accessor);

		accessor.requestToken = null;
		accessor.accessToken = token;

		// update token in local cache
		ALL_TOKENS.add(accessor);
	}

	public static void handleException(Exception e, HttpServletRequest request, HttpServletResponse response,
			boolean sendBody) throws IOException, ServletException {
		String realm = (request.isSecure()) ? "https://" : "http://";
		realm += request.getLocalName();
		OAuthServlet.handleException(response, e, realm, sendBody);
	}

}

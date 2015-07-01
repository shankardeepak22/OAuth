/**
 * 
 */
package com.oauth.consumer.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthException;
import com.oauth.commons.OAuthMessage;
import com.oauth.commons.OAuthProblemException;
import com.oauth.commons.ParameterStyle;
import com.oauth.commons.signature.OAuthSignatureMethod;
import com.oauth.consumer.http.HttpClient;
import com.oauth.consumer.http.HttpMessage;
import com.oauth.consumer.http.HttpMessageDecoder;
import com.oauth.consumer.http.HttpResponseMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class OAuthClient {

	public OAuthClient(HttpClient http) {
		this.http = http;
		httpParameters.put(HttpClient.FOLLOW_REDIRECTS, Boolean.FALSE);
	}

	private HttpClient http;
	protected final Map<String, Object> httpParameters = new HashMap<String, Object>();

	public void setHttpClient(HttpClient http) {
		this.http = http;
	}

	public HttpClient getHttpClient() {
		return http;
	}

	/**
	 * HTTP client parameters, as a map from parameter name to value.
	 * 
	 * @see HttpClient for parameter names.
	 */
	public Map<String, Object> getHttpParameters() {
		return httpParameters;
	}

	/**
	 * Get a fresh request token from the service provider.
	 * 
	 * @param accessor
	 *            should contain a consumer that contains a non-null consumerKey
	 *            and consumerSecret. Also,
	 *            accessor.consumer.serviceProvider.requestTokenURL should be
	 *            the URL (determined by the service provider) for getting a
	 *            request token.
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public void getRequestToken(OAuthAccessor accessor) throws IOException,
			OAuthException, URISyntaxException {
		getRequestToken(accessor, null);
	}

	/**
	 * Get a fresh request token from the service provider.
	 * 
	 * @param accessor
	 *            should contain a consumer that contains a non-null consumerKey
	 *            and consumerSecret. Also,
	 *            accessor.consumer.serviceProvider.requestTokenURL should be
	 *            the URL (determined by the service provider) for getting a
	 *            request token.
	 * @param httpMethod
	 *            typically OAuthMessage.POST or OAuthMessage.GET, or null to
	 *            use the default method.
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public void getRequestToken(OAuthAccessor accessor, String httpMethod)
			throws IOException, OAuthException, URISyntaxException {
		getRequestToken(accessor, httpMethod, null);
	}

	/**
	 * Get a fresh request token from the service provider.
	 * 
	 * @param accessor
	 *            should contain a consumer that contains a non-null consumerKey
	 *            and consumerSecret. Also,
	 *            accessor.consumer.serviceProvider.requestTokenURL should be
	 *            the URL (determined by the service provider) for getting a
	 *            request token.
	 * @param httpMethod
	 *            typically OAuthMessage.POST or OAuthMessage.GET, or null to
	 *            use the default method.
	 * @param parameters
	 *            additional parameters for this request, or null to indicate
	 *            that there are no additional parameters.
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public void getRequestToken(OAuthAccessor accessor, String httpMethod,
			Collection<? extends Map.Entry> parameters) throws IOException,
			OAuthException, URISyntaxException {
		getRequestTokenResponse(accessor, httpMethod, parameters);
	}

	/**
	 * Get a fresh request token from the service provider.
	 * 
	 * @param accessor
	 *            should contain a consumer that contains a non-null consumerKey
	 *            and consumerSecret. Also,
	 *            accessor.consumer.serviceProvider.requestTokenURL should be
	 *            the URL (determined by the service provider) for getting a
	 *            request token.
	 * @param httpMethod
	 *            typically OAuthMessage.POST or OAuthMessage.GET, or null to
	 *            use the default method.
	 * @param parameters
	 *            additional parameters for this request, or null to indicate
	 *            that there are no additional parameters.
	 * @return the response from the service provider
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public OAuthMessage getRequestTokenResponse(OAuthAccessor accessor,
			String httpMethod, Collection<? extends Map.Entry> parameters)
			throws IOException, OAuthException, URISyntaxException {
		accessor.accessToken = null;
		accessor.tokenSecret = null;
		{
			// This code supports the 'Variable Accessor Secret' extension
			// described in http://oauth.pbwiki.com/AccessorSecret
			Object accessorSecret = accessor
					.getProperty(OAuthConsumer.ACCESSOR_SECRET);
			if (accessorSecret != null) {
				List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(
						1) : new ArrayList<Map.Entry>(parameters);
				p.add(new OAuth.Parameter("oauth_accessor_secret",
						accessorSecret.toString()));
				parameters = p;
				// But don't modify the caller's parameters.
			}
		}
		OAuthMessage response = invoke(accessor, httpMethod,
				accessor.consumer.serviceProvider.requestTokenURL, parameters);
		accessor.requestToken = response.getParameter(OAuth.OAUTH_TOKEN);
		accessor.tokenSecret = response.getParameter(OAuth.OAUTH_TOKEN_SECRET);
		response.requireParameters(OAuth.OAUTH_TOKEN, OAuth.OAUTH_TOKEN_SECRET);
		System.out.println("OAUTH Secret is: " + accessor.tokenSecret);
		System.out.println("OAUTH request token is: " + accessor.requestToken);
		return response;
	}

	/**
	 * Get an access token from the service provider, in exchange for an
	 * authorized request token.
	 * 
	 * @param accessor
	 *            should contain a non-null requestToken and tokenSecret, and a
	 *            consumer that contains a consumerKey and consumerSecret. Also,
	 *            accessor.consumer.serviceProvider.accessTokenURL should be the
	 *            URL (determined by the service provider) for getting an access
	 *            token.
	 * @param httpMethod
	 *            typically OAuthMessage.POST or OAuthMessage.GET, or null to
	 *            use the default method.
	 * @param parameters
	 *            additional parameters for this request, or null to indicate
	 *            that there are no additional parameters.
	 * @return the response from the service provider
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public OAuthMessage getAccessToken(OAuthAccessor accessor,
			String httpMethod, Collection<? extends Map.Entry> parameters)
			throws IOException, OAuthException, URISyntaxException {
		if (accessor.requestToken != null) {
			if (parameters == null) {
				parameters = OAuth.newList(OAuth.OAUTH_TOKEN,
						accessor.requestToken);
			} else if (!OAuth.newMap(parameters).containsKey(OAuth.OAUTH_TOKEN)) {
				List<Map.Entry> p = new ArrayList<Map.Entry>(parameters);
				p.add(new OAuth.Parameter(OAuth.OAUTH_TOKEN,
						accessor.requestToken));
				parameters = p;
			}
		}
		OAuthMessage response = invoke(accessor, httpMethod,
				accessor.consumer.serviceProvider.accessTokenURL, parameters);
		response.requireParameters(OAuth.OAUTH_TOKEN, OAuth.OAUTH_TOKEN_SECRET);
		accessor.accessToken = response.getParameter(OAuth.OAUTH_TOKEN);
		accessor.tokenSecret = response.getParameter(OAuth.OAUTH_TOKEN_SECRET);
		System.out.println("Access Token is " + response.getToken());
		System.out.println("Token Secret is: " + accessor.tokenSecret);
		return response;
	}

	/**
	 * Construct a request message, send it to the service provider and get the
	 * response.
	 * 
	 * @param httpMethod
	 *            the HTTP request method, or null to use the default method
	 * @return the response
	 * @throws URISyntaxException
	 *             the given url isn't valid syntactically
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public OAuthMessage invoke(OAuthAccessor accessor, String httpMethod,
			String url, Collection<? extends Map.Entry> parameters)
			throws IOException, OAuthException, URISyntaxException {
		OAuthMessage request = accessor.newRequestMessage(httpMethod, url,
				parameters);
		Object accepted = accessor.consumer
				.getProperty(OAuthConsumer.ACCEPT_ENCODING);
		if (accepted != null) {
			request.getHeaders().add(
					new OAuth.Parameter(HttpMessage.ACCEPT_ENCODING, accepted
							.toString()));
		}
		Object ps = accessor.consumer.getProperty(PARAMETER_STYLE);
		ParameterStyle style = (ps == null) ? ParameterStyle.BODY : Enum
				.valueOf(ParameterStyle.class, ps.toString());
		return invoke(request, style);
	}

	/**
	 * The name of the OAuthConsumer property whose value is the ParameterStyle
	 * to be used by invoke.
	 */
	public static final String PARAMETER_STYLE = "parameterStyle";

	/**
	 * The name of the OAuthConsumer property whose value is the Accept-Encoding
	 * header in HTTP requests.
	 * 
	 * @deprecated use {@link OAuthConsumer#ACCEPT_ENCODING} instead
	 */
	@Deprecated
	public static final String ACCEPT_ENCODING = OAuthConsumer.ACCEPT_ENCODING;

	/**
	 * Construct a request message, send it to the service provider and get the
	 * response.
	 * 
	 * @return the response
	 * @throws URISyntaxException
	 *             the given url isn't valid syntactically
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public OAuthMessage invoke(OAuthAccessor accessor, String url,
			Collection<? extends Map.Entry> parameters) throws IOException,
			OAuthException, URISyntaxException {
		return invoke(accessor, null, url, parameters);
	}

	/**
	 * Send a request message to the service provider and get the response.
	 * 
	 * @return the response
	 * @throws IOException
	 *             failed to communicate with the service provider
	 * @throws OAuthProblemException
	 *             the HTTP response status code was not 200 (OK)
	 */
	public OAuthMessage invoke(OAuthMessage request, ParameterStyle style)
			throws IOException, OAuthException {
		OAuthResponseMessage response = access(request, style);
		if ((response.getHttpResponse().getStatusCode() / 100) != 2) {
			OAuthProblemException problem = response.toOAuthProblemException();
			try {
				problem.setParameter(
						OAuthProblemException.SIGNATURE_BASE_STRING,
						OAuthSignatureMethod.getBaseString(request));
			} catch (Exception ignored) {
			}
			throw problem;
		}
		return response;
	}

	/**
	 * Send a request and return the response. Don't try to decide whether the
	 * response indicates success; merely return it.
	 */
	public OAuthResponseMessage access(OAuthMessage request,
			ParameterStyle style) throws IOException {
		HttpMessage httpRequest = HttpMessage.newRequest(request, style);
		HttpResponseMessage httpResponse = http.execute(httpRequest,
				httpParameters);
		httpResponse = HttpMessageDecoder.decode(httpResponse);
		return new OAuthResponseMessage(httpResponse);
	}

	protected static final String PUT = OAuthMessage.PUT;
	protected static final String POST = OAuthMessage.POST;
	protected static final String DELETE = OAuthMessage.DELETE;
	protected static final String CONTENT_LENGTH = HttpMessage.CONTENT_LENGTH;

}

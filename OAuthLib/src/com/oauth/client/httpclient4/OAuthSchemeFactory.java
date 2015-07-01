/**
 * 
 */
package com.oauth.client.httpclient4;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

/**
 * @author Deepak R Shankar
 *
 */
public class OAuthSchemeFactory implements AuthSchemeFactory {

	/** The name of this authorization scheme. */
	public static final String SCHEME_NAME = "OAuth";

	/**
	 * The name of an HttpClient param whose value is the realm to send in
	 * Authorization headers, if no realm was previously received in a
	 * WWW-Authenticate challenge. This is useful for preemptive authorization;
	 * that is sending an Authorization header without any need for a challenge.
	 */
	public static final String DEFAULT_REALM = "defaultRealm";

	public AuthScheme newInstance(final HttpParams params) {
		return new OAuthScheme((String) params.getParameter(DEFAULT_REALM));
	}

}

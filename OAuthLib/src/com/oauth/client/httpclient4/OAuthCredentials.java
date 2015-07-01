/**
 * 
 */
package com.oauth.client.httpclient4;

import org.apache.http.auth.UsernamePasswordCredentials;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;

/**
 * @author Deepak R Shankar
 *
 */
public class OAuthCredentials extends UsernamePasswordCredentials {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final OAuthAccessor accessor;

	public OAuthCredentials(OAuthAccessor accessor) {
		super(accessor.consumer.consumerKey, accessor.consumer.consumerSecret);
		this.accessor = accessor;
	}

	/**
	 * Constructs a simple accessor, containing only a consumer key and secret.
	 * This is useful for two-legged OAuth; that is interaction between a
	 * Consumer and Service Provider with no User involvement.
	 */
	public OAuthCredentials(String consumerKey, String consumerSecret) {
		this(new OAuthAccessor(new OAuthConsumer(null, consumerKey,
				consumerSecret, null)));
	}

	public OAuthAccessor getAccessor() {
		return accessor;
	}

	/** Get the current consumer secret, to be used as a password. */
	@Override
	public String getPassword() {
		return getAccessor().consumer.consumerSecret;
	}

	/** Get the current consumer key, to be used as a password. */
	@Override
	public String getUserName() {
		return getAccessor().consumer.consumerKey;
	}

}

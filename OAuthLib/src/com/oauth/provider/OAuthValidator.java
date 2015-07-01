/**
 * 
 */
package com.oauth.provider;

import java.io.IOException;
import java.net.URISyntaxException;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthException;
import com.oauth.commons.OAuthMessage;

/**
 * @author Deepak R Shankar
 *
 */
public interface OAuthValidator {

	/**
	 * Check that the given message from the given accessor is valid.
	 * 
	 * @throws OAuthException
	 *             the message doesn't conform to OAuth. The exception contains
	 *             information that conforms to the OAuth <a
	 *             href="http://wiki.oauth.net/ProblemReporting">Problem
	 *             Reporting extension</a>.
	 * @throws IOException
	 *             the message couldn't be read.
	 * @throws URISyntaxException
	 *             the message URL is invalid.
	 */
	public void validateMessage(OAuthMessage message, OAuthAccessor accessor)
			throws OAuthException, IOException, URISyntaxException;
}

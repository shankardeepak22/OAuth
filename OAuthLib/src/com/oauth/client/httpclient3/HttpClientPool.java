/**
 * 
 */
package com.oauth.client.httpclient3;

import java.net.URL;

import org.apache.commons.httpclient.HttpClient;

/**
 * A source of Jakarta Commons HttpClient objects.
 * 
 * @author Deepak R Shankar
 *
 */
public interface HttpClientPool {

	/** Get the appropriate HttpClient for sending a request to the given URL. */
	public HttpClient getHttpClient(URL server);
}

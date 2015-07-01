/**
 * 
 */
package com.oauth.client.httpclient4;

import java.net.URL;

/**
 * @author Deepak R Shankar
 *
 */
public interface HttpClientPool {
	
	/** Get the appropriate HttpClient for sending a request to the given URL. */
    public org.apache.http.client.HttpClient getHttpClient(URL server);


}

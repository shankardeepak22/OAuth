/**
 * 
 */
package com.oauth.consumer.http;

import java.io.IOException;
import java.util.Map;

import com.oauth.commons.OAuthMessage;

/**
 * @author Deepak R Shankar
 *
 */
public interface HttpClient {

	/**
     * Send an HTTP request and return the response.
     * 
     * @param httpParameters
     *            HTTP client parameters, as a map from parameter name to value.
     *            Parameter names are defined as constants below.
     */
    HttpResponseMessage execute(HttpMessage request, Map<String, Object> httpParameters) throws IOException;

    /**
     * The name of the parameter that is the maximum time to wait to connect to
     * the server. (Integer msec)
     */
    static final String CONNECT_TIMEOUT = "connectTimeout";

    /**
     * The name of the parameter that is the maximum time to wait for response
     * data. (Integer msec)
     */
    static final String READ_TIMEOUT = "readTimeout";

    /** The name of the parameter to automatically follow redirects. (Boolean) */
    static final String FOLLOW_REDIRECTS = "followRedirects";

    static final String GET = OAuthMessage.GET;
    static final String POST = OAuthMessage.POST;
    static final String PUT = OAuthMessage.PUT;
    static final String DELETE = OAuthMessage.DELETE;

}

/**
 * 
 */
package com.oauth.consumer.http;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.oauth.commons.OAuthProblemException;

/**
 * @author Deepak R Shankar
 *
 */
public abstract class HttpResponseMessage extends HttpMessage {
	
	 protected HttpResponseMessage(String method, URL url) {
	        super(method, url);
	    }

	    @Override
	    public void dump(Map<String, Object> into) throws IOException {
	        super.dump(into);
	        into.put(STATUS_CODE, Integer.valueOf(getStatusCode()));
	        String location = getHeader(LOCATION);
	        if (location != null) {
	            into.put(LOCATION, location);
	        }
	    }

	    public abstract int getStatusCode() throws IOException;

	    /** The name of a dump entry whose value is the response Location header. */
	    public static final String LOCATION = OAuthProblemException.HTTP_LOCATION;

	    /** The name of a dump entry whose value is the HTTP status code. */
	    public static final String STATUS_CODE = OAuthProblemException.HTTP_STATUS_CODE;

	    /** The statusCode that indicates a normal outcome. */
	    public static final int STATUS_OK = 200;

	    /** The standard end-of-line marker in an HTTP message. */
	    public static final String EOL = "\r\n";

}

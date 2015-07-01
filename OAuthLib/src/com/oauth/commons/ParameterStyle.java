/**
 * 
 */
package com.oauth.commons;

/**
 * @author Deepak R Shankar
 *
 */
public enum ParameterStyle {
	 /**
     * Send parameters whose names begin with "oauth_" in an HTTP header, and
     * other parameters (whose names don't begin with "oauth_") in either the
     * message body or URL query string. The header formats are specified by
     * OAuth Core under <a href="http://oauth.net/core/1.0a#auth_header">OAuth
     * HTTP Authorization Scheme</a>.
     */
    AUTHORIZATION_HEADER,

    /**
     * Send all parameters in the message body, with a Content-Type of
     * application/x-www-form-urlencoded.
     */
    BODY,

    /** Send all parameters in the query string part of the URL. */
    QUERY_STRING;

}

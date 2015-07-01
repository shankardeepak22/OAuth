/**
 * 
 */
package com.oauth.client.httpclient4;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.auth.RFC2617Scheme;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.message.BasicHeader;

import com.oauth.commons.OAuthAccessor;
import com.oauth.commons.OAuthConsumer;
import com.oauth.commons.OAuthMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class OAuthScheme extends RFC2617Scheme{
	
	private final String defaultRealm;
    /** Whether the authentication process is complete (for the current context) */
    private boolean complete;

    OAuthScheme(String defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    @Override
    public String getRealm() {
        String realm = super.getRealm();
        if (realm == null) {
            realm = defaultRealm;
        }
        return realm;
    }

    public String getSchemeName() {
        return OAuthSchemeFactory.SCHEME_NAME;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isConnectionBased() {
        return false;
    }

    /**
     * Handle a challenge from an OAuth server.
     */
    @Override
    public void processChallenge(Header challenge) throws MalformedChallengeException {
        super.processChallenge(challenge);
        complete = true;
    }

    public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        String uri;
        String method;
        HttpUriRequest uriRequest = getHttpUriRequest(request);
        if (uriRequest != null) {
            uri = uriRequest.getURI().toString();
            method = uriRequest.getMethod();
        } else {
            // Some requests don't include the server name in the URL.
            RequestLine requestLine = request.getRequestLine();
            uri = requestLine.getUri();
            method = requestLine.getMethod();
        }
        try {
            OAuthMessage message = new OAuthMessage(method, uri, null);
            OAuthAccessor accessor = getAccessor(credentials);
            message.addRequiredParameters(accessor);
            String authorization = message.getAuthorizationHeader(getRealm());
            return new BasicHeader("Authorization", authorization);
        } catch (Exception e) {
            throw new AuthenticationException(null, e);
        }
    }

    private HttpUriRequest getHttpUriRequest(HttpRequest request) {
        while (request instanceof RequestWrapper) {
            HttpRequest original = ((RequestWrapper) request).getOriginal();
            if (original == request) {
                break;
            }
            request = original;
        }
        if (request instanceof HttpUriRequest) {
            return (HttpUriRequest) request;
        }
        return null;
    }

    private OAuthAccessor getAccessor(Credentials credentials) {
        if (credentials instanceof OAuthCredentials) {
            return ((OAuthCredentials) credentials).getAccessor();
        }
        return new OAuthAccessor(new OAuthConsumer(null // callback URL
                , credentials.getUserPrincipal().getName() // consumer key
                , credentials.getPassword() // consumer secret
                , null)); // service provider
    }

}

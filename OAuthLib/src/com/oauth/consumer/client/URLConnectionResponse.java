/**
 * 
 */
package com.oauth.consumer.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.oauth.commons.OAuth;
import com.oauth.consumer.http.HttpMessage;
import com.oauth.consumer.http.HttpResponseMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class URLConnectionResponse extends HttpResponseMessage{

	 /**
     * Construct an OAuthMessage from the HTTP response, including parameters
     * from OAuth WWW-Authenticate headers and the body. The header parameters
     * come first, followed by the ones from the response body.
     */
    public URLConnectionResponse(HttpMessage request, String requestHeaders,
            byte[] requestExcerpt, URLConnection connection) throws IOException {
        super(request.method, request.url);
        this.requestHeaders = requestHeaders;
        this.requestExcerpt = requestExcerpt;
        this.requestEncoding = request.getContentCharset();
        this.connection = connection;
        this.headers.addAll(getHeaders());
    }

    private final String requestHeaders;
    private final byte[] requestExcerpt;
    private final String requestEncoding;
    private final URLConnection connection;

    @Override
    public int getStatusCode() throws IOException {
        if (connection instanceof HttpURLConnection) {
            return ((HttpURLConnection) connection).getResponseCode();
        }
        return STATUS_OK;
    }

    @Override
    public InputStream openBody() {
        try {
            return connection.getInputStream();
        } catch (IOException ohWell) {
        }
        return null;
    }

    protected String getHeaderField(URLConnection connection, int index) {
        try {
            return connection.getHeaderField(index);
        } catch (NoSuchElementException e) {
            // This violates the interface contract, but it happens.
            // http://code.google.com/p/googleappengine/issues/detail?id=1945
            return null;
        }
    }

    protected String getHeaderFieldKey(URLConnection connection, int index) {
        try {
            return connection.getHeaderFieldKey(index);
        } catch (NoSuchElementException e) {
            // This violates the interface contract, but it happens.
            // http://code.google.com/p/googleappengine/issues/detail?id=1945
            return null;
        }
    }

    private List<Map.Entry<String, String>> getHeaders() {
        List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>();
        boolean foundContentType = false;
        String value;
        for (int i = 0; (value = getHeaderField(connection, i)) != null; ++i) {
            String name = getHeaderFieldKey(connection, i);
            if (name != null) {
                headers.add(new OAuth.Parameter(name, value));
                if (CONTENT_TYPE.equalsIgnoreCase(name)) {
                    foundContentType = true;
                }
            }
        }
        if (!foundContentType) {
            headers.add(new OAuth.Parameter(CONTENT_TYPE, connection
                    .getContentType()));
        }
        return headers;
    }
    /** Return a complete description of the HTTP exchange. */
    @Override
    public void dump(Map<String, Object> into) throws IOException {
        super.dump(into);
        {
            StringBuilder request = new StringBuilder(requestHeaders);
            request.append(EOL);
            if (requestExcerpt != null) {
                request.append(new String(requestExcerpt, requestEncoding));
            }
            into.put(REQUEST, request.toString());
        }
        {
            HttpURLConnection http = (connection instanceof HttpURLConnection) ? (HttpURLConnection) connection
                    : null;
            StringBuilder response = new StringBuilder();
            String value;
            for (int i = 0; (value = getHeaderField(connection, i)) != null; ++i) {
                String name = getHeaderFieldKey(connection, i);
                if (i == 0 && name != null && http != null) {
                    String firstLine = "HTTP " + getStatusCode();
                    String message = http.getResponseMessage();
                    if (message != null) {
                        firstLine += (" " + message);
                    }
                    response.append(firstLine).append(EOL);
                }
                if (name != null) {
                    response.append(name).append(": ");
                    name = name.toLowerCase();
                }
                response.append(value).append(EOL);
            }
            response.append(EOL);
            if (body != null) {
                response.append(new String(((ExcerptInputStream) body)
                        .getExcerpt(), getContentCharset()));
            }
            into.put(HttpMessage.RESPONSE, response.toString());
        }
    }
}

/**
 * 
 */
package com.oauth.client.httpclient3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;

import com.oauth.commons.OAuth;
import com.oauth.consumer.client.ExcerptInputStream;
import com.oauth.consumer.http.HttpMessage;
import com.oauth.consumer.http.HttpResponseMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class HttpMethodResponse extends HttpResponseMessage {

	 /**
     * Construct an OAuthMessage from the HTTP response, including parameters
     * from OAuth WWW-Authenticate headers and the body. The header parameters
     * come first, followed by the ones from the response body.
     */
    public HttpMethodResponse(HttpMethod method, byte[] requestBody, String requestEncoding)
            throws IOException
    {
        super(method.getName(), new URL(method.getURI().toString()));
        this.method = method;
        this.requestBody = requestBody;
        this.requestEncoding = requestEncoding;
        this.headers.addAll(getHeaders());
    }

    private final HttpMethod method;
    private final byte[] requestBody;
    private final String requestEncoding;

    @Override
    public int getStatusCode()
    {
        return method.getStatusCode();
    }

    @Override
    public InputStream openBody() throws IOException
    {
        return method.getResponseBodyAsStream();
    }

    private List<Map.Entry<String, String>> getHeaders()
    {
        List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>();
        Header[] allHeaders = method.getResponseHeaders();
        if (allHeaders != null) {
            for (Header header : allHeaders) {
                headers.add(new OAuth.Parameter(header.getName(), header.getValue()));
            }
        }
        return headers;
    }

    /** Return a complete description of the HTTP exchange. */
    @Override
    public void dump(Map<String, Object> into) throws IOException
    {
        super.dump(into);
        {
            StringBuilder request = new StringBuilder(method.getName());
            request.append(" ").append(method.getPath());
            String query = method.getQueryString();
            if (query != null && query.length() > 0) {
                request.append("?").append(query);
            }
            request.append(EOL);
            for (Header header : method.getRequestHeaders()) {
                request.append(header.getName()).append(": ").append(header.getValue()).append(EOL);
            }
            request.append(EOL);
            if (requestBody != null) {
                request.append(new String(requestBody, requestEncoding));
            }
            into.put(REQUEST, request.toString());
        }
        {
            StringBuilder response = new StringBuilder();
            String value = method.getStatusLine().toString();
            response.append(value).append(EOL);
            for (Header header : method.getResponseHeaders()) {
                String name = header.getName();
                value = header.getValue();
                response.append(name).append(": ").append(value).append(EOL);
            }
            response.append(EOL);
            if (body != null) {
                response.append(new String(((ExcerptInputStream) body).getExcerpt(),
                        getContentCharset()));
            }
            into.put(HttpMessage.RESPONSE, response.toString());
        }
    }


}

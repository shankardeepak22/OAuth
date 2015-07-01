/**
 * 
 */
package com.oauth.provider.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oauth.commons.OAuth;
import com.oauth.commons.OAuthMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class HttpRequestMessage extends OAuthMessage {
	
	public HttpRequestMessage(HttpServletRequest request, String URL) {
        super(request.getMethod(), URL, getParameters(request));
        this.request = request;
        copyHeaders(request, getHeaders());
    }

    private final HttpServletRequest request;

    @Override
    public InputStream getBodyAsStream() throws IOException {
        return request.getInputStream();
    }

    @Override
    public String getBodyEncoding() {
        return request.getCharacterEncoding();
    }

    private static void copyHeaders(HttpServletRequest request, Collection<Map.Entry<String, String>> into) {
        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Enumeration<String> values = request.getHeaders(name);
                if (values != null) {
                    while (values.hasMoreElements()) {
                        into.add(new OAuth.Parameter(name, values.nextElement()));
                    }
                }
            }
        }
    }

    public static List<OAuth.Parameter> getParameters(HttpServletRequest request) {
        List<OAuth.Parameter> list = new ArrayList<OAuth.Parameter>();
        for (Enumeration<String> headers = request.getHeaders("Authorization"); headers != null
                && headers.hasMoreElements();) {
            String header = headers.nextElement();
            for (OAuth.Parameter parameter : OAuthMessage
                    .decodeAuthorization(header)) {
                if (!"realm".equalsIgnoreCase(parameter.getKey())) {
                    list.add(parameter);
                }
            }
        }
        for (Object e : request.getParameterMap().entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                list.add(new OAuth.Parameter(name, value));
            }
        }
        return list;
    }


}

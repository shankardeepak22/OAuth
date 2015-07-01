/**
 * 
 */
package com.oauth.client.httpclient3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.oauth.consumer.client.ExcerptInputStream;
import com.oauth.consumer.http.HttpMessage;
import com.oauth.consumer.http.HttpResponseMessage;

/**
 * @author Deepak R Shankar
 *
 */
public class HttpClient3 implements com.oauth.consumer.http.HttpClient {

	public HttpClient3() {
		this(SHARED_CLIENT);
	}

	public HttpClient3(HttpClientPool clientPool) {
		this.clientPool = clientPool;
	}

	private final HttpClientPool clientPool;

	public HttpResponseMessage execute(HttpMessage request,
			Map<String, Object> parameters) throws IOException {
		final String method = request.method;
		final String url = request.url.toExternalForm();
		final InputStream body = request.getBody();
		final boolean isDelete = DELETE.equalsIgnoreCase(method);
		final boolean isPost = POST.equalsIgnoreCase(method);
		final boolean isPut = PUT.equalsIgnoreCase(method);
		byte[] excerpt = null;
		HttpMethod httpMethod;
		if (isPost || isPut) {
			EntityEnclosingMethod entityEnclosingMethod = isPost ? new PostMethod(
					url) : new PutMethod(url);
			if (body != null) {
				ExcerptInputStream e = new ExcerptInputStream(body);
				String length = request
						.removeHeaders(HttpMessage.CONTENT_LENGTH);
				entityEnclosingMethod
						.setRequestEntity((length == null) ? new InputStreamRequestEntity(
								e) : new InputStreamRequestEntity(e, Long
								.parseLong(length)));
				excerpt = e.getExcerpt();
			}
			httpMethod = entityEnclosingMethod;
		} else if (isDelete) {
			httpMethod = new DeleteMethod(url);
		} else {
			httpMethod = new GetMethod(url);
		}
		for (Map.Entry<String, Object> p : parameters.entrySet()) {
			String name = p.getKey();
			String value = p.getValue().toString();
			if (FOLLOW_REDIRECTS.equals(name)) {
				httpMethod.setFollowRedirects(Boolean.parseBoolean(value));
			} else if (READ_TIMEOUT.equals(name)) {
				httpMethod.getParams().setIntParameter(
						HttpMethodParams.SO_TIMEOUT, Integer.parseInt(value));
			}
		}
		for (Map.Entry<String, String> header : request.headers) {
			httpMethod.addRequestHeader(header.getKey(), header.getValue());
		}
		HttpClient client = clientPool.getHttpClient(new URL(httpMethod
				.getURI().toString()));
		client.executeMethod(httpMethod);
		return new HttpMethodResponse(httpMethod, excerpt,
				request.getContentCharset());
	}

	private static final HttpClientPool SHARED_CLIENT = new SingleClient();

	/**
	 * A pool that simply shares a single HttpClient, as recommended <a
	 * href="http://hc.apache.org/httpclient-3.x/performance.html">here</a>. An
	 * HttpClient owns a pool of TCP connections. So, callers that share an
	 * HttpClient will share connections. Sharing improves performance (by
	 * avoiding the overhead of creating connections) and uses fewer resources
	 * in the client and its servers.
	 */
	private static class SingleClient implements HttpClientPool {
		SingleClient() {
			client = new HttpClient();
			client.setHttpConnectionManager(new MultiThreadedHttpConnectionManager());
		}

		private final HttpClient client;

		public HttpClient getHttpClient(URL server) {
			return client;
		}
	}

}

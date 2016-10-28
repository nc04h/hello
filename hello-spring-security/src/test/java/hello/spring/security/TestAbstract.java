package hello.spring.security;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class TestAbstract {

	protected HttpClient httpClient(String login, String password, HttpHost host) {
		Credentials credentials = new UsernamePasswordCredentials(login, password);
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(host), credentials);
		HttpClient httpClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();
		return httpClient;
	}

	protected HttpClient httpClient(HttpHost host) {
		HttpClient httpClient = HttpClientBuilder.create()
				.build();
		return httpClient;
	}

	protected static class TestRequestFactory extends HttpComponentsClientHttpRequestFactory {

		private HttpHost host;

		public TestRequestFactory(HttpClient httpClient, HttpHost host) {
			super(httpClient);
			this.host = host;
		}

		@Override
		protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
			AuthCache authCache = new BasicAuthCache();
			DigestScheme digestScheme = new DigestScheme();
			authCache.put(host, digestScheme);
			BasicHttpContext httpContext = new BasicHttpContext();
			httpContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
			return httpContext;
		}
	}
}

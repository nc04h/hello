package hello.spring.security;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class TestBasicAuth {

	private static final Logger log = Logger.getLogger(TestBasicAuth.class);

	@Test
	public void testBasicAuth() {
		String url = "http://localhost:1234/basic/auth";
		String login = "basic";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		Credentials credentials = new UsernamePasswordCredentials(login, password);
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(host), credentials);
		HttpClient httpClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = 
				new HttpComponentsClientHttpRequestFactory(httpClient) {
			@Override
			protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
				AuthCache authCache = new BasicAuthCache();
				BasicScheme basicScheme = new BasicScheme();
				authCache.put(host, basicScheme);
				BasicHttpContext httpContext = new BasicHttpContext();
				httpContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
				return httpContext;
			}
		};
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
		log.debug(response);
	}

}

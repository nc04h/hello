package hello.spring.security;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
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
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class TestDigestAuth {

	private static final Logger log = Logger.getLogger(TestDigestAuth.class);

	@Test
	public void testDigestAuth() {
		String url = "http://localhost:1234/digest/auth";
		String login = "digest";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(login, password, host);
		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
		log.debug(response);
	}

	@Test
	public void testToken() {
		String url = "http://localhost:1234/digest/token";
		String login = "token";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(login, password, host);
		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-csrf", "1");
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		String response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
		log.debug(response);
	}

	private HttpClient httpClient(String login, String password, HttpHost host) {
		Credentials credentials = new UsernamePasswordCredentials(login, password);
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(host), credentials);
		HttpClient httpClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();
		return httpClient;
	}

	static class TestRequestFactory extends HttpComponentsClientHttpRequestFactory {

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

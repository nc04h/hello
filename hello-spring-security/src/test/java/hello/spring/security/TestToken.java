package hello.spring.security;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import hello.spring.security.token.TokenConstants;

public class TestToken extends TestAbstract {

	private static final Logger log = Logger.getLogger(TestToken.class);

	@Test
	public void testTokenAuth() {
		String token = null;
		final HttpHost host = new HttpHost("localhost", 1234);
		{
			String url = "http://localhost:1234/digest/token";
			String login = "token";
			String password = "auth";
			HttpClient httpClient = httpClient(login, password, host);
			RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			token = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
			log.debug(token);
		}
		Assert.hasText(token);
		{
			String url = "http://localhost:1234/token/auth";
			HttpClient httpClient = httpClient(host);
			RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
			HttpHeaders headers = new HttpHeaders();
			headers.add(TokenConstants.CUSTOM_TOKEN_HEADER, token);
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			log.debug(response);
		}
	}

	@Test
	public void testWrongTokenAuth() {
		try {
			String url = "http://localhost:1234/token/auth";
			final HttpHost host = new HttpHost("localhost", 1234);
			HttpClient httpClient = httpClient(host);
			RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
			HttpHeaders headers = new HttpHeaders();
			headers.add(TokenConstants.CUSTOM_TOKEN_HEADER, "wrong");
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			log.debug(response);
			Assert.isTrue(false);
		} catch (RestClientException e) {
			log.error(e);
		}
	}

}

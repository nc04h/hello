package hello.spring.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import hello.spring.security.token.TokenConstants;

public class TestToken extends TestAbstract {

	private static final Logger log = Logger.getLogger(TestToken.class);

	@Test
	public void testTokenAuth() {
		String url = "http://localhost:1234/token/auth";
		String login = "token";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(host);
		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		HttpHeaders headers = new HttpHeaders();
		headers.add(TokenConstants.TOKEN_HEADER, "ULZg4QdfWxJtjNhUyoa/eTxFS5UnbB1B4WiqR/lki6A");
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		
		String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
		log.debug(response);
	}
}

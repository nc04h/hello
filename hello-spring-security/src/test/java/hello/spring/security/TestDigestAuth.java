package hello.spring.security;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class TestDigestAuth extends TestAbstract {

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
		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		String response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
		log.debug(response);
	}

}

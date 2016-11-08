package hello.spring.security;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class TestOAuth extends TestAbstract {

	private static final Logger log = Logger.getLogger(TestOAuth.class);

	@Test
	public void testOAuth() {
		String url = "http://localhost:1234/oauth/auth";
		String login = "basic";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(login, password, host);
		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
		log.debug(response);
	}

}

package hello.spring.security;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TestTwitter extends TestAbstract {
	
	private static final Logger log = Logger.getLogger(TestTwitter.class);

	@Test
	public void testAuth() {
		String url = "http://localhost:1234/signin/twitter";
		String login = "alopwpmp";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(login, password, host);
		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		
		ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, null, Object.class);
		Object response = responseEntity.getBody();
		log.debug(response);
		HttpHeaders headers = responseEntity.getHeaders();
		log.debug(headers);
		log.debug(responseEntity.getStatusCode());
		List<String> locations = headers.get("Location");
		log.debug(locations);
		locations.stream().findFirst().ifPresent(location -> {
			log.debug(location);
			ResponseEntity<String> responseEntity2 = restTemplate.getForEntity(location, String.class);
			log.debug(responseEntity2);
		});
		
		
	}
}

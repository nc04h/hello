package hello.spring.security;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TestTwitter extends TestAbstract {

	private static final Logger log = Logger.getLogger(TestTwitter.class);

	@Test
	public void test() throws Exception {
		ProxyConfig proxyConfig = new ProxyConfig();
		proxyConfig.addHostsToProxyBypass("localhost");
		proxyConfig.setProxyHost("");
		proxyConfig.setProxyPort(8080);
		WebClient webClient = new WebClient(BrowserVersion.getDefault());
		webClient.getOptions().setProxyConfig(proxyConfig);
		WebRequest request = new WebRequest(new URL("http://localhost:1234/signin/twitter"), 
				com.gargoylesoftware.htmlunit.HttpMethod.POST);
		HtmlPage page = webClient.getPage(request);
		log.debug(page);
		WebResponse response = page.getWebResponse();
		log.debug(response.getContentAsString());

		log.debug(page.getElementById("username_or_email"));
		log.debug(page.getElementById("password"));
		log.debug(page.getElementById("allow"));

		HtmlTextInput username = (HtmlTextInput) page.getElementById("username_or_email");
		username.setValueAttribute("");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		password.setValueAttribute("");
		HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("allow");
		submitButton.click();

		page.getForms().stream().filter(f -> f.getId().equals("oauth_form")).forEach(f -> {
		});

		webClient.close();
	}


	public void testAuth() {
		/*		
		String url = "http://localhost:1234/signin/twitter?oauth_token=123";
		String login = "";
		String password = "auth";
		final HttpHost host = new HttpHost("localhost", 1234);
		HttpClient httpClient = httpClient(login, password, host);

		RestTemplate restTemplate = new RestTemplate(new TestRequestFactory(httpClient, host));
		ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
		Object response = responseEntity.getBody();
		log.debug(response);
		HttpHeaders headers = responseEntity.getHeaders();
		log.debug(headers);
		log.debug(responseEntity.getStatusCode());
		 */		

		String url = "http://localhost:1234/signin/twitter";
		String login = "";
		String password = "";
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
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			Proxy proxy= new Proxy(Type.HTTP, new InetSocketAddress("", 8080));
			requestFactory.setProxy(proxy);
			RestTemplate restTemplate2 = new RestTemplate(requestFactory);
			ResponseEntity<String> responseEntity2 = restTemplate2.getForEntity(location, String.class);
			log.debug(responseEntity2.getHeaders());
			log.debug(responseEntity2);
			Document doc = Jsoup.parse(responseEntity2.getBody());
			Element oauthForm = doc.getElementById("oauth_form");
			Elements elements = oauthForm.getAllElements();
			Map<String, String> params = new HashMap<>();
			elements.stream().filter(e -> e.tagName().equals("input")).forEach(e -> {
				log.debug(e);
				log.debug("name=" + e.attr("name") + " " + e.attr("value"));
				switch(e.attr("name")) {
				case "authenticity_token": 
				case "oauth_token":
				case "remember_me":
				case "redirect_after_login":
					params.put(e.attr("name"), e.attr("value"));
					break;
				case "session[username_or_email]":
					params.put(e.attr("name"), login);
					break;
				case "session[password]":
					params.put(e.attr("name"), password);
					break;
				default:
					break;
				}
			});
			log.debug(params);
			ResponseEntity<String> responseEntity3 = restTemplate2.exchange(
					location, HttpMethod.POST, null, String.class, params);
			log.debug(responseEntity3.getHeaders());
			log.debug(responseEntity3);
			List<String> locations3 = responseEntity3.getHeaders().get("location");
			log.debug(locations3);
		});

	}
}

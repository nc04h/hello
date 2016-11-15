package hello.spring.security;

import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.Test;

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
	private boolean useProxy = false;

	@Test
	public void test() throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.getDefault());
		if (useProxy) {
			ProxyConfig proxyConfig = new ProxyConfig();
			proxyConfig.addHostsToProxyBypass("localhost");
			proxyConfig.setProxyHost(System.getProperty("http.proxyHost"));
			proxyConfig.setProxyPort(Integer.valueOf(System.getProperty("http.proxyPort")));
			webClient.getOptions().setProxyConfig(proxyConfig);
		}
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
		username.setValueAttribute(System.getProperty("twitter.username"));
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		password.setValueAttribute(System.getProperty("twitter.password"));
		HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("allow");
		submitButton.click();

		page.getForms().stream().filter(f -> f.getId().equals("oauth_form")).forEach(f -> {
		});

		webClient.close();
	}
}

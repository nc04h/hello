package hello.spring.security.oauth;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.security.oauth.provider.OAuthAuthenticationHandler;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.stereotype.Component;

@Component
public class MyOAuthAuthenticationHandler implements OAuthAuthenticationHandler {
	
	private static final Logger log = Logger.getLogger(MyOAuthAuthenticationHandler.class);

	@Override
	public Authentication createAuthentication(HttpServletRequest request, ConsumerAuthentication authentication,
			OAuthAccessProviderToken authToken) {
		// TODO
		log.debug("createAuthentication");
		return null;
	}

}

package hello.spring.security.oauth;

import org.apache.log4j.Logger;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.RandomValueProviderTokenServices;
import org.springframework.stereotype.Component;

@Component
public class MyOAuthProviderTokenServices extends RandomValueProviderTokenServices {

	private static final Logger log = Logger.getLogger(MyOAuthProviderTokenServices.class);

	@Override
	protected OAuthProviderTokenImpl readToken(String token) {
		log.debug("readToken");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void storeToken(String tokenValue, OAuthProviderTokenImpl token) {
		// TODO Auto-generated method stub
		log.debug("storeToken");

	}

	@Override
	protected OAuthProviderTokenImpl removeToken(String tokenValue) {
		log.debug("removeToken");
		// TODO Auto-generated method stub
		return null;
	}

}

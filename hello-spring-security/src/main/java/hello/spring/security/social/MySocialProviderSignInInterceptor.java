package hello.spring.security.social;

import org.apache.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class MySocialProviderSignInInterceptor implements ProviderSignInInterceptor<Twitter> {
	
	private static final Logger log = Logger.getLogger(MySocialProviderSignInInterceptor.class);

	@Override
	public void preSignIn(ConnectionFactory<Twitter> connectionFactory, MultiValueMap<String, String> parameters,
			WebRequest request) {
		// TODO Auto-generated method stub
		log.debug("preSignIn " + parameters);
	}

	@Override
	public void postSignIn(Connection<Twitter> connection, WebRequest request) {
		// TODO Auto-generated method stub
		log.debug("postSignIn " + request);
	}


}

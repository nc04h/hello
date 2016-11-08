package hello.spring.security.oauth;

import org.apache.log4j.Logger;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MyOAuthDetailsService implements ConsumerDetailsService  {

	private static final Logger log = Logger.getLogger(MyOAuthDetailsService.class);

	@Override
	public ConsumerDetails loadConsumerByConsumerKey(String consumerKey) throws OAuthException {
		log.debug("loadConsumerByConsumerKey consumerKey: " + consumerKey);
		BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
		// TODO 
		return consumerDetails;
	}

}

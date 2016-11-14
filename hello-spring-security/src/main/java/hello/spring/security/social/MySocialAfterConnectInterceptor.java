package hello.spring.security.social;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

@Component
public class MySocialAfterConnectInterceptor implements ConnectInterceptor<Twitter> {
	
	private static final Logger log = Logger.getLogger(MySocialAfterConnectInterceptor.class);

	private static final String POST_TWEET_PARAMETER = "postTweet";
	private static final String POST_TWEET_ATTRIBUTE = "twitterConnect." + POST_TWEET_PARAMETER;
	
	@Override
	public void preConnect(ConnectionFactory<Twitter> connectionFactory, MultiValueMap<String, String> parameters,
			WebRequest request) {
		log.debug("preConnect");
		if (StringUtils.isNotEmpty(request.getParameter(POST_TWEET_PARAMETER))) {
			request.setAttribute(POST_TWEET_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	@Override
	public void postConnect(Connection<Twitter> connection, WebRequest request) {
		log.debug("postConnect");
		if (request.getAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				connection.updateStatus("I've connected with the Spring Social Showcase!");
			} catch (DuplicateStatusException e) {
			}
			request.removeAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

}

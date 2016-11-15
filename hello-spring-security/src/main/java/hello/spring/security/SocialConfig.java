package hello.spring.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import hello.spring.security.social.MySocialSignInAdapter;

@Configuration
@EnableSocial
public class SocialConfig extends AbstractConfig implements SocialConfigurer {

	private static final Logger log = Logger.getLogger(SocialConfig.class);

	@Value("${twitter.consumerKey}")
	private String consumerKey;
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(
				decrypt(consumerKey), decrypt(consumerSecret)));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
		repository.setConnectionSignUp(new ConnectionSignUp() {

			@Override
			public String execute(Connection<?> connection) {
				log.debug("ConnectionSignUp.execute " + connection.getKey().getProviderUserId());
				return connection.getKey().getProviderUserId();
			}

		});
		return repository;
	}

	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, 
			ConnectionRepository connectionRepository) {
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}

	@Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, 
			UsersConnectionRepository connectionRepository) {
		log.debug("providerSignInController " + connectionFactoryLocator + " " + connectionRepository);
		ProviderSignInController signInController = new ProviderSignInController(
				connectionFactoryLocator, connectionRepository, signInAdapter());
		signInController.setPostSignInUrl("/social/twitter/auth");
		return signInController;
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new MySocialSignInAdapter(new HttpSessionRequestCache());
	}
}

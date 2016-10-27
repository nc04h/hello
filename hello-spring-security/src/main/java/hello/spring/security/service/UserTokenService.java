package hello.spring.security.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;

import org.apache.log4j.Logger;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import hello.spring.security.data.User;
import hello.spring.security.data.UserToken;
import hello.spring.security.repo.UserTokenRepository;

@Service
@Transactional
public class UserTokenService {

	public static final long DEFAULT_EXPIRATION = 1000 * 60 * 60 * 24;

	private static final Logger log = Logger.getLogger(UserTokenService.class);

	@Autowired
	private UserTokenRepository userTokenRepository;
	@Autowired
	private UserService userService;

	public UserTokenRepository getUserTokenRepository() {
		return userTokenRepository;
	}

	public void invalidateToken(String login) {
		log.debug("invalidate token for login: " + login);
		UserToken userToken = userTokenRepository.findByLogin(login);
		Assert.notNull(userToken, "token not found");
		userToken.setEnabled(false);
		userToken = userTokenRepository.save(userToken);
	}

	public boolean validateToken(String login) {
		UserToken userToken = userTokenRepository.findByLogin(login);
		Assert.notNull(userToken, "token not found");
		return userToken.isEnabled() && userToken.getExpiration().isAfter(Instant.now());
	}

	public UserToken updateToken(String login) {
		UserToken userToken = null;
		User user = userService.getUserRepository().findByLogin(login);
		Assert.notNull(user, "user not found");
		if (login != null) {
			try {
				userToken = userTokenRepository.findByLogin(login);
				if (userToken == null) {
					userToken = new UserToken();
					userToken.setLogin(login);
				}
				userToken.setEnabled(true);
				userToken.setExpiration(Instant.now().plusMillis(DEFAULT_EXPIRATION));
				byte[] values = new byte[32];
				SecureRandom.getInstanceStrong().nextBytes(values);
				userToken.setToken(Base64.encodeBytes(values));
				userToken = userTokenRepository.save(userToken);
			} catch (NoSuchAlgorithmException e) {
				log.error(e);
			}
		}
		return userToken;
	}
}

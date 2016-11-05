package hello.spring.security.service;

import java.time.Instant;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import hello.spring.security.data.User;
import hello.spring.security.data.UserToken;
import hello.spring.security.repo.UserTokenRepository;
import hello.spring.security.token.TokenConstants;

@Service
@Transactional
public class UserTokenService {

	private static final Logger log = Logger.getLogger(UserTokenService.class);

	@Autowired
	private UserTokenRepository userTokenRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EncryptionService encryptionService;

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

	public UserToken validateToken(String token) {
		Assert.hasText(token, "token is empty");
		String[] tokenData = encryptionService.decryptString(token).split(":");
		Arrays.stream(tokenData).forEach(t -> log.debug("tokenData=" + t));
		UserToken userToken = userTokenRepository.findByLogin(tokenData[0]);
		Assert.notNull(userToken, "token not found");
		User user = userService.findByLogin(tokenData[0]);
		Assert.notNull(user, "user not found");
		Assert.isTrue(userToken.isEnabled() 
				&& user.getMd5Password().equals(tokenData[1])
				&& Instant.ofEpochMilli(Long.parseLong(tokenData[2])).equals(userToken.getExpiration())
				&& userToken.getExpiration().isAfter(Instant.now()));
		return userToken;
	}

	public UserToken updateToken(String login, String password) {
		UserToken userToken = null;
		User user = userService.getUserRepository().findByLogin(login);
		Assert.notNull(user, "user not found");
		if (login != null) {
			userToken = userTokenRepository.findByLogin(login);
			if (userToken == null) {
				userToken = new UserToken();
				userToken.setLogin(login);
			}
			userToken.setEnabled(true);
			userToken.setExpiration(Instant.now().plusMillis(TokenConstants.DEFAULT_EXPIRATION));
			String str = String.format("%s:%s:%s", login, password, userToken.getExpiration().toEpochMilli());
			userToken.setToken(encryptionService.encryptString(str));
			userToken = userTokenRepository.save(userToken);
		}
		return userToken;
	}
}

package hello.spring.security.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hello.spring.security.token.TokenConstants;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {

	@Value("${pass}")
	private String pass;

	@Autowired
	private UserService userService;

	public String getToken(String username, String password) {
		Assert.hasText(username, "username is empty");
		Assert.hasText(password, "password is empty");
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put("username", username);
		tokenData.put("create_date", Instant.now().toString());
		Instant expiration = Instant.now().plusMillis(TokenConstants.DEFAULT_EXPIRATION);
		tokenData.put("expire_date", expiration.toString());
		JwtBuilder jwtBuilder = Jwts.builder()
				.setExpiration(Date.from(expiration))
				.setClaims(tokenData);
		return jwtBuilder.signWith(SignatureAlgorithm.HS512, pass).compact();
	}
}

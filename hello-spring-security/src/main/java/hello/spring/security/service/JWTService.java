package hello.spring.security.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hello.spring.security.data.User;
import hello.spring.security.token.TokenConstants;
import hello.spring.security.token.jwt.MyJWTAuthentication;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

@Service
public class JWTService {

	private static final Logger log = Logger.getLogger(JWTService.class);

	private static final String USERNAME_PARAM = "username";
	private static final String CREATE_DATE_PARAM = "create_date";
	private static final String EXPIRE_DATE_PARAM = "expire_date";

	@Value("${pass}")
	private String pass;

	@Autowired
	private UserService userService;

	public String getToken(String username, String password) {
		log.debug("getToken");
		Assert.hasText(username, "username is empty");
		Assert.hasText(password, "password is empty");
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put(USERNAME_PARAM, username);
		tokenData.put(CREATE_DATE_PARAM, Instant.now().toString());
		Instant expiration = Instant.now().plusMillis(TokenConstants.DEFAULT_EXPIRATION);
		tokenData.put(EXPIRE_DATE_PARAM, expiration.toString());
		JwtBuilder jwtBuilder = Jwts.builder()
				.setExpiration(Date.from(expiration))
				.setClaims(tokenData);
		return jwtBuilder.signWith(SignatureAlgorithm.HS512, pass).compact();
	}

	public MyJWTAuthentication validateToken(String token) {
		log.debug("validateToken");
		if (token == null) {
			throw new AuthenticationServiceException("Token is empty");
		}
		DefaultClaims claims;
		try {
			claims = (DefaultClaims) Jwts.parser().setSigningKey(pass).parse(token).getBody();
		} catch (Exception ex) {
			throw new AuthenticationServiceException("Token corrupted");
		}
		String expireDateString = claims.get(EXPIRE_DATE_PARAM, String.class);
		if (expireDateString == null) {
			throw new AuthenticationServiceException("Invalid token");
		}
		Instant expireDate = Instant.parse(expireDateString);
		if (expireDate.isAfter(Instant.now())) {
			String username = claims.get(USERNAME_PARAM, String.class);
			User user = userService.findByLogin(username);
			if (user == null) {
				throw new AuthenticationServiceException("user not found");
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			user.getRoles().stream().filter(role -> role != null && role.getPermissions() != null).forEach(role -> {
				role.getPermissions().stream().filter(perm -> perm != null).forEach(perm -> {
					authorities.add(new SimpleGrantedAuthority(perm.getName()));
				});
			});
			return new MyJWTAuthentication(token, authorities, user);
		} else { 
			throw new AuthenticationServiceException("Token expired");
		}

	}
}

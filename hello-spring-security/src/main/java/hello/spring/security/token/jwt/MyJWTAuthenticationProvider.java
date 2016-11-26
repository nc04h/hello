package hello.spring.security.token.jwt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import hello.spring.security.service.JWTService;

@Component
public class MyJWTAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = Logger.getLogger(MyJWTAuthenticationProvider.class);

	@Autowired
	private JWTService jwtService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("authenticate");
		if (!(authentication instanceof MyJWTAuthentication)) {
			throw new InsufficientAuthenticationException("Invalid auth");
		}
		MyJWTAuthentication jwtAuthentication = (MyJWTAuthentication) authentication;
		return validateAuthentication(jwtAuthentication);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(MyJWTAuthentication.class);
	}

	private MyJWTAuthentication validateAuthentication(MyJWTAuthentication authentication) {
		log.debug("validateAuthentication");
		String token = authentication.getToken();
		MyJWTAuthentication result = jwtService.validateToken(token);
		result.setAuthenticated(true);
		return result;
	}
}

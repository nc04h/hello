package hello.spring.security.token.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MyJWTAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
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
		String token = authentication.getToken();
		// TODO
		return null;
	}
}

package hello.spring.security.token;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MyTokenAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = Logger.getLogger(MyTokenAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		log.debug("---> authenticate " + this);
		log.debug(authentication);
		log.debug(authentication.getPrincipal());
		log.debug(authentication.getCredentials());
		log.debug(authentication.isAuthenticated());
		if (!(authentication instanceof MyTokenAuthentication)) {
			throw new BadCredentialsException("Invalid credentials");
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}

package hello.spring.security.digest;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class MyDigestAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = Logger.getLogger(MyDigestAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("---> authenticate " + this);
		log.debug(authentication);
		log.debug(authentication.getPrincipal());
		log.debug(authentication.getCredentials());
		if (!(authentication.getPrincipal() instanceof User)) {
			throw new BadCredentialsException("Invalid principal");
		}
		User user = (User) authentication.getPrincipal();
		log.debug("password=" + user.getPassword());
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword(), user.getAuthorities());
		log.debug("auth=" + auth);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("---> supports " + authentication);
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}

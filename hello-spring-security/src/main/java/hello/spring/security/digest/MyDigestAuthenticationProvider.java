package hello.spring.security.digest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class MyDigestAuthenticationProvider  implements AuthenticationProvider {
	
	private static final Logger log = Logger.getLogger(MyDigestAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			log.debug("---> authenticate " + this);
			log.debug(authentication);
			String user = String.valueOf(authentication.getPrincipal());
			String password = String.valueOf(authentication.getCredentials());
			log.debug("user=" + user + " password=" + password + " " + (password == null));
			if (user.equals("digest") && password.equals("auth")) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_DIGEST_USER"));
				Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authorities);
				log.debug(authentication.isAuthenticated());
				log.debug(auth);
				return auth;
			} else {
				System.out.println("Bad credentials");
				throw new BadCredentialsException("Bad credentials");
			}
		} finally {
			log.debug("<--- authenticate " + this);			
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}

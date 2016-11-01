package hello.spring.security.token;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import hello.spring.security.data.User;
import hello.spring.security.data.UserToken;
import hello.spring.security.service.UserService;
import hello.spring.security.service.UserTokenService;

@Component
public class MyTokenAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = Logger.getLogger(MyTokenAuthenticationProvider.class);

	@Autowired
	private UserTokenService userTokenService;
	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			log.debug("---> authenticate " + this);
			log.debug(authentication);
			log.debug(authentication.getPrincipal());
			log.debug(authentication.getCredentials());
			log.debug(authentication.isAuthenticated());
			if (!(authentication instanceof MyTokenAuthentication) || authentication.getCredentials() == null) {
				throw new BadCredentialsException("Invalid credentials");
			}
			MyTokenAuthentication auth = (MyTokenAuthentication) authentication;
			UserToken userToken = userTokenService.validateToken(auth.getCredentials().toString());
			User user = userService.findByLogin(userToken.getLogin());
			List<GrantedAuthority> authorities = new ArrayList<>();
			user.getRoles().stream().filter(role -> role != null && role.getPermissions() != null).forEach(role -> {
				role.getPermissions().stream().filter(perm -> perm != null).forEach(perm -> {
					authorities.add(new SimpleGrantedAuthority(perm.getName()));
				});
			});
			MyTokenAuthentication result = new MyTokenAuthentication(auth.getCredentials().toString(), authorities);
			result.setAuthenticated(true);
			return result;
		} catch (Exception e) {
			throw new BadCredentialsException("authentication error", e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(MyTokenAuthentication.class);
	}

}

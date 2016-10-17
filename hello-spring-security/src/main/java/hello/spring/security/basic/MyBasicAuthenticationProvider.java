package hello.spring.security.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import hello.spring.security.data.User;
import hello.spring.security.service.EncryptionService;
import hello.spring.security.service.UserService;

@Component
public class MyBasicAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = Logger.getLogger(MyBasicAuthenticationProvider.class);

	@Autowired
	private UserService userService;
	@Autowired
	private EncryptionService passwordService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			log.debug("---> authenticate " + this);
			log.debug(authentication);
			String login = String.valueOf(authentication.getPrincipal());
			String password = String.valueOf(authentication.getCredentials());
			log.debug("user=" + login + " password=" + password + " " + (password == null));
			User user = userService.getUserRepository().findByLogin(login);
			if (user != null && StringUtils.equals(user.getLogin(), login) 
					&& passwordService.validatePassword(user, password)) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				user.getRoles().stream().filter(role -> role != null).forEach(role -> {
					role.getPermissions().stream().filter(perm -> perm != null && role.getPermissions() != null)
					.forEach(perm -> {
						authorities.add(new SimpleGrantedAuthority(perm.getName()));
					});
				});
				log.debug("authorities=" + authorities);
				Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authorities);
				log.debug("isAuthenticated=" + authentication.isAuthenticated());
				log.debug(auth);
				return auth;
			} else {
				log.error("Bad credentials: login=" + login + ", password=" + password);
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

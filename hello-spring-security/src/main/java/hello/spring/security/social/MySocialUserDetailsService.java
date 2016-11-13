package hello.spring.security.social;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import hello.spring.security.service.UserService;

//@Service
public class MySocialUserDetailsService implements SocialUserDetailsService, UserDetailsService {

	private static final Logger log = Logger.getLogger(MySocialUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			log.debug("---> loadUserByUsername " + username);
			if (StringUtils.isEmpty(username)) {
				throw new UsernameNotFoundException("user is not found");
			}
			hello.spring.security.data.User user = userService.findByLogin(username);
			if (user == null) {
				throw new UsernameNotFoundException("user is not found");
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			user.getRoles().stream().filter(role -> role != null && role.getPermissions() != null).forEach(role -> {
				role.getPermissions().stream().filter(perm -> perm != null).forEach(perm -> {
					authorities.add(new SimpleGrantedAuthority(perm.getName()));
				});
			});
			log.debug("authorities=" + authorities);
			UserDetails userDetails = new MySocialUserDetails(user.getLogin(), user.getMd5Password(), authorities);
			log.debug(userDetails);
			return userDetails;
		} finally {
			log.debug("<--- loadUserByUsername");
		}
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		UserDetails userDetails = loadUserByUsername(userId);
		if (userDetails == null) {
			throw new UsernameNotFoundException("username not found");
		}
		return (MySocialUserDetails) userDetails;
	}

}

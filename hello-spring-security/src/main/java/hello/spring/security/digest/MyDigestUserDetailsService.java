package hello.spring.security.digest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hello.spring.security.service.UserService;

@Service
public class MyDigestUserDetailsService implements UserDetailsService {

	private static Logger log = Logger.getLogger(MyDigestUserDetailsService.class);

	@Autowired
	private UserService userService;

	@PostConstruct
	public void postConstruct() {
		log.debug("postConstruct");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			log.debug("---> loadUserByUsername " + username);
			if (StringUtils.isEmpty(username)) {
				throw new UsernameNotFoundException("user is not found");
			}
			hello.spring.security.data.User user = userService.getUserRepository().findByLogin(username);
			if (user == null) {
				throw new UsernameNotFoundException("user is not found");
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			user.getRoles().stream().filter(role -> role != null && role.getPermissions() != null).forEach(role -> {
				role.getPermissions().stream().filter(perm -> perm != null).forEach(perm -> {
					authorities.add(new SimpleGrantedAuthority(perm.getName()));
				});
			});
			UserDetails userDetails = new User(user.getLogin(), user.getPassword(), authorities);
			log.debug(userDetails);
			return userDetails;
		} finally {
			log.debug("<--- loadUserByUsername");
		}
	}

}

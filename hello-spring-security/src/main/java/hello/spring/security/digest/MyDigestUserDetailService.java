package hello.spring.security.digest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyDigestUserDetailService implements UserDetailsService {

	private static Logger log = Logger.getLogger(MyDigestUserDetailService.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("postConstruct");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			log.debug("---> loadUserByUsername " + username);
			if (username != null && username.equals("digest")) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_DIGEST_USER"));
				UserDetails userDetails = new User(username, "auth", authorities);
				log.debug(userDetails);
				return userDetails;
			} else {
				throw new UsernameNotFoundException("user is not found");
			}
		} finally {
			log.debug("<--- loadUserByUsername");
		}
	}

}

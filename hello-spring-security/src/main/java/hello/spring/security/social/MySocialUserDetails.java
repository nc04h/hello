package hello.spring.security.social;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUser;

public class MySocialUserDetails extends SocialUser {

	private static final long serialVersionUID = 2900346748217617582L;

	public MySocialUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

}

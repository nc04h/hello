package hello.spring.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MyTokenAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 3248055124947286479L;

	private String token;

	public MyTokenAuthentication(String token) {
		super(null);
		this.token = token;
	}

	public MyTokenAuthentication(String token, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

}

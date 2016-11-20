package hello.spring.security.token.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyJWTAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -3906794970175149117L;

	private String token;
	private UserDetails principal;

	public MyJWTAuthentication(String token) {
		this(token, null, null);
	}

	public MyJWTAuthentication(String token, Collection<? extends GrantedAuthority> authorities, 
			UserDetails principal) {
		super(authorities);
		this.token = token;
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getToken() {
		return token;
	}
}

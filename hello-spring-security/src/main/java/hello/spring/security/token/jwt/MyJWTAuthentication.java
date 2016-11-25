package hello.spring.security.token.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MyJWTAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -3906794970175149117L;

	private String token;
	private Object principal;

	public MyJWTAuthentication(String token) {
		this(token, null, null);
	}

	public MyJWTAuthentication(String token, Collection<? extends GrantedAuthority> authorities, 
			Object principal) {
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

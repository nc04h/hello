package hello.spring.security.token.jwt;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import hello.spring.security.token.AbstractTokenAuthenticationFilter;
import hello.spring.security.token.TokenConstants;

public class MyJWTAuthenticationFilter extends AbstractTokenAuthenticationFilter {

	private static final Logger log = Logger.getLogger(MyJWTAuthenticationFilter.class);

	@Override
	protected String getToken(HttpServletRequest httpRequest) {
		String token = httpRequest.getHeader(TokenConstants.JWT_HEADER);
		log.debug("getToken: token=" + token);
		return token;
	}

	@Override
	protected AbstractAuthenticationToken getAuthentication(String token) {
		return new MyJWTAuthentication(token);
	}
}

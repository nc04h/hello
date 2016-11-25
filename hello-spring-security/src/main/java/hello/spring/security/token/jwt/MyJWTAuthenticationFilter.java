package hello.spring.security.token.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import hello.spring.security.token.TokenConstants;

public class MyJWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = Logger.getLogger(MyJWTAuthenticationFilter.class);

	public MyJWTAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.debug("attemptAuthentication request=" + request);
		String token = request.getHeader(TokenConstants.JWT_HEADER);
		log.debug("token=" + token);
		if (token == null) {
			throw new AuthenticationServiceException("Invalid token");
		}
		MyJWTAuthentication tokenAuthentication = new MyJWTAuthentication(token);
		Authentication authentication = getAuthenticationManager().authenticate(tokenAuthentication);
		return authentication;
	}

}

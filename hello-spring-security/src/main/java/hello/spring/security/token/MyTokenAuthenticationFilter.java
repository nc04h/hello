package hello.spring.security.token;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class MyTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = Logger.getLogger(MyTokenAuthenticationFilter.class);

	public MyTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String token = request.getHeader(TokenConstants.TOKEN_HEADER);
		log.debug("token=" + token);
		log.debug("request=" + request.getRequestURI());

		return getAuthenticationManager().authenticate(new MyTokenAuthentication(token));
	}

}

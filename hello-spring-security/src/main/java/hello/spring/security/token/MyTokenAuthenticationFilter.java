package hello.spring.security.token;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class MyTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = Logger.getLogger(MyTokenAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	public static final String TOKEN_HEADER = "x-my-token";

	public MyTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		String token = request.getHeader(TOKEN_HEADER);
		log.debug("token=" + token);
		return authenticationManager.authenticate(new MyTokenAuthentication(token));
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}

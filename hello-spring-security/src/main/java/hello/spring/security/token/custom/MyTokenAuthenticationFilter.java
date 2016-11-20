package hello.spring.security.token.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import hello.spring.security.token.TokenConstants;

public class MyTokenAuthenticationFilter extends GenericFilterBean {

	private static final Logger log = Logger.getLogger(MyTokenAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		log.debug("doFilter");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String token = request.getHeader(TokenConstants.CUSTOM_TOKEN_HEADER);
		log.debug("token=" + token);
		log.debug("request=" + request.getRequestURI());

		if (token != null && isAuthenticationRequired()) {
			MyTokenAuthentication authToken = new MyTokenAuthentication(token);
			Authentication authResult = getAuthenticationManager().authenticate(authToken);
			log.debug("authResult=" + authResult);

			SecurityContextHolder.getContext().setAuthentication(authResult);
		}
		chain.doFilter(request, response);
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	private boolean isAuthenticationRequired() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("isAuthenticationRequired: " + auth);
		if (auth == null || !auth.isAuthenticated()) {
			return true;
		}
		log.debug("isAuthenticated: " + auth.isAuthenticated());
		return false;
	}
}

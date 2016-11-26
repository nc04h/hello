package hello.spring.security.token;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public abstract class AbstractTokenAuthenticationFilter extends GenericFilterBean {

	private static final Logger log = Logger.getLogger(AbstractTokenAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("doFilter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String token = getToken(httpRequest);
		log.debug("token=" + token);
		log.debug("request=" + httpRequest.getRequestURI());

		if (token != null && isAuthenticationRequired()) {
			AbstractAuthenticationToken authToken = getAuthentication(token);
			Authentication authResult = getAuthenticationManager().authenticate(authToken);
			log.debug("authResult=" + authResult);

			SecurityContextHolder.getContext().setAuthentication(authResult);
		}
		chain.doFilter(request, response);
	}

	protected abstract String getToken(HttpServletRequest httpRequest);

	protected abstract AbstractAuthenticationToken getAuthentication(String token);

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

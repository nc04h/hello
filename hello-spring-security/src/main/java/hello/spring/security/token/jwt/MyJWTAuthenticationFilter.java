package hello.spring.security.token.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import hello.spring.security.token.TokenConstants;

public class MyJWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final Logger log = Logger.getLogger(MyJWTAuthenticationFilter.class);

	public MyJWTAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				log.debug("onAuthenticationSuccess");
				SecurityContextHolder.getContext().setAuthentication(authentication);
				request.getRequestDispatcher(request.getServletPath() + request.getPathInfo());
			}
		});
		setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				log.debug("onAuthenticationFailure");
				response.getOutputStream().print(exception.getMessage());
				log.error(exception);
			}
		});
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String token = request.getHeader(TokenConstants.JWT_HEADER);
		if (token == null) {
			MyJWTAuthentication authentication = new MyJWTAuthentication(null, null, null);
			authentication.setAuthenticated(false);
			return authentication;
		}
		MyJWTAuthentication tokenAuthentication = new MyJWTAuthentication(token);
		Authentication authentication = getAuthenticationManager().authenticate(tokenAuthentication);
		return authentication;
	}
}

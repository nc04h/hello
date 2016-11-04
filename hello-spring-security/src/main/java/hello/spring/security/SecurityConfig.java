package hello.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import hello.spring.security.basic.MyBasicAuthenticationProvider;
import hello.spring.security.digest.MyDigestAuthenticationProvider;
import hello.spring.security.digest.MyDigestUserDetailsService;
import hello.spring.security.token.MyTokenAuthenticationFilter;
import hello.spring.security.token.MyTokenAuthenticationProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private static final Logger log = Logger.getLogger(SecurityConfig.class);

	@Configuration
	@Order(1)
	public static class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

		public static final String REALM_NAME = "Hello Basic Auth";

		@Autowired
		private MyBasicAuthenticationProvider basicAuthProvider;

		public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName(REALM_NAME);
			return entryPoint;
		}

		@Bean
		public BasicAuthenticationFilter basicAuthenticationFilter() throws Exception {
			BasicAuthenticationFilter filter = new BasicAuthenticationFilter(
					authenticationManager(), basicAuthenticationEntryPoint());
			return filter;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/basic/**")
			.addFilter(basicAuthenticationFilter())
			.exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint())
			;
		}

		@Bean(name = "basicAuthenticationManager")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(basicAuthProvider);
		}
	}

	@Configuration
	@Order(2)
	public static class DigestAuthenticationConfig extends WebSecurityConfigurerAdapter {

		public static final String REALM_NAME = "Hello Digest Auth";

		@Autowired
		private MyDigestAuthenticationProvider digestAuthProvider;
		@Autowired
		private MyDigestUserDetailsService userDetailsService;

		@Bean
		public DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
			DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
			filter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint());
			filter.setUserDetailsService(userDetailsService);
			filter.setPasswordAlreadyEncoded(true);
			return filter;
		}

		public DigestAuthenticationEntryPoint digestAuthenticationEntryPoint() {
			DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
			entryPoint.setRealmName(REALM_NAME);
			entryPoint.setNonceValiditySeconds(300);
			entryPoint.setKey("acegi");
			return entryPoint;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/digest/**")
			.addFilter(digestAuthenticationFilter())
			.exceptionHandling().authenticationEntryPoint(digestAuthenticationEntryPoint())
			.and().csrf().disable()
			;
		}

		@Bean(name = "digestAuthenticationManager")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(digestAuthProvider);
		}

	}

	@Configuration
	@Order(3)
	public static class TokenAuthenticationConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private MyTokenAuthenticationProvider tokenAuthProvider;

		@Bean
		public MyTokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
			MyTokenAuthenticationFilter filter = new MyTokenAuthenticationFilter();
			filter.setAuthenticationManager(authenticationManager());
			return filter;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/token/**")
			.addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authenticationProvider(tokenAuthProvider)
			.exceptionHandling().authenticationEntryPoint(tokenAuthenticationEntryPoint())
			;
		}

		private AuthenticationEntryPoint tokenAuthenticationEntryPoint() {
			return new AuthenticationEntryPoint() {
				@Override
				public void commence(HttpServletRequest request, HttpServletResponse response,
						AuthenticationException authException) throws IOException, ServletException {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
				}
			};
		}

		@Bean(name = "tokenAuthenticationManager")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(tokenAuthProvider);
		}
	}

}

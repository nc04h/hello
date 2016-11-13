package hello.spring.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import hello.spring.security.basic.MyBasicAuthenticationProvider;
import hello.spring.security.digest.MyDigestAuthenticationProvider;
import hello.spring.security.digest.MyDigestUserDetailsService;
import hello.spring.security.social.MySocialUserDetailsService;
import hello.spring.security.token.MyTokenAuthenticationFilter;
import hello.spring.security.token.MyTokenAuthenticationProvider;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private static final Logger log = Logger.getLogger(SecurityConfig.class);

	@Autowired
	private MyBasicAuthenticationProvider basicAuthProvider;
	@Autowired
	private MyDigestAuthenticationProvider digestAuthProvider;
	@Autowired
	private MyTokenAuthenticationProvider tokenAuthProvider;
	
	@Bean
    public AuthenticationManager authenticationManager() {
		List<AuthenticationProvider> providers = new ArrayList<>();
		providers.add(basicAuthProvider);
		providers.add(digestAuthProvider);
		return new ProviderManager(providers);
    }

	
	@Configuration
	@Order(1)
	public class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

		public static final String REALM_NAME = "Hello Basic Auth";


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
			.addFilterAfter(basicAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint())
			;
		}

//		@Bean(name = "basicAuthenticationManager")
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(basicAuthProvider);
		}
	}

	@Configuration
	@Order(2)
	public class DigestAuthenticationConfig extends WebSecurityConfigurerAdapter {

		public static final String REALM_NAME = "Hello Digest Auth";

//		@Autowired
//		private MyDigestUserDetailsService userDetailsService;

		
		@Bean
		public DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
			DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
			filter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint());
			filter.setUserDetailsService(userDetailsService());
			filter.setPasswordAlreadyEncoded(true);
			filter.setCreateAuthenticatedToken(false);
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
			.exceptionHandling().authenticationEntryPoint(digestAuthenticationEntryPoint())
			.and()
			.addFilter(digestAuthenticationFilter())
//			.addFilterAfter(digestAuthenticationFilter(), BasicAuthenticationFilter.class)
			.authenticationProvider(digestAuthProvider)
			.antMatcher("/digest/**")
				.csrf().disable()
				.authorizeRequests()
				.anyRequest()
				.authenticated()
			;
		}
		
		@Bean
		@Override
		public MyDigestUserDetailsService userDetailsService() {
			return new MyDigestUserDetailsService();
		}

//		@Bean(name = "digestAuthenticationManager")
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			List<AuthenticationProvider> providers = new ArrayList<>();
//			providers.add(digestAuthProvider);
//			return new ProviderManager(providers);
//		}
	}

	@Configuration
	@Order(3)
	public class TokenAuthenticationConfig extends WebSecurityConfigurerAdapter {

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
			.addFilterAfter(tokenAuthenticationFilter(), DigestAuthenticationFilter.class)
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

//		@Bean(name = "tokenAuthenticationManager")
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(tokenAuthProvider);
		}
	}

	@Configuration
	@Order(4)
	public class SocialConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/twitter/**")
			.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.apply(new SpringSocialConfigurer())
			;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(socialUserDetailsService());
		}

		@Bean
		public MySocialUserDetailsService socialUserDetailsService() {
			return new MySocialUserDetailsService();
		}

	}

}

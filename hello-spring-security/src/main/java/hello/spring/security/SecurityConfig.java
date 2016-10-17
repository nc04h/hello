package hello.spring.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import hello.spring.security.basic.MyBasicAuthenticationProvider;
import hello.spring.security.digest.MyDigestUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private static final Logger log = Logger.getLogger(SecurityConfig.class);

	@Configuration
	@Order(2)
	public static class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private MyBasicAuthenticationProvider basicAuth;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(basicAuth);
		}

		public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName("Hello Basic Auth");
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
			.authenticationProvider(basicAuth)
			.addFilter(basicAuthenticationFilter());
		}		
	}

	@Configuration
	@Order(1)
	public static class DigestAuthenticationConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private MyDigestUserDetailsService userDetailsService;
		@Autowired
		private MyBasicAuthenticationProvider basicAuth;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(basicAuth);
			auth.userDetailsService(userDetailsService);
		}

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
			entryPoint.setRealmName("Hello Digest Auth");
			entryPoint.setNonceValiditySeconds(300);
			entryPoint.setKey("acegi");
			return entryPoint;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/digest/**")
			.addFilter(digestAuthenticationFilter())
			.exceptionHandling().authenticationEntryPoint(digestAuthenticationEntryPoint());
		}
		
	}
}

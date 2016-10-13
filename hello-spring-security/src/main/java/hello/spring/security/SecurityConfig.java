package hello.spring.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import hello.spring.security.basic.MyBasicAuthenticationProvider;
import hello.spring.security.digest.MyDigestAuthenticationProvider;
import hello.spring.security.digest.MyDigestUserDetailService;

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
//			.exceptionHandling()
//			.authenticationEntryPoint(basicAuthenticationEntryPoint())
//			.and()
			.addFilter(basicAuthenticationFilter())


			//.antMatcher("/basic/**")
			//			.addFilter(basicAuthenticationFilter())
			//			.addFilterAfter(basicAuthenticationFilter(), DigestAuthenticationFilter.class)
			//			.addFilterAfter(new MyFilter(), DigestAuthenticationFilter.class)
			//			.authenticationProvider(basicAuth)
			//			.httpBasic()
			//			.and()
			//.authorizeRequests().anyRequest().authenticated()
			;
		}		
	}

	@Configuration
	@Order(1)
	public static class DigestAuthenticationConfig extends WebSecurityConfigurerAdapter {

		//		@Autowired 
		//		private MyDigestUserDetailService digestUserDetailService;

		@Autowired
		private MyDigestAuthenticationProvider digestAuth;

		@Bean
		public DigestAuthenticationFilter digestAuthenticationFilter() {
			DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
			filter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint());
			filter.setUserDetailsService(new MyDigestUserDetailService());
			return filter;
		}

		@Bean
		public MyFilter myFilter() {
			MyFilter filter = new MyFilter();
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
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(digestAuth);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/digest/**")
//			.exceptionHandling()
//			.authenticationEntryPoint(digestAuthenticationEntryPoint())
//			.and()
			//.addFilter(new BasicAuthenticationFilter(authenticationManager()))
			//.addFilterAfter(digestAuthenticationFilter(), BasicAuthenticationFilter.class)
			.addFilter(digestAuthenticationFilter())
			//			.addFilterBefore(digestAuthenticationFilter(), BasicAuthenticationFilter.class)
			//			.addFilterBefore(myFilter(), DigestAuthenticationFilter.class)
			.authenticationProvider(digestAuth)

			//			.httpBasic()
			//			.and()
			//			.authorizeRequests().anyRequest().authenticated()
			;
		}		
	}

	public static class MyFilter extends GenericFilterBean {

		@Override
		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
				throws IOException, ServletException {

			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			log.debug(request);
			log.debug(request.getHeader("Authorization"));
			log.debug(response);
			log.debug(chain);


			chain.doFilter(request, response);
		}

	}
}

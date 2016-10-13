package hello.spring.security.basic;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Controller
@RequestMapping("/basic")
public class MyBasicAuthController {
	
	private static final Logger log = Logger.getLogger(MyBasicAuthController.class);

	@PreAuthorize("hasRole('ROLE_BASIC_USER')")
	@RequestMapping("/auth")
	public String auth(Authentication authentication, Principal principal) {
		try {
			log.debug("---> auth");
			log.debug("authentication=" + authentication);
			log.debug("principal=" + principal);
			log.debug(((UsernamePasswordAuthenticationToken) principal).getPrincipal());
			for (GrantedAuthority auth: authentication.getAuthorities()) {
				log.debug(auth.getAuthority());
			}
			return "index";
		} finally {
			log.debug("<--- auth");
		}

	}

}

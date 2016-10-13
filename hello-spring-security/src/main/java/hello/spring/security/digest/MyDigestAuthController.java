package hello.spring.security.digest;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/digest")
public class MyDigestAuthController {
	
	private static final Logger log = Logger.getLogger(MyDigestAuthController.class);

	@PreAuthorize("hasRole('ROLE_DIGEST_USER')")
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

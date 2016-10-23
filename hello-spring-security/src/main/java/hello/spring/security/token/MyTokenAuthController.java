package hello.spring.security.token;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token")
public class MyTokenAuthController {

	private static final Logger log = Logger.getLogger(MyTokenAuthController.class);

	@PreAuthorize("hasRole('ROLE_TOKEN_USER')")
	@RequestMapping("/auth")
	public @ResponseBody String auth(Authentication authentication, Principal principal) {
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

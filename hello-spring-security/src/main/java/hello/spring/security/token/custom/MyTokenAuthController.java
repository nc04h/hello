package hello.spring.security.token.custom;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token")
public class MyTokenAuthController {

	private static final Logger log = Logger.getLogger(MyTokenAuthController.class);

	@PreAuthorize("hasRole('ROLE_TOKEN_USER')")
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public @ResponseBody String auth(Principal principal) {
		try {
			log.debug("---> auth");
			log.debug("principal=" + principal);
			log.debug(SecurityContextHolder.getContext().getAuthentication());
			return "token index";
		} finally {
			log.debug("<--- auth");
		}

	}
}

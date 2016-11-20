package hello.spring.security.token.jwt;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jwt")
public class MyJWTAuthController {

	private static final Logger log = Logger.getLogger(MyJWTAuthController.class);

	@RequestMapping("/auth")
	public @ResponseBody String auth(Principal principal) {
		try {
			log.debug("---> auth");
			log.debug("principal=" + principal);
			log.debug(SecurityContextHolder.getContext().getAuthentication());
			return "index";
		} finally {
			log.debug("<--- auth");
		}

	}


}

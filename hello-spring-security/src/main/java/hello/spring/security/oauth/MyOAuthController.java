package hello.spring.security.oauth;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth")
public class MyOAuthController {

	private static final Logger log = Logger.getLogger(MyOAuthController.class);

	@PreAuthorize("hasRole('ROLE_OAUTH_USER')")
	@RequestMapping(path = "/auth", method = RequestMethod.GET)
	public @ResponseBody String auth() {
		// TODO
		log.debug("auth");
		return "index";
	}
}

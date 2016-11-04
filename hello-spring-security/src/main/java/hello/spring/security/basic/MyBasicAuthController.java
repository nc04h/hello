package hello.spring.security.basic;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/basic")
public class MyBasicAuthController {

	private static final Logger log = Logger.getLogger(MyBasicAuthController.class);

	@PreAuthorize("hasRole('ROLE_BASIC_USER')")
	@RequestMapping("/auth")
	public @ResponseBody String auth() {
		try {
			log.debug("---> auth");
			return "index";
		} finally {
			log.debug("<--- auth");
		}

	}

}

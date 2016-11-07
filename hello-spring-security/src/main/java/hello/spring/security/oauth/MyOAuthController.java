package hello.spring.security.oauth;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/oauth")
public class MyOAuthController {

	private static final Logger log = Logger.getLogger(MyOAuthController.class);

	@RequestMapping(path = "/auth", method = RequestMethod.GET)
	public String auth() {
		// TODO
		return "index";
	}
}

package hello.spring.security.social;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
public class MySocialController {

	private static final Logger log = Logger.getLogger(MySocialController.class);

	@RequestMapping(path = "/signin", method = RequestMethod.GET)
	public @ResponseBody String signin() {
		try {
			log.debug("---> signin");
			return "index";
		} finally {
			log.debug("<--- signin");
		}
	}
	
	@RequestMapping(path = "/twitter/auth", method = RequestMethod.GET)
	public @ResponseBody String auth() {
		try {
			log.debug("---> auth");
			return "index";
		} finally {
			log.debug("<--- auth");
		}
	}

}

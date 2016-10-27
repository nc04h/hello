package hello.spring.security.digest;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.spring.security.data.User;
import hello.spring.security.service.UserService;

@RestController
@RequestMapping("/digest")
public class MyDigestAuthController {

	private static final Logger log = Logger.getLogger(MyDigestAuthController.class);
	
	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_DIGEST_USER')")
	@RequestMapping(path = "/auth", method = RequestMethod.GET)
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

	@PreAuthorize("hasRole('ROLE_TOKEN_USER')")
	@RequestMapping(path = "/token", method = RequestMethod.PUT)
	public @ResponseBody String token(Authentication authentication, Principal principal) {
		// TODO
		log.debug("---> token");
		log.debug("authentication=" + authentication);
		log.debug("principal=" + principal);
		log.debug(((UsernamePasswordAuthenticationToken) principal).getPrincipal());
		User user = userService.findByLogin(principal.getName());
		Assert.notNull(user, "user not found");
		user = userService.updateToken(user);
		return user.getToken();
	}

}

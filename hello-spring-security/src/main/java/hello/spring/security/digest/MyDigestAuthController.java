package hello.spring.security.digest;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.spring.security.data.User;
import hello.spring.security.data.UserToken;
import hello.spring.security.service.JWTService;
import hello.spring.security.service.UserService;
import hello.spring.security.service.UserTokenService;

@RestController
@RequestMapping("/digest")
public class MyDigestAuthController {

	private static final Logger log = Logger.getLogger(MyDigestAuthController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserTokenService userTokenService;
	@Autowired
	private JWTService jwtService;

	@PreAuthorize("hasRole('ROLE_DIGEST_USER')")
	@RequestMapping(path = "/auth", method = RequestMethod.GET)
	public @ResponseBody String auth() {
		try {
			log.debug("---> auth");
			return "index";
		} finally {
			log.debug("<--- auth");
		}
	}

	@PreAuthorize("hasRole('ROLE_TOKEN_USER')")
	@RequestMapping(path = "/token", method = RequestMethod.PUT)
	public @ResponseBody String token(Principal principal) {
		log.debug("---> token");
		log.debug("principal=" + principal);
		log.debug(((UsernamePasswordAuthenticationToken) principal).getPrincipal());
		log.debug(((UsernamePasswordAuthenticationToken) principal).getCredentials());
		User user = userService.findByLogin(principal.getName());
		Assert.notNull(user, "user not found");
		UserToken token = userTokenService.updateToken(user.getLogin(), 
				user.getMd5Password());
		Assert.notNull(token, "token not found");
		return token.getToken();
	}

	@PreAuthorize("hasRole('ROLE_TOKEN_USER')")
	@RequestMapping(path = "/jwt", method = RequestMethod.PUT)
	public @ResponseBody String jwt(Principal principal) {
		log.debug("---> jwt");
		log.debug("principal=" + principal);
		log.debug(((UsernamePasswordAuthenticationToken) principal).getPrincipal());
		log.debug(((UsernamePasswordAuthenticationToken) principal).getCredentials());
		User user = userService.findByLogin(principal.getName());
		String token = jwtService.getToken(user.getLogin(), user.getMd5Password());
		log.debug("token=" + token);
		return token;
	}
}

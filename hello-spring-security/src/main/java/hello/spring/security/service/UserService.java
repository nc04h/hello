package hello.spring.security.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.spring.security.data.User;
import hello.spring.security.repo.RoleRepository;
import hello.spring.security.repo.UserRepository;

@Service
@Transactional
public class UserService {

	private static final Logger log = Logger.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}
	public User findByLogin(String login) {
		log.debug("findByLogin " + login);
		User user = userRepository.findByLogin(login);
		log.debug(user);
		return populateUser(user);
	}

	public User populateUser(User user) {
		if (user != null) {
			roleRepository.findAllByUserId(user.getId()).forEach(role -> {
				user.getRoles().add(role);
			});
		}
		log.debug(user);
		return user;
	}

}

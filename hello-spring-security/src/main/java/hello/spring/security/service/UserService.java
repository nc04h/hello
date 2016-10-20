package hello.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.spring.security.data.User;
import hello.spring.security.repo.RoleRepository;
import hello.spring.security.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	public UserRepository getUserRepository() {
		return userRepository;
	}
	
	public User populateUser(User user) {
		roleRepository.findAllByUserId(user.getId()).forEach(role -> {
			user.getRoles().add(role);
		});
		return user;
	}

}

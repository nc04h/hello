package hello.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.spring.security.repo.RoleRepository;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}
}

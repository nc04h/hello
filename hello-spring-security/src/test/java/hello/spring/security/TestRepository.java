package hello.spring.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import hello.spring.security.data.Role;
import hello.spring.security.data.User;
import hello.spring.security.service.EncryptionService;
import hello.spring.security.service.UserService;

@DirtiesContext 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@TestPropertySource(locations="classpath:application.properties")
@WebAppConfiguration
public class TestRepository {

	@Autowired
	private EntityManagerFactory emf;

	private EntityManager em;

	@Autowired
	private UserService userService;

	@Autowired
	private EncryptionService passwordService;

	@Before
	public void setUp() throws Exception {
		em = emf.createEntityManager();
	}

	@Test
	public void testCreateRole() {
		// TODO
	}
	
	@Test
	public void testCreatePermission() {
		// TODO
	}
	
	@Test
	public void testCreateUser() {
		String login = "basic";
		String password = "auth";
		User user = new User();
		user.setLogin(login);
		user.setPassword(passwordService.enryptPassword(password));
		user.setMd5Password(passwordService.md5Hex(password));
		Set<Role> roles = new HashSet<>();
		user.setRoles(roles);
		user = userService.getUserRepository().save(user);
		
	}

}

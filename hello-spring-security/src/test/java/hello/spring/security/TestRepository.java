package hello.spring.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import hello.spring.security.data.Permission;
import hello.spring.security.data.Role;
import hello.spring.security.data.User;
import hello.spring.security.service.EncryptionService;
import hello.spring.security.service.PermissionService;
import hello.spring.security.service.RoleService;
import hello.spring.security.service.UserService;

@DirtiesContext 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@TestPropertySource(locations="classpath:application.properties")
@WebAppConfiguration
public class TestRepository {

	private static final Logger log = Logger.getLogger(TestRepository.class);

	@Autowired
	private EntityManagerFactory emf;

	private EntityManager em;

	@Autowired
	private UserService userService;
	@Autowired
	private EncryptionService passwordService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;

	@Before
	public void setUp() throws Exception {
		em = emf.createEntityManager();
	}

	@Test
	public void testCreateRole() {
		createRole("Basic", "ROLE_BASIC_USER");
		createRole("Digest", "ROLE_DIGEST_USER");
		createRole("Token", "ROLE_TOKEN_USER");
	}

	private Role createRole(String roleName, String permissionName) {
		Role role = roleService.getRoleRepository().findByName(roleName);
		if (role != null) {
			log.info("the role already exists");
			return role;
		}
		Permission perm = permissionService.getPermissionRepository().findByName(permissionName);
		if (perm == null) {
			perm = new Permission();
			perm.setName(permissionName);
			perm = permissionService.getPermissionRepository().save(perm);
		}
		role = new Role();
		role.setName(roleName);
		role.addPermission(perm);
		role = roleService.getRoleRepository().save(role);
		log.info(roleName + " created");
		return role;
	}

	@Test
	public void testCreateUser() {
		createUser("basic", "auth", "Basic");
		createUser("digest", "auth", "Digest");
		createUser("token", "auth", "Token");
	}

	private User createUser(String login, String password, String roleName) {
		User user = userService.getUserRepository().findByLogin(login);
		if (user != null) {
			log.info("user already exists");
			return user;
		}
		Role role = roleService.getRoleRepository().findByName(roleName);
		Assert.notNull(role, "role not found");
		user = new User();
		user.setLogin(login);
		user.setPassword(passwordService.enryptPassword(password));
		user.setMd5Password(passwordService.md5Hex(login + ":" + SecurityConfig.DigestAuthenticationConfig.REALM_NAME 
				+ ":" + password));
		role.setUser(user);
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		user = userService.getUserRepository().save(user);
		return user;
	}

}

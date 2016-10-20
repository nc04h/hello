package hello.spring.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByName(String name);

	@Query("SELECT r FROM user_roles ur, roles r WHERE ur.role_id = r.id")
	public List<Role> findAllByUserId(long userId);

}

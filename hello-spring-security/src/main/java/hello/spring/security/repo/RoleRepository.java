package hello.spring.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByName(String name);

	@Query(value = "SELECT * FROM user_roles ur, roles r WHERE ur.id_role = r.id AND ur.id_user = :userId", nativeQuery = true)
	public List<Role> findAllByUserId(@Param("userId") long userId);

}

package hello.spring.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}

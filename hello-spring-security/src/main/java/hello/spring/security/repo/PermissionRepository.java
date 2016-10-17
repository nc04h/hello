package hello.spring.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

}

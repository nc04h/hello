package hello.spring.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByLogin(String login);
}

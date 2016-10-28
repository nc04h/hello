package hello.spring.security.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.spring.security.data.UserToken;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

	public UserToken findByLogin(String login);

	public UserToken findByToken(String token);
}

package hello.spring.security.service;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hello.spring.security.data.User;

@Service
public class EncryptionService {
	
	private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	public boolean validatePassword(User user, String password) {
		Assert.notNull(user, "user is null");
		Assert.hasText(password, "password is empty");
		return passwordEncryptor.checkPassword(password, user.getPassword());
	}
	
	public String enryptPassword(String password) {
		return passwordEncryptor.encryptPassword(password);
	}
}

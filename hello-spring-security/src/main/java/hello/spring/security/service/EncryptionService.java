package hello.spring.security.service;

import javax.annotation.PostConstruct;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import hello.spring.security.data.User;

@Service
public class EncryptionService {

	private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	private StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();

	@Value("${pass}")
	private String pass;

	@PostConstruct
	public void postConstruct() {
		stringEncryptor.setPassword(pass);
	}

	public boolean validatePassword(User user, String password) {
		Assert.notNull(user, "user is null");
		Assert.hasText(password, "password is empty");
		return passwordEncryptor.checkPassword(password, user.getPassword());
	}

	public String enryptPassword(String password) {
		return passwordEncryptor.encryptPassword(password);
	}

	public String md5Hex(String password) {
		Assert.hasText(password, "password is empty");
		return DigestUtils.md5DigestAsHex(password.getBytes());
	}

	public String encryptString(String message) {
		return stringEncryptor.encrypt(message);
	}

	public String decryptString(String message) {
		return stringEncryptor.decrypt(message);
	}
}

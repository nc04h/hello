package hello.spring.security;

import javax.annotation.PostConstruct;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractConfig {

//	@Value("${pass}")
	private String pass = "Qqq!2345";

	private StandardPBEStringEncryptor encryptor;

	@PostConstruct
	void init() {
		encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(pass);
	}

	public String decrypt(String encryptedMessage) {
		return encryptor.decrypt(encryptedMessage);
	}
}

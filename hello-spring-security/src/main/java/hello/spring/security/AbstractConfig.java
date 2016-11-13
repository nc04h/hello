package hello.spring.security;

import javax.annotation.PostConstruct;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;

public class AbstractConfig {

	@Value("${pass}")
	private String pass;

	private StandardPBEStringEncryptor encryptor;

	@PostConstruct
	public void init() {
		encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(pass);
	}

	public String decrypt(String encryptedMessage) {
		return encryptor.decrypt(encryptedMessage);
	}

}

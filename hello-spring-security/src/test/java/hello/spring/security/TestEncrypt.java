package hello.spring.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

public class TestEncrypt {

	@Test
	public void encrypt() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("Qqq!2345");
		String result = encryptor.encrypt("jdbc:postgresql://ec2-54-243-54-21.compute-1.amazonaws.com:5432/d14mn5pjf48f3u?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
		System.out.println(result);
	}
	
}

package hello.spring.security.token;

public class TokenConstants {

	public static final String CUSTOM_TOKEN_HEADER = "x-my-token";
	public static final String JWT_HEADER = "x-my-jwt";
	public static final long DEFAULT_EXPIRATION = 1000 * 60 * 60 * 24;
}

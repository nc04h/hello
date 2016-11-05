package hello.spring.security.data;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "USER_TOKENS")
public class UserToken implements Serializable {

	private static final long serialVersionUID = -205772628753829619L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "LOGIN", nullable = false, unique = true)
	private String login;

	@Transient
	private String token;

	@Column(name = "EXPIRATION", nullable = false, unique = false)
	private Instant expiration;

	@Column(name = "ENABLED", nullable = false, unique = false)
	private boolean enabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public String getToken() {
		return token;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserToken [login=").append(login).append(", token=").append(token).append(", expiration=")
		        .append(expiration).append(", enabled=").append(enabled).append("]");
		return builder.toString();
	}

}

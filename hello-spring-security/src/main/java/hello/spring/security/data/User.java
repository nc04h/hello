package hello.spring.security.data;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 5734171702407212127L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "LOGIN", nullable = false, unique = true)
	private String login;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "MD5_PASSWORD", nullable = false)
	private String md5Password;

	public String getMd5Password() {
		return md5Password;
	}

	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<Role> roles;

	public long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}

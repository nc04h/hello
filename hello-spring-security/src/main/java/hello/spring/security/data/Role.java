package hello.spring.security.data;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

	private static final long serialVersionUID = 7221745307292071690L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private Set<Permission> permissions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "ID_ROLE"), inverseJoinColumns = @JoinColumn(name = "ID_USER"))
	private User user;

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

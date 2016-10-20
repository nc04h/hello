package hello.spring.security.data;

import java.io.Serializable;
import java.util.HashSet;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

	private static final long serialVersionUID = 7221745307292071690L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	@JoinTable(name = "ROLE_PERMISSIONS", 
		joinColumns = @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID"), 
		inverseJoinColumns = @JoinColumn(name = "ID_PERMISSION", referencedColumnName = "ID"))
	private Set<Permission> permissions = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLES", 
		joinColumns = @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID"), 
		inverseJoinColumns = @JoinColumn(name = "ID_USER", referencedColumnName = "ID"))
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

	public void addPermission(Permission permission) {
		getPermissions().add(permission);
		permission.setRole(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (id != other.id)
			return false;
		return true;
	}
}

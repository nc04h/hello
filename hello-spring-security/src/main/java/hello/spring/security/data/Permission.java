package hello.spring.security.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PERMISSIONS")
public class Permission implements Serializable {

	private static final long serialVersionUID = -802679466630268251L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "ROLE_PERMISSIONS", joinColumns = @JoinColumn(name = "ID_PERMISSION"), inverseJoinColumns = @JoinColumn(name = "ID_ROLE"))
	private Role role;



}

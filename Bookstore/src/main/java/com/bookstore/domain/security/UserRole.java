package com.bookstore.domain.security;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookstore.domain.Users;

// The @Entity annotation specifies that the class Role is mapped to a table in the database
// Here the table name is mentioned is mentioned as user_role, since the class name and the
// database table name is not some.
@Entity
@Table(name="user_role")
public class UserRole {
	// The @Id annotation specifies the primary key of the entity, which is the userRoleId here
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userRoleId;

	// The @ManyToOne annotation is used to map the source entity UserRole with the target Users entity.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the column user_id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private Users users;

	// The @ManyToOne annotation is used to map the source entity UserRole with the target Role entity.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the column role_id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="role_id")
	private Role role;

	// Default Constructor
	public UserRole(){}

	// Constructor with a Users object and a Role object
	public UserRole(Users users, Role role) {
		this.users = users; // Set Operations
		this.role = role;
	}

	// Get operation for userRoleId in the UserRole entity
	public Long getUserRoleId() {
		return userRoleId;
	}

	// Set operation for userRoleId in the UserRole entity
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	// Get operation for users in the UserRole entity
	public Users getUsers() {
		return users;
	}

	// Set operation for users in the UserRole entity
	public void setUsers(Users users) {
		this.users = users;
	}

	// Get operation for role in the UserRole entity
	public Role getRole() {
		return role;
	}

	// set operation for role in the UserRole entity
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}

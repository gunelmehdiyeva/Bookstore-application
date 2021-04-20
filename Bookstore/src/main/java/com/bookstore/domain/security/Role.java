package com.bookstore.domain.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

// The @Entity annotation specifies that the class Role is mapped to a table in the database
@Entity
public class Role {
	// The @Id annotation specifies the primary key of the entity, which is the roleId here
	@Id
	private int roleId;
	private String name;

	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created betwee Role and userRoles entity.
	@OneToMany(mappedBy = "role", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<UserRole> userRoles = new HashSet<>();

	public int getRoleId() {
		return roleId;
	}// Get operation for roleId in the Role entity

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}// Set operation for roleId in the Role entity

	public String getName() {
		return name;
	}// Get operation for name in the Role entity

	public void setName(String name) {
		this.name = name;
	} // Get operation for name in the Role entity

	public Set<UserRole> getUserRoles() {
		return userRoles;
	} // Get operation for userRoles in the Role entity

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	
}

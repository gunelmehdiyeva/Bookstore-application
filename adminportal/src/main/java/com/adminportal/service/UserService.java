package com.adminportal.service;

import java.util.Set;

import com.adminportal.domain.Users;
import com.adminportal.domain.security.UserRole;



public interface UserService {
	Users createUser(Users users, Set<UserRole> userRoles) throws Exception;
	
	Users save(Users users);
}

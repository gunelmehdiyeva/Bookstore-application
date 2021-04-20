package com.adminportal.service.impl;

import java.util.Set;

import com.adminportal.domain.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.domain.security.UserRole;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Users createUser(Users users, Set<UserRole> userRoles) {
		Users localUsers = userRepository.findByUsername(users.getUsername());

		if (localUsers != null) {
			LOG.info("users {} already exists. Nothing will be done.", users.getUsername());
		} else {
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}

			users.getUserRoles().addAll(userRoles);

			localUsers = userRepository.save(users);
		}

		return localUsers;
	}

	@Override
	public Users save(Users users) {
		return userRepository.save(users);
	}

}

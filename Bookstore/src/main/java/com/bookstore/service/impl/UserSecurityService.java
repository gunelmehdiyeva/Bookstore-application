package com.bookstore.service.impl;

import com.bookstore.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstore.repository.UserRepository;
// Service implementaion
@Service
public class UserSecurityService implements UserDetailsService{
	// Injecting dependency on UserRepository object
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = userRepository.findByUsername(username);// Find user by username
		// If user not found
		if(null == users) {
			throw new UsernameNotFoundException("Username not found");
		}
		
		return users;
	}

}

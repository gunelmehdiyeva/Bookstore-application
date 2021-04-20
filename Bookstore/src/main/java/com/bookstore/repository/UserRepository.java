package com.bookstore.repository;

import com.bookstore.domain.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {
	Users findByUsername(String username);
	
	Users findByEmail(String email);
}

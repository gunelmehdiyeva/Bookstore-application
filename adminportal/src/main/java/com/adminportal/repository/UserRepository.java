package com.adminportal.repository;

import com.adminportal.domain.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {
	Users findByUsername(String username);
}

package com.adminportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.adminportal.service.UserService;


@SpringBootApplication
public class AdminportalApplication{
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AdminportalApplication.class, args);
	}
	

	/*public void run(String... args) throws Exception {
		Users user1 = new Users();
		user1.setUsername("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user1.setEmail("admin@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(0);
		role1.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}*/
}

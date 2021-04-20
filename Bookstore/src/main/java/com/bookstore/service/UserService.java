package com.bookstore.service;

import java.util.Set;

import com.bookstore.domain.Users;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final Users users, final String token);
	
	Users findByUsername(String username);
	
	Users findByEmail (String email);
	
	Users findById(Long id);
	
	Users createUser(Users users, Set<UserRole> userRoles) throws Exception;
	
	Users save(Users users);
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, Users users);
	
	void updateUserShipping(UserShipping userShipping, Users users);
	
	void setUserDefaultPayment(Long userPaymentId, Users users);
	
	void setUserDefaultShipping(Long userShippingId, Users users);
}

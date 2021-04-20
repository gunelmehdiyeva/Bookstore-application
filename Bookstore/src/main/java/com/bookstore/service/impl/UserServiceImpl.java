package com.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bookstore.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.domain.Users;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserService;
// Service implementaion
@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	// Injecting dependency on UserRepository object
	@Autowired
	private UserRepository userRepository;
	// Injecting dependency on RoleRepository object
	@Autowired
	private RoleRepository roleRepository;
	// Injecting dependency on UserPaymentRepository object
	@Autowired
	private UserPaymentRepository userPaymentRepository;
	// Injecting dependency on UserShippingRepository object
	@Autowired
	private UserShippingRepository userShippingRepository;
	// Injecting dependency on PasswordResetTokenRepository object
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);// Get token
	}
	
	@Override
	public void createPasswordResetTokenForUser(final Users users, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, users);
		passwordResetTokenRepository.save(myToken);// Save token to database
	}
	
	@Override
	public Users findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public Users findById(Long id){
		return userRepository.findOne(id);
	}
	
	@Override
	public Users findByEmail (String email) {
		return userRepository.findByEmail(email);
	}
	// Within the scope of the transaction
	@Override
	@Transactional
	public Users createUser(Users users, Set<UserRole> userRoles){
		Users localUsers = userRepository.findByUsername(users.getUsername()); // Get user by name
		// If user is not found
		if(localUsers != null) {
			LOG.info("users {} already exists. Nothing will be done.", users.getUsername());
		} else {
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			
			users.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUsers(users);
			users.setShoppingCart(shoppingCart);
			
			users.setUserShippingList(new ArrayList<UserShipping>());
			users.setUserPaymentList(new ArrayList<UserPayment>());
			
			localUsers = userRepository.save(users); // Save user to database
		}
		
		return localUsers;
	}
	
	@Override
	public Users save(Users users) {
		return userRepository.save(users);
	}
	
	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, Users users) {
		userPayment.setUsers(users);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		users.getUserPaymentList().add(userPayment);
		save(users); // Save billing details to database
	}
	
	@Override
	public void updateUserShipping(UserShipping userShipping, Users users){
		userShipping.setUsers(users);
		userShipping.setUserShippingDefault(true);
		users.getUserShippingList().add(userShipping);
		save(users); // Save shipping details to database
	}
	
	@Override
	public void setUserDefaultPayment(Long userPaymentId, Users users) {
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.getId() == userPaymentId) {
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment); // Save payment details to database
			} else {
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
		}
	}
	
	@Override
	public void setUserDefaultShipping(Long userShippingId, Users users) {
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
		
		for (UserShipping userShipping : userShippingList) {
			if(userShipping.getId() == userShippingId) {
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);// Save default shipping details to database
			} else {
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);
			}
		}
	}

}

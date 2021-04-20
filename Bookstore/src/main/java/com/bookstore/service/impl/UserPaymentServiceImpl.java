package com.bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserPayment;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.service.UserPaymentService;
// Service implementaion
@Service
public class UserPaymentServiceImpl implements UserPaymentService{
	// Injecting dependency on UserPaymentRepository object
	@Autowired
	private UserPaymentRepository userPaymentRepository;
		// Find user payment by id
	public UserPayment findById(Long id) {
		return userPaymentRepository.findOne(id);
	}
	
	public void removeById(Long id) {
		userPaymentRepository.delete(id);
	}// delete from database
} 

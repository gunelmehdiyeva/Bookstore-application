package com.bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserShipping;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserShippingService;
// Service implementaion
@Service
public class UserShippingServiceImpl implements UserShippingService{
	// Injecting dependency on UserShippingRepository object
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	// Get shipping by ID
	public UserShipping findById(Long id) {
		return userShippingRepository.findOne(id);
	}
	// Delete from database by id
	public void removeById(Long id) {
		userShippingRepository.delete(id);
	}

}

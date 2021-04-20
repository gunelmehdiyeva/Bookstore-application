package com.bookstore.service;

import com.bookstore.domain.*;
import com.bookstore.domain.Users;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			Users users);
	
	Order findOne(Long id);
}

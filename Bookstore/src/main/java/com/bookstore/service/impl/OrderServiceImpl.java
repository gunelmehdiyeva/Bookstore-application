package com.bookstore.service.impl;

import java.util.Calendar;
import java.util.List;

import com.bookstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Users;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
// Service implementaion
@Service
public class OrderServiceImpl implements OrderService{
	// Injecting dependency on OrderRepository object
	@Autowired
	private OrderRepository orderRepository;
	// Injecting dependency on CartItemService object
	@Autowired
	private CartItemService cartItemService;
	// Creating a new order
	public synchronized Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			Users users) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		// List all cart items
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		// Looping through all cart items
		for(CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUsers(users);
		order = orderRepository.save(order);// Save to database
		
		return order;
	}
	
	public Order findOne(Long id) {
		return orderRepository.findOne(id);
	}

}

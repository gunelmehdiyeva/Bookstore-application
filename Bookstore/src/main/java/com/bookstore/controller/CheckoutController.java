package com.bookstore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.bookstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Users;
import com.bookstore.service.BillingAddressService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.PaymentService;
import com.bookstore.service.ShippingAddressService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.USConstants;
// The annoted class CheckoutController is a controller
@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress(); // Innitiate new ShippingAddress object
	private BillingAddress billingAddress = new BillingAddress(); // Innitiate new BillingAddress object
	private Payment payment = new Payment(); // Innitiate new Payment object
	// Injecting dependency on JavaMailSender object
	@Autowired
	private JavaMailSender mailSender;
	// Injecting dependency on MailConstructor object
	@Autowired
	private MailConstructor mailConstructor;
	// Injecting dependency on UserService object
	@Autowired
	private UserService userService;
	// Injecting dependency on CartItemService object
	@Autowired
	private CartItemService cartItemService;
	// Injecting dependency on ShoppingCartService object
	@Autowired
	private ShoppingCartService shoppingCartService;
	// Injecting dependency on ShippingAddressService object
	@Autowired
	private ShippingAddressService shippingAddressService;
	// Injecting dependency on BillingAddressService object
	@Autowired
	private BillingAddressService billingAddressService;
	// Injecting dependency on PaymentService object
	@Autowired
	private PaymentService paymentService;
	// Injecting dependency on UserShippingService object
	@Autowired
	private UserShippingService userShippingService;
	// Injecting dependency on UserPaymentService object
	@Autowired
	private UserPaymentService userPaymentService;
	// Injecting dependency on OrderService object
	@Autowired
	private OrderService orderService;
	// Maps HTTP requests to handler methods checkout,  of MVC and REST controllers
	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId, // Taking the id and missingRequiredField from the request link
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {
		Users users = userService.findByUsername(principal.getName());// Checking the username

		if (cartId != users.getShoppingCart().getId()) { // If Cart id doesnot matches the user's cart details
			return "badRequestPage";
		}
		// Cart object is sent to findByShoppingCart service and retrived the list of cartItems
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(users.getShoppingCart());
		// If no item in cart
		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		// Looping through the cart items
		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) { // Checking if the stock is available
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = users.getUserShippingList();// Getting of user shipping list
		List<UserPayment> userPaymentList = users.getUserPaymentList();// Getting user payment list

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		// If no payment is available of the user
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		// If no shipping list if found for th user
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		ShoppingCart shoppingCart = users.getShoppingCart();
		// Loop through the list of shopping cart object
		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress); // Setting value
			}
		}
		// Loop through the list of user paument object
		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);// Setting value
			}
		}
		// Adding attributes
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", users.getShoppingCart());
		// Get the list of states of US
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList); // Sorting Alphabetically
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";

	}
	// Maps HTTP requests to handler methods checkout and method is post,  of MVC and REST controllers
	// Gets the attribute values
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
		// Gets the shopping cart of the user
		// Get the list of item in the car
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		// Get shipping address
		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}
		// Checking required filds
		if (shippingAddress.getShippingAddressStreet1().isEmpty() 
				|| shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty() 
				|| payment.getCardNumber().isEmpty()
				|| payment.getCvc() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() 
				|| billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		
		Users users = userService.findByUsername(principal.getName());// Get user details
		// CreateOrder service is call
		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, users);
		// mailSender sercive is called
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(users, order, Locale.ENGLISH));
		// Removing items from cart
		shoppingCartService.clearShoppingCart(shoppingCart);
		// Get local time
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		// Calculate estimated delivery date
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
	}

	// Maps HTTP requests to handler methods setShippingAddress,  of MVC and REST controllers
	// Gets the userShippingId from the link
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal,
			Model model) {
		Users users = userService.findByUsername(principal.getName()); // Get user
		UserShipping userShipping = userShippingService.findById(userShippingId); // Get shipping details
		// If user id is not present
		if (userShipping.getUsers().getId() != users.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);// Setting shipping address
			// Get cart details
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(users.getShoppingCart());
			// Set attributes
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", users.getShoppingCart());
			// Get list of US states
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList); // sort Alphabetically
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = users.getUserShippingList();
			List<UserPayment> userPaymentList = users.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActiveShipping", true);
			// If user payment list is empty
			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			return "checkout";
		}
	}
	// Maps HTTP requests to handler methods setPaymentMethod,  of MVC and REST controllers
	// Gets the userPaymentId from the link
	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal,
			Model model) {
		Users users = userService.findByUsername(principal.getName()); // Get user details
		UserPayment userPayment = userPaymentService.findById(userPaymentId); // Get payment object
		UserBilling userBilling = userPayment.getUserBilling(); // Get billing object
		// If user is not present
		if (userPayment.getUsers().getId() != users.getId()) {
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);
			// Get all cart items
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(users.getShoppingCart());
			// Setting billing address
			billingAddressService.setByUserBilling(userBilling, billingAddress);
			// Set attributes
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", users.getShoppingCart());
			// Get list of us States
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = users.getUserShippingList();
			List<UserPayment> userPaymentList = users.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActivePayment", true);

			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}

}

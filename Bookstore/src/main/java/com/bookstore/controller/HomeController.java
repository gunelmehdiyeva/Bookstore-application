package com.bookstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import com.bookstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Users;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.service.impl.UserSecurityService;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;
import com.bookstore.utility.USConstants;
// The annoted class CheckoutController is a controller
@Controller
public class HomeController {
	// Injecting dependency on JavaMailSender object
	@Autowired
	private JavaMailSender mailSender;
	// Injecting dependency on MailConstructor object
	@Autowired
	private MailConstructor mailConstructor;
	// Injecting dependency on UserService object
	@Autowired
	private UserService userService;
	// Injecting dependency on UserSecurityService object
	@Autowired
	private UserSecurityService userSecurityService;
	// Injecting dependency on BookService object
	@Autowired
	private BookService bookService;
	// Injecting dependency on UserPaymentService object
	@Autowired
	private UserPaymentService userPaymentService;
	// Injecting dependency on UserShippingService object
	@Autowired
	private UserShippingService userShippingService;
	// Injecting dependency on CartItemService object
	@Autowired
	private CartItemService cartItemService;
	// Injecting dependency on OrderService object
	@Autowired
	private OrderService orderService;
	// Maps HTTP requests to handler methods /,  of MVC and REST controllers
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	// Maps HTTP requests to handler methods login,  of MVC and REST controllers
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	// Maps HTTP requests to handler methods hours,  of MVC and REST controllers
	@RequestMapping("/hours")
	public String hours() {
		return "hours";
	}
	// Maps HTTP requests to handler methods faq,  of MVC and REST controllers
	@RequestMapping("/faq")
	public String faq() {
		return "faq";
	}
	// Maps HTTP requests to handler methods bookshelf,  of MVC and REST controllers
	@RequestMapping("/bookshelf")
	public String bookshelf(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName(); // Get user name
			Users users = userService.findByUsername(username);
			model.addAttribute("users", users);
		}
		
		List<Book> bookList = bookService.findAll(); // Get all books
		model.addAttribute("bookList", bookList);
		model.addAttribute("activeAll",true);
		
		return "bookshelf";
	}
	// Maps HTTP requests to handler bookDetail,  of MVC and REST controllers
	// Get the id from the parameter
	@RequestMapping("/bookDetail")
	public String bookDetail(
			@PathParam("id") Long id, Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			Users users = userService.findByUsername(username); // Get User object
			model.addAttribute("users", users);
		}
		
		Book book = bookService.findOne(id); // Find the book by ID
		
		model.addAttribute("book", book);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "bookDetail";
	}
	// Maps HTTP requests to handler forgetPassword,  of MVC and REST controllers
	@RequestMapping("/forgetPassword")
	public String forgetPassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Model model
			) {

		model.addAttribute("classActiveForgetPassword", true);
		
		Users users = userService.findByEmail(email);// Find user by email
		// If email not found
		if (users == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
		
		String password = SecurityUtility.randomPassword(); // Generate random password
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password); // Encrypt password
		users.setPassword(encryptedPassword); // Set Password
		
		userService.save(users); // Save in database
		
		String token = UUID.randomUUID().toString(); // get Password token
		userService.createPasswordResetTokenForUser(users, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, users, password);
		
		mailSender.send(newEmail); // Email sent
		
		model.addAttribute("forgetPasswordEmailSent", "true");
		
		
		return "myAccount";
	}
	// Maps HTTP requests to handler myProfile,  of MVC and REST controllers
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		Users users = userService.findByUsername(principal.getName());
		model.addAttribute("users", users); // Get user by user name
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("orderList", users.getOrderList());
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
	}
	// Maps HTTP requests to handler listOfCreditCards,  of MVC and REST controllers
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(
			Model model, Principal principal, HttpServletRequest request
			) {
		Users users = userService.findByUsername(principal.getName());// Get user by user name
		model.addAttribute("users", users);
		model.addAttribute("userPaymentList", users.getUserPaymentList()); // Get payment list
		model.addAttribute("userShippingList", users.getUserShippingList());// Get Shipping list
		model.addAttribute("orderList", users.getOrderList()); // Get Order list
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	// Maps HTTP requests to handler listOfShippingAddresses,  of MVC and REST controllers
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		Users users = userService.findByUsername(principal.getName());// Get user by user name
		model.addAttribute("users", users);
		model.addAttribute("userPaymentList", users.getUserPaymentList());// Get payment list
		model.addAttribute("userShippingList", users.getUserShippingList());// Get Shipping list
		model.addAttribute("orderList", users.getOrderList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	// Maps HTTP requests to handler addNewCreditCard,  of MVC and REST controllers
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(
			Model model, Principal principal
			){
		Users users = userService.findByUsername(principal.getName());
		model.addAttribute("users", users);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler addNewShippingAddress,  of MVC and REST controllers
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		Users users = userService.findByUsername(principal.getName()); // Get user by user name
		model.addAttribute("users", users);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler addNewCreditCard,  of MVC and REST controllers
	@RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
	public String addNewCreditCard(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Principal principal, Model model
			){
		Users users = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, users);
		
		model.addAttribute("users", users);
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler addNewShippingAddress,  of MVC and REST controllers
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model
			){
		Users users = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, users);
		
		model.addAttribute("users", users);
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}

	// Maps HTTP requests to handler updateCreditCard,  of MVC and REST controllers
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			) {
		Users users = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);// Find payment by ID
		// IF user is not available
		if(users.getId() != userPayment.getUsers().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("users", users);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", users.getUserPaymentList());
			model.addAttribute("userShippingList", users.getUserShippingList());
			model.addAttribute("orderList", users.getOrderList());
			
			return "myProfile";
		}
	}
	// Maps HTTP requests to handler updateUserShipping,  of MVC and REST controllers
	// Get id from the link attribute
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
			) {
		Users users = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		// If user is not available
		if(users.getId() != userShipping.getUsers().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("users", users);
			
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			model.addAttribute("userPaymentList", users.getUserPaymentList());
			model.addAttribute("userShippingList", users.getUserShippingList());
			model.addAttribute("orderList", users.getOrderList());
			
			return "myProfile";
		}
	}
	// Maps HTTP requests to handler updateUserShipping,  of MVC and REST controllers
	// Get defaultUserPaymentId from the link attribute
	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
	public String setDefaultPayment(
			@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
			) {
		Users users = userService.findByUsername(principal.getName());// Get user object
		userService.setUserDefaultPayment(defaultPaymentId, users);
		
		model.addAttribute("users", users);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler setDefaultShippingAddress,  of MVC and REST controllers
	// Get defaultShippingAddressId from the link attribute
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
			) {
		Users users = userService.findByUsername(principal.getName()); // Get users
		userService.setUserDefaultShipping(defaultShippingId, users);
		
		model.addAttribute("users", users);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", users.getUserPaymentList());
		model.addAttribute("userShippingList", users.getUserShippingList());
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler removeCreditCard,  of MVC and REST controllers
	// Get id from the link attribute
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			){
		Users users = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(users.getId() != userPayment.getUsers().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("users", users);
			userPaymentService.removeById(creditCardId);
			
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", users.getUserPaymentList());
			model.addAttribute("userShippingList", users.getUserShippingList());
			model.addAttribute("orderList", users.getOrderList());
			
			return "myProfile";
		}
	}
	// Maps HTTP requests to handler removeUserShipping,  of MVC and REST controllers
	// Get id from the link attribute
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
			){
		Users users = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(users.getId() != userShipping.getUsers().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("users", users);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userPaymentList", users.getUserPaymentList());
			model.addAttribute("userShippingList", users.getUserShippingList());
			model.addAttribute("orderList", users.getOrderList());
			
			return "myProfile";
		}
	}
	// Maps HTTP requests to handler newUser,  of MVC and REST controllers
	// Get email,username from the link attribute
	@RequestMapping(value="/newUser", method = RequestMethod.POST)
	public String newUserPost(
			HttpServletRequest request,
			@ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username,
			Model model
			) throws Exception{
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			
			return "myAccount";
		}
		
		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			
			return "myAccount";
		}
		
		Users users = new Users();
		users.setUsername(username);
		users.setEmail(userEmail);
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		users.setPassword(encryptedPassword);
		
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(users, role));
		userService.createUser(users, userRoles);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(users, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, users, password);
		
		mailSender.send(email);
		
		model.addAttribute("emailSent", "true");
		model.addAttribute("orderList", users.getOrderList());
		
		return "myAccount";
	}

	// Maps HTTP requests to handler newUser,  of MVC and REST controllers
	// Get token from link parameter
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		Users users = passToken.getUsers();
		String username = users.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("users", users);

		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
	}
	// Maps HTTP requests to handler updateUserInfo,  of MVC and REST controllers
	// Get email,username from the link attribute
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfo(
			@ModelAttribute("users") Users users,
			@ModelAttribute("newPassword") String newPassword,
			Model model
			) throws Exception {
		Users currentUsers = userService.findById(users.getId());
		
		if(currentUsers == null) {
			throw new Exception ("Users not found");
		}
		
		/*check email already exists*/
		if (userService.findByEmail(users.getEmail())!=null) {
			if(userService.findByEmail(users.getEmail()).getId() != currentUsers.getId()) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}
		
		/*check username already exists*/
		if (userService.findByUsername(users.getUsername())!=null) {
			if(userService.findByUsername(users.getUsername()).getId() != currentUsers.getId()) {
				model.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}
		
//		update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUsers.getPassword();
			if(passwordEncoder.matches(users.getPassword(), dbPassword)){
				currentUsers.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				
				return "myProfile";
			}
		}
		
		currentUsers.setFirstName(users.getFirstName());
		currentUsers.setLastName(users.getLastName());
		currentUsers.setUsername(users.getUsername());
		currentUsers.setEmail(users.getEmail());
		
		userService.save(currentUsers);
		
		model.addAttribute("updateSuccess", true);
		model.addAttribute("users", currentUsers);
		model.addAttribute("classActiveEdit", true);
		
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("listOfCreditCards", true);
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUsers.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("orderList", users.getOrderList());
		
		return "myProfile";
	}
	// Maps HTTP requests to handler orderDetail,  of MVC and REST controllers
	// Get id from the link param
	@RequestMapping("/orderDetail")
	public String orderDetail(
			@RequestParam("id") Long orderId,
			Principal principal, Model model
			){
		Users users = userService.findByUsername(principal.getName());
		Order order = orderService.findOne(orderId);
		
		if(order.getUsers().getId()!= users.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("users", users);
			model.addAttribute("order", order);
			
			model.addAttribute("userPaymentList", users.getUserPaymentList());
			model.addAttribute("userShippingList", users.getUserShippingList());
			model.addAttribute("orderList", users.getOrderList());
			
			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);
			
			return "myProfile";
		}
	}
	
	
	
	
}

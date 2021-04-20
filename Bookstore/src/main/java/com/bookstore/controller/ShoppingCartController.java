package com.bookstore.controller;

import java.security.Principal;
import java.util.List;

import com.bookstore.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;
// The annoted class CheckoutController is a controller
// Maps HTTP requests to handler methods shoppingCart,  of MVC and REST controllers
@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	// Injecting dependency on UserService object
	@Autowired
	private UserService userService;
	// Injecting dependency on CartItemService object
	@Autowired
	private CartItemService cartItemService;
	// Injecting dependency on BookService object
	@Autowired
	private BookService bookService;
	// Injecting dependency on ShoppingCartService object
	@Autowired
	private ShoppingCartService shoppingCartService;
	// Maps HTTP requests to handler methods cart,  of MVC and REST controllers
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		Users users = userService.findByUsername(principal.getName()); // Get user by name
		ShoppingCart shoppingCart = users.getShoppingCart(); // Get shopping cart of the user
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart); // Get all cart items
		// Update service is called
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}
	// Maps HTTP requests to handler methods addItem,  of MVC and REST controllers
	// Get book, qty from link attribute
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("book") Book book,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		Users users = userService.findByUsername(principal.getName()); // Get user by name
		book = bookService.findOne(book.getId()); // find book by id
		// if stock is not present
		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id="+book.getId();
		}
		// add book service is called
		CartItem cartItem = cartItemService.addBookToCartItem(book, users, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);
		
		return "forward:/bookDetail?id="+book.getId();
	}
	// Maps HTTP requests to handler methods updateCartItem,  of MVC and REST controllers
	// Get id, qty from link attribute
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);// Get cart by id
		cartItem.setQty(qty); // set quantity
		cartItemService.updateCartItem(cartItem); // Update cart service is called
		
		return "forward:/shoppingCart/cart";
	}
	// Maps HTTP requests to handler methods updateCartItem,  of MVC and REST controllers
	// Get id from link attribute
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));// Remove from cart service is called
		
		return "forward:/shoppingCart/cart";
	}
}

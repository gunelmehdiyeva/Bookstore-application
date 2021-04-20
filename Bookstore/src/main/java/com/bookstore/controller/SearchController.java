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
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;
// The annoted class CheckoutController is a controller
@Controller
public class SearchController {
	// Injecting dependency on UserService object
	@Autowired
	private UserService userService;
	// Injecting dependency on BookService object
	@Autowired
	private BookService bookService;
	// Maps HTTP requests to handler methods searchByCategory,  of MVC and REST controllers
	// Get the category from link parameter
	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") String category,
			Model model, Principal principal
			){
		if(principal!=null) {
			String username = principal.getName(); // get user name
			Users users = userService.findByUsername(username); // get user by user name
			model.addAttribute("users", users);
		}
		
		String classActiveCategory = "active"+category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Book> bookList = bookService.findByCategory(category); // Get all list of books by category
		// If not book found
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	// Maps HTTP requests to handler methods searchBook,  of MVC and REST controllers
	// Get the keyword from link attribute
	@RequestMapping("/searchBook")
	public String searchBook(
			@ModelAttribute("keyword") String keyword,
			Principal principal, Model model
			) {
		if(principal!=null) {
			String username = principal.getName();
			Users users = userService.findByUsername(username);// Get user by username
			model.addAttribute("users", users);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);// Do blurry search by the keyword entered
		// If no book found.
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
}

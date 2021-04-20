package com.adminportal.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adminportal.service.BookService;
// The annoted class CheckoutController is a controller
@RestController
public class ResourceController {
	// Injecting dependency on BookService object
	@Autowired
	private BookService bookService;
	// Maps HTTP requests to handler methods book/removeList,  of MVC and REST controllers
	// Method is post
	@RequestMapping(value="/book/removeList", method=RequestMethod.POST)
	public String removeList(
			@RequestBody ArrayList<String> bookIdList, Model model
			){
		// Looping through the book list
		for (String id : bookIdList) {
			String bookId =id.substring(8);
			bookService.removeOne(Long.parseLong(bookId));// Remove book one at a time from database
		}
		
		return "delete success";
	}
}

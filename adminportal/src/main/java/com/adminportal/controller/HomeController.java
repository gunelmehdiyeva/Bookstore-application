package com.adminportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
// The annoted class CheckoutController is a controller
@Controller
public class HomeController {
	// Maps HTTP requests to handler methods /,  of MVC and REST controllers
	@RequestMapping("/")
	public String index(){
		return "redirect:/home";
	}
	// Maps HTTP requests to handler methods home,  of MVC and REST controllers
	@RequestMapping("/home")
	public String home(){
		return "home";
	}
	// Maps HTTP requests to handler methods login,  of MVC and REST controllers
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
}

package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.domain.Book;
import com.adminportal.service.BookService;
// The annoted class CheckoutController is a controller
// Maps HTTP requests to handler methods book,  of MVC and REST controllers
@Controller
@RequestMapping("/book")
public class BookController {
	// Injecting dependency on BookService object
	@Autowired
	private BookService bookService;
	// Maps HTTP requests to handler methods add,  of MVC and REST controllers
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "addBook";
	}
	// Maps HTTP requests to handler methods add,  of MVC and REST controllers
	// Get book from the link attribute
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(
					/*new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));*/
			        new FileOutputStream(new File("src/main/resources/upload/" + name))); // Get image from static
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:bookList";
	}
	// Maps HTTP requests to handler methods bookInfo,  of MVC and REST controllers
	// Get id from the link param
	@RequestMapping("/bookInfo")
	public String bookInfo(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id); // Find book by id
		model.addAttribute("book", book);
		
		return "bookInfo";
	}
	// Maps HTTP requests to handler methods updateBook,  of MVC and REST controllers
	// Get id from the link param
	@RequestMapping("/updateBook")
	public String updateBook(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		
		return "updateBook";
	}
	// Maps HTTP requests to handler methods updateBook,  of MVC and REST controllers
	// Get book from the link param
	@RequestMapping(value="/updateBook", method=RequestMethod.POST)
	public String updateBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.save(book); // Save book to database
		
		MultipartFile bookImage = book.getBookImage();
		// If book image is not empty
		if(!bookImage.isEmpty()) {
			try {
				byte[] bytes = bookImage.getBytes();
				String name = book.getId() + ".png";
				// Delete from static
				Files.delete(Paths.get("src/main/resources/static/image/book/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/book/bookInfo?id="+book.getId();
	}
	// Maps HTTP requests to handler methods bookList,  of MVC and REST controllers
	@RequestMapping("/bookList")
	public String bookList(Model model) {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);		
		return "bookList";
		
	}
	// Maps HTTP requests to handler methods remove,  of MVC and REST controllers
	// Get id from link attribute
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(
			@ModelAttribute("id") String id, Model model
			) {
		bookService.removeOne(Long.parseLong(id.substring(8))); // Remove from database
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		
		return "redirect:/book/bookList";
	}

}

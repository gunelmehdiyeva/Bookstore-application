package com.bookstore.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bookstore.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bookstore.domain.Order;
// The annoted class CheckoutController is a controller
@Component
public class MailConstructor {
	// Injecting dependency on Environment object
	@Autowired
	private Environment env;
	// Injecting dependency on TemplateEngine object
	@Autowired
	private TemplateEngine templateEngine;
	// Preparing the email body and context
	public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, Users users, String password
			) {
		
		String url = contextPath + "/newUser?token="+token;
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"+password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(users.getEmail());
		email.setSubject("Le's Bookstore - New Users");
		email.setText(url+message);
		email.setFrom(env.getProperty("support.email"));
		return email;
		
	}
	// Prepare the email context from the confirmation of order.
	public MimeMessagePreparator constructOrderConfirmationEmail (Users users, Order order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", users);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(users.getEmail());
				email.setSubject("Order Confirmation - "+order.getId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("ray.deng83@gmail.com"));
			}
		};
		
		return messagePreparator;
	}
}

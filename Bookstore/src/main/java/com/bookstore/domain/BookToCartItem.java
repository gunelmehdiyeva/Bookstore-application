package com.bookstore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
// The @Entity annotation specifies that the class BookToCartItem is mapped to a table in the database
@Entity
public class BookToCartItem {
	// The @Id annotation specifies the primary key of the entity, which is the id here
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created betwee book and BookToCartItem entity.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the column book_id
	@ManyToOne
	@JoinColumn(name="book_id")
	private Book book;

	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created betwee cartItem and BookToCartItem entity.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the column cart_item_id
	@ManyToOne
	@JoinColumn(name="cart_item_id")
	private CartItem cartItem;

	// All the required Set and Get operations for the fileds in BookToCartItem entity

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public CartItem getCartItem() {
		return cartItem;
	}

	public void setCartItem(CartItem cartItem) {
		this.cartItem = cartItem;
	}
	
	
}

package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
// The @Entity annotation specifies that the class CartItem is mapped to a table in the database
@Entity
public class CartItem {
	// The @Id annotation specifies the primary key of the entity, which is the id here
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int qty;
	private BigDecimal subtotal;
	// The @OneToOne annotation is used to map the source entity CartItem with the target Book entity.
	@OneToOne
	private Book book;

	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created between CartItem and List of BookToCartItem entity.
	// @JasonIgnore is used to skip the fields during serialization and deserialization
	@OneToMany(mappedBy = "cartItem")
	@JsonIgnore
	private List<BookToCartItem> bookToCartItemList;
	// The @ManyToOne annotation with mappedBy clause signifies that many CartItem entity can be mapped to
	// one ShoppingCart object.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the
	// column shopping_cart_id
	@ManyToOne
	@JoinColumn(name="shopping_cart_id")
	private ShoppingCart shoppingCart;
	// The @ManyToOne annotation with mappedBy clause signifies that many CartItem entity can be mapped to
	// one Order object.
	// The @JoinColumn annotation provides the information that the Many-to-one mapping will be done on the
	// column order_id
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;

	// All the required Set and Get operations for the fileds in CartItem entity

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<BookToCartItem> getBookToCartItemList() {
		return bookToCartItemList;
	}

	public void setBookToCartItemList(List<BookToCartItem> bookToCartItemList) {
		this.bookToCartItemList = bookToCartItemList;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}

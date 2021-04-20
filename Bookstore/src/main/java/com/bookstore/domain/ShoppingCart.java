package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
// The @Entity annotation specifies that the class ShoppingCart is mapped to a table in the database
@Entity
public class ShoppingCart {
	// The @Id annotation specifies the primary key of the entity, which is the id here
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private BigDecimal GrandTotal;
	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created between ShoppingCart and List of CartItem entity. In this type of association the instance
	// of ShoppingCart entity can be mapped with any number of instances of CartItem entity
	// @JasonIgnore is used to skip the fields during serialization and deserialization
	@OneToMany(mappedBy="shoppingCart", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnore
	private List<CartItem> cartItemList;
	// The @OneToOne annotation is used to map the source entity ShoppingCart with the target Users entity.
	@OneToOne(cascade = CascadeType.ALL)
	private Users users;

	// All the required Set and Get operations for the fileds in ShippingAddress entity
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getGrandTotal() {
		return GrandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		GrandTotal = grandTotal;
	}

	public List<CartItem> getCartItemList() {
		return cartItemList;
	}

	public void setCartItemList(List<CartItem> cartItemList) {
		this.cartItemList = cartItemList;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	
}

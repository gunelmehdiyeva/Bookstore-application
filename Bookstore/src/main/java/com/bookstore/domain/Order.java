package com.bookstore.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
// The @Entity annotation specifies that the class Order is mapped to a table in the database
// Here the table name is mentioned is mentioned as user_order, since the class name and the
// database table name is not some.
@Entity
@Table(name="user_order")
public class Order {
	// The @Id annotation specifies the primary key of the entity, which is the id here
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date orderDate;
	private Date shippingDate;
	private String shippingMethod;
	private String orderStatus;
	private BigDecimal orderTotal;
	// The @OneToMany annotation with mappedBy clause signifies that a bidirectional association has been
	// created between CartItem and List of Order entity. In this type of association the instance
	// of Order entity can be mapped with any number of instances of CartItem entity
	@OneToMany(mappedBy = "order", cascade=CascadeType.ALL )
	private List<CartItem> cartItemList;
	// The @OneToOne annotation is used to map the source entity Order with the target ShippingAddress entity.
	@OneToOne(cascade=CascadeType.ALL)
	private ShippingAddress shippingAddress;
	// The @OneToOne annotation is used to map the source entity Order with the target BillingAddress entity.
	@OneToOne(cascade=CascadeType.ALL)
	private BillingAddress billingAddress;
	// The @OneToOne annotation is used to map the source entity Order with the target Payment entity.
	@OneToOne(cascade=CascadeType.ALL)
	private Payment payment;
	// The @ManyToOne annotation with mappedBy clause signifies that many CartItem entity can be mapped to
	// one ShoppingCart object.
	@ManyToOne
	private Users users;

	// All the required Set and Get operations for the fileds in Order entity

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}

	public List<CartItem> getCartItemList() {
		return cartItemList;
	}

	public void setCartItemList(List<CartItem> cartItemList) {
		this.cartItemList = cartItemList;
	}

	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	
}

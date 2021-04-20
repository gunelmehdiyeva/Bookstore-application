package com.bookstore.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

// The @Entity annotation specifies that the class BillingAddress is mapped to a table in the database
@Entity
public class BillingAddress {

	// The @Id annotation specifies the primary key of the entity, which is the id here
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String BillingAddressName;
	private String BillingAddressStreet1;
	private String BillingAddressStreet2;
	private String BillingAddressCity;
	private String BillingAddressState;
	private String BillingAddressCountry;
	private String BillingAddressZipcode;
	// The @OneToOne annotation is used to map the source entity BillingAddress with the target Order entity.
	@OneToOne
	private Order order;
	// Get operation for id in the BillingAddress entity
	public Long getId() {
		return id;
	}
	// Set operation for id in the BillingAddress entity
	public void setId(Long id) {
		this.id = id;
	}
	// Get operation for BillingAddressName in the BillingAddress entity
	public String getBillingAddressName() {
		return BillingAddressName;
	}
	// Set operation for BillingAddressName in the BillingAddress entity
	public void setBillingAddressName(String billingAddressName) {
		BillingAddressName = billingAddressName;
	}
	// Get operation for BillingAddressStreet1 in the BillingAddress entity
	public String getBillingAddressStreet1() {
		return BillingAddressStreet1;
	}
	// Set operation for BillingAddressStreet1 in the BillingAddress entity
	public void setBillingAddressStreet1(String billingAddressStreet1) {
		BillingAddressStreet1 = billingAddressStreet1;
	}
	// Get operation for BillingAddressStreet2 in the BillingAddress entity
	public String getBillingAddressStreet2() {
		return BillingAddressStreet2;
	}
	// Set operation for BillingAddressStreet2 in the BillingAddress entity
	public void setBillingAddressStreet2(String billingAddressStreet2) {
		BillingAddressStreet2 = billingAddressStreet2;
	}
	// Get operation for BillingAddressCity in the BillingAddress entity
	public String getBillingAddressCity() {
		return BillingAddressCity;
	}
	// Set operation for BillingAddressCity in the BillingAddress entity
	public void setBillingAddressCity(String billingAddressCity) {
		BillingAddressCity = billingAddressCity;
	}
	// Get operation for BillingAddressState in the BillingAddress entity
	public String getBillingAddressState() {
		return BillingAddressState;
	}
	// Set operation for BillingAddressState in the BillingAddress entity
	public void setBillingAddressState(String billingAddressState) {
		BillingAddressState = billingAddressState;
	}
	// Get operation for BillingAddressCountry in the BillingAddress entity
	public String getBillingAddressCountry() {
		return BillingAddressCountry;
	}
	// Set operation for BillingAddressCountry in the BillingAddress entity
	public void setBillingAddressCountry(String billingAddressCountry) {
		BillingAddressCountry = billingAddressCountry;
	}
	// Get operation for BillingAddressZipcode in the BillingAddress entity
	public String getBillingAddressZipcode() {
		return BillingAddressZipcode;
	}
	// Set operation for BillingAddressZipcode in the BillingAddress entity
	public void setBillingAddressZipcode(String billingAddressZipcode) {
		BillingAddressZipcode = billingAddressZipcode;
	}
	// Get operation for Order object in the BillingAddress entity
	public Order getOrder() {
		return order;
	}
	// Set operation for Order object in the BillingAddress entity
	public void setOrder(Order order) {
		this.order = order;
	}

}

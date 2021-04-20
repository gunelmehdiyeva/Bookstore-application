package com.bookstore.domain.security;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.bookstore.domain.Users;

// The @Entity annotation specifies that the class is mapped to a table in the database
@Entity
public class PasswordResetToken {

	private static final int EXPIRATION = 60 * 24;
	// The @Id annotation specifies the primary key of the entity
	// The @GeneratedValue specifies the specification of generation strategies for the values of primary keys.
	// Here GenerationType.AUTO is used which is the defalut type of strategy and it allows the database to
	// Choose the database specific stratety. In most cases the GenerationType.SEQUENCE is selected.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String token;
	// The @OneToOne annotation is used to map the source entity PasswordRestToken with the target Users entity.
	// The @JoinColumn annotation provides the information that the one-to-one mapping will be done on the column user_id
	@OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable=false, name="user_id")
	private Users user;
	
	private Date expiryDate;

	// Default Constructor
	public PasswordResetToken(){}

	// Specific construction which take the a string named token and a object of user class as arguments.
	public PasswordResetToken(final String token, final Users user) {
		super ();
		// Setting of the values
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION); // calculateExpiryDate Method is invoked
	}

	// calculateExpiryDate method which exact expiry date of the password token
	private Date calculateExpiryDate (final int expiryTimeInMinutes) {
		final Calendar cal = Calendar.getInstance(); // New instance of calendar is initiated
		cal.setTimeInMillis(new Date().getTime()); // Get the current datetime in milliseconds
		cal.add(Calendar.MINUTE, expiryTimeInMinutes); // Adding the expiry time in minutes to the current datetime
		return new Date(cal.getTime().getTime()); // Returning the expiry date.
	}

	// updateToken method which takes a srting named token as argument
	public void updateToken(final String token) {
		this.token = token; // Setting token value
		this.expiryDate = calculateExpiryDate(EXPIRATION); // Setting expiry date using the calculateExpiryDate Method
	}

	public Long getId() {
		return id;
	} // Get operation for Id in the PasswordRestToken entity

	public void setId(Long id) {
		this.id = id;
	} // Set operation for Id in the PasswordRestToken entity

	public String getToken() {
		return token;
	} // Get operation for token in the PasswordRestToken entity

	public void setToken(String token) {
		this.token = token;
	} // Set operation for token in the PasswordRestToken entity

	public Users getUsers() {
		return user;
	} // Get operation for user in the PasswordRestToken entity

	public void setUsers(Users user) {
		this.user = user;
	} // Get operation for user in the PasswordRestToken entity

	public Date getExpiryDate() {
		return expiryDate;
	} // Get operation for expiryDate in the PasswordRestToken entity

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}// Set operation for expiryDate in the PasswordRestToken entity

	public static int getExpiration() {
		return EXPIRATION;
	} // Get operation for EXPIRATION value

	// The toString method is overriden and this method will retun the PasswordResetToken
	@Override
	public String toString() {
		return "PasswordResetToken [id=" + id + ", token=" + token + ", users=" + user + ", expiryDate=" + expiryDate
				+ "]";
	}
	
	
}

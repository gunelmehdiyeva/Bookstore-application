
package com.bookstore.domain.security;

import org.springframework.security.core.GrantedAuthority;

// Class Authority implements Granted Authority interface, it represents an authority granted to an authentication object.
public class Authority implements GrantedAuthority{
	private final String authority;
	// Constructor of the class
	public Authority(String authority) {
		this.authority = authority;
	}// Set operation of authority value
	
	@Override
	public String getAuthority() {
		return authority;
	}// Get operation of authority value
}

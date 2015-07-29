/**
 * 
 */
package com.oauth.example.modal;

/**
 * @author DEEPAK
 *
 */
public class UserCredentials {

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private User user;
	private String password;

}

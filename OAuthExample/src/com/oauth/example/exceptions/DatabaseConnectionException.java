/**
 * 
 */
package com.oauth.example.exceptions;

/**
 * @author DEEPAK R SHANKAR
 *
 */
public class DatabaseConnectionException extends ClassNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DatabaseConnectionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 * @param ex
	 */
	public DatabaseConnectionException(String s, Throwable ex) {
		super(s, ex);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 */
	public DatabaseConnectionException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	

}

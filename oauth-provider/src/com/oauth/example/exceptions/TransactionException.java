/**
 * 
 */
package com.oauth.example.exceptions;

import java.sql.SQLException;

/**
 * @author DEEPAK R SHANKAR
 *
 */
public class TransactionException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TransactionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 */
	public TransactionException(String reason) {
		super(reason);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public TransactionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param SQLState
	 */
	public TransactionException(String reason, String SQLState) {
		super(reason, SQLState);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param cause
	 */
	public TransactionException(String reason, Throwable cause) {
		super(reason, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param SQLState
	 * @param vendorCode
	 */
	public TransactionException(String reason, String SQLState, int vendorCode) {
		super(reason, SQLState, vendorCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param cause
	 */
	public TransactionException(String reason, String sqlState, Throwable cause) {
		super(reason, sqlState, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param vendorCode
	 * @param cause
	 */
	public TransactionException(String reason, String sqlState, int vendorCode,
			Throwable cause) {
		super(reason, sqlState, vendorCode, cause);
		// TODO Auto-generated constructor stub
	}

}

/**
 * 
 */
package com.oauth.example.exceptions;

import java.sql.SQLException;

/**
 * @author DEEPAK R SHANKAR
 *
 */
public class BusinessException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public BusinessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param vendorCode
	 * @param cause
	 */
	public BusinessException(String reason, String sqlState, int vendorCode,
			Throwable cause) {
		super(reason, sqlState, vendorCode, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param SQLState
	 * @param vendorCode
	 */
	public BusinessException(String reason, String SQLState, int vendorCode) {
		super(reason, SQLState, vendorCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param cause
	 */
	public BusinessException(String reason, String sqlState, Throwable cause) {
		super(reason, sqlState, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param SQLState
	 */
	public BusinessException(String reason, String SQLState) {
		super(reason, SQLState);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param cause
	 */
	public BusinessException(String reason, Throwable cause) {
		super(reason, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 */
	public BusinessException(String reason) {
		super(reason);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public BusinessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}

/**
 * 
 */
package com.oauth.example.dao.util;

import com.oauth.example.exceptions.DatabaseConnectionException;

/**
 * @author DEEPAK
 *
 */
public class ConnectionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		try {
			ConnectionUtil.getDBConnectionToMySQL();
		} catch (DatabaseConnectionException e) {
			
			e.printStackTrace();
		}
		finally {
			try {
				ConnectionUtil.closeDBConnection();
			} catch (DatabaseConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

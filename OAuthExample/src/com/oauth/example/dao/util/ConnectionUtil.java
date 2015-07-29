/**
 * 
 */
package com.oauth.example.dao.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oauth.example.exceptions.BusinessException;
import com.oauth.example.exceptions.DatabaseConnectionException;
import com.oauth.example.exceptions.TransactionException;

public class ConnectionUtil {

	private static Connection conn = null;

	static final Logger logger = Logger.getLogger(ConnectionUtil.class);

	/**
	 * This method is used to get a connection to the oracle database.
	 * 
	 * @return Returns a connection object.
	 * @throws DatabaseConnectionException
	 */
	public static Connection getDBConnectionToOracle() throws DatabaseConnectionException {

		String driver;
		String url;
		String userName;
		String password;
		Properties properties = new Properties();
		InputStream stream;

		try {

			stream = ConnectionUtil.class.getClassLoader().getResourceAsStream("database.properties");

			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		driver = properties.getProperty("oracle.driver");
		url = properties.getProperty("oracle.url");
		userName = properties.getProperty("oracle.user");
		password = properties.getProperty("oracle.password");

		try {

			logger.debug("trying to connect to the Oracle database... " + url);
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			logger.debug("connection successful");
			return conn;

		} catch (ClassNotFoundException e) {

			logger.debug("Invalid database driver!!! " + driver);
			throw new DatabaseConnectionException("Could not connect to the database!!! driver not found!!!", e);

		} catch (SQLException e) {

			throw new DatabaseConnectionException(
					"Could not connect to the database!!! username or password incorrect!!!", e);

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * This method is used to get a connection to the MySQL database.
	 * 
	 * @return Returns a connection object.
	 * @throws DatabaseConnectionException
	 */
	public static Connection getDBConnectionToMySQL() throws DatabaseConnectionException {

		String driver;
		String url;
		String userName;
		String password;
		Properties properties = new Properties();
		InputStream stream;

		try {

			stream = ConnectionUtil.class.getClassLoader().getResourceAsStream("database.properties");

			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		driver = properties.getProperty("mysql.driver");
		url = properties.getProperty("mysql.url");
		userName = properties.getProperty("mysql.user");
		password = properties.getProperty("mysql.password");

		try {

			logger.debug("trying to connect to MySQL the database... " + url);
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			logger.debug("connection successful");
			return conn;

		} catch (ClassNotFoundException e) {

			logger.debug("Invalid database driver!!! " + driver);
			throw new DatabaseConnectionException("Could not connect to the database!!! driver not found!!!", e);

		} catch (SQLException e) {

			throw new DatabaseConnectionException(
					"Could not connect to the database!!! username or password incorrect!!!", e);

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * This method is used to close an existing connection to the database.
	 * 
	 * @throws DatabaseConnectionException
	 */
	public static void closeDBConnection() throws DatabaseConnectionException {

		try {
			conn.close();
			logger.debug("Connection to the database closed successfully");
		} catch (SQLException ex) {
			logger.debug("connection to the database could not be closed!!! the exception is \n" + ex.getStackTrace()
					+ "\n");
			throw new DatabaseConnectionException(
					"Could not close the connection to the database!!! Connection already closed?", ex);
		}
	}

	/**
	 * @throws DatabaseConnectionException
	 * @throws SQLException
	 * 
	 */
	public static void toggelAutoCommit(Boolean value) throws DatabaseConnectionException {

		logger.debug("toggling AUTOCOMMIT to " + value);
		try {
			conn.setAutoCommit(value);
		} catch (SQLException e) {
			logger.debug("AUTOCOMMIT could not be toggled!!! the exception is \n" + e.getStackTrace() + "\n");
			throw new DatabaseConnectionException("Could not toggle AUTOCOMMIT!!!", e);
		}

	}

	/**
	 * @throws BusinessException
	 * @throws SQLException
	 * 
	 */
	public static void commitTransaction() throws BusinessException {

		logger.debug("Commiting the transaction...");
		try {
			conn.commit();
		} catch (SQLException e) {

			logger.debug("COMMIT failed!!! rollingback now...");

			try {
				rollbackTransaction();
			} catch (TransactionException ex) {
				logger.debug("rollback failure!!!" + ex.getStackTrace());
				ex.printStackTrace();
			}

			throw new BusinessException("Could not commit the transaction...", e);
		}

	}

	/**
	 * @throws TransactionException
	 * @throws BusinessException
	 * 
	 * 
	 */
	public static void rollbackTransaction() throws TransactionException {

		logger.debug("Rolling back...");
		try {
			conn.rollback();
		} catch (SQLException e) {

			throw new TransactionException("Rollback to the savepoint failed!!!", e);

		}

	}

	public static void rollbackTransactionToPreviousSavePoint(Savepoint savepoint) throws TransactionException {

		try {
			logger.debug("Rolling back to previous save point " + savepoint.getSavepointName() + "...");
			conn.rollback(savepoint);
		} catch (SQLException e) {

			throw new TransactionException("Rollback to the savepoint failed!!!", e);

		}

	}
}

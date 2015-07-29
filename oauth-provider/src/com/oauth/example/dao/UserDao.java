/**
 * 
 */
package com.oauth.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.catalina.connector.Request;
import org.apache.log4j.Logger;

import com.oauth.example.dao.util.ConnectionUtil;
import com.oauth.example.exceptions.DatabaseConnectionException;
import com.oauth.example.exceptions.TransactionException;
import com.oauth.example.modal.Roles;
import com.oauth.example.modal.User;
import com.oauth.example.modal.UserCredentials;
import com.oauth.example.modal.UserRoles;

/**
 * @author DEEPAK
 *
 */
public class UserDao {

	private Connection conn = null;

	private PreparedStatement ps;

	private ResultSet result;

	private final String insertUser = "INSERT INTO EXAMPLE.USER_DETAILS(USER_NAME,USER_EMAIL) VALUES (?,?)";

	private final String insertUserCredentials = "INSERT INTO EXAMPLE.USER_CREDENTIALS(USER_ID,USER_NAME,PASSWORD) VALUES (?,?,?)";

	private final String insertUserRoles = "INSERT INTO EXAMPLE.USER_ROLES(USER_ID,USER_ROLE) VALUES (?,?)";

	private final String getAllUsers = "SELECT * FROM EXAMPLE.USER_DETAILS";

	private final String getUserwithId = "SELECT * FROM EXAMPLE.USER_DETAILS WHERE USER_ID = ?";

	private final String getUserwithName = "SELECT * FROM EXAMPLE.USER_DETAILS WHERE USER_NAME = ?";

	private final String getUserCredentialsWithUserName = "SELECT * FROM EXAMPLE.USER_CREDENTIALS WHERE USER_NAME = ? AND PASSWORD = ?";

	private final String getUserRole = "SELECT * FROM EXAMPLE.USER_ROLES WHERE USER_ID = ?";

	private final String getRole = "SELECT ROLE_NAME FROM EXAMPLE.ROLES WHERE ROLE_ID = ?";

	final static Logger logger = Logger.getLogger(AppDao.class);

	public User save(User user, UserCredentials credentials, UserRoles roles) {

		try {
			Integer userId = null;
			conn = ConnectionUtil.getDBConnectionToMySQL();
			logger.debug("creating user with username " + user.getUserName());

			ps = conn.prepareStatement(insertUser);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmailId());
			ps.executeUpdate();

			logger.debug("user Created!");

			ps.close();

			ps = conn.prepareStatement(getUserwithName);
			ps.setString(1, user.getUserName());

			result = ps.executeQuery();
			while (result.next()) {
				userId = result.getInt("USER_ID");
			}
			result.close();
			ps.close();

			logger.debug("saving user credentials...");
			ps = conn.prepareStatement(insertUserCredentials);
			ps.setInt(1, userId);
			ps.setString(2, user.getUserName());
			ps.setString(3, credentials.getPassword());
			ps.executeUpdate();
			logger.debug("user credentials saved.");

			ps.close();

			logger.debug("Assigning Roles to " + user.getUserName() + " ...");
			ps = conn.prepareStatement(insertUserRoles);
			ps.setInt(1, userId);
			ps.setInt(2, roles.getRoles().getId());
			ps.executeUpdate();
			logger.debug("Role " + roles.getRoles().getName() + " assigned.");

			user.setId(userId);

		} catch (DatabaseConnectionException e) {
			logger.debug("could not connect to the database...");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {

			logger.debug("Database error... rolling back transaction...");

			try {
				ConnectionUtil.rollbackTransaction();
			} catch (TransactionException e1) {

				logger.debug("rollback Failure! \n" + e1.getMessage());

			}

		} finally {
			try {
				ps.close();
				ConnectionUtil.closeDBConnection();
			} catch (DatabaseConnectionException e) {
				logger.debug("Error occurred!!! Could not close connection to database.");
				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return user;
	}

	public User getAuthorizedUser(String userName, String encPassword) {

		User user = null;
		try {
			String name = null;
			conn = ConnectionUtil.getDBConnectionToMySQL();
			ps = conn.prepareStatement(getUserCredentialsWithUserName);
			ps.setString(1, userName);
			ps.setString(2, encPassword);
			result = ps.executeQuery();
			while (result.next()) {
				name = result.getString("USER_NAME");
			}
			result.close();
			ps.close();
			if (name != null && name.equals(userName)) {

				ps = conn.prepareStatement(getUserwithName);
				ps.setString(1, userName);
				result = ps.executeQuery();
				while (result.next()) {
					logger.debug("Authorized user found... returning the user now!!!");
					user = new User();
					user.setId(result.getInt("USER_ID"));
					user.setUserName(result.getString("USER_NAME"));
					user.setEmailId(result.getString("USER_EMAIL"));
				}

			}
		} catch (DatabaseConnectionException e) {
			logger.debug("could not connect to the database...");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {

			logger.debug("Database error... rolling back transaction...");

			try {
				ConnectionUtil.rollbackTransaction();
			} catch (TransactionException e1) {

				logger.debug("rollback Failure! \n" + e1.getMessage());

			}

		} finally {
			try {
				ps.close();
				ConnectionUtil.closeDBConnection();
			} catch (DatabaseConnectionException e) {
				logger.debug("Error occurred!!! Could not close connection to database.");
				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return user;
	}

	public UserRoles getUserRole(User user) {
		UserRoles userRole = null;
		Integer roleId = null;
		String roleName = null;
		try {
			conn = ConnectionUtil.getDBConnectionToMySQL();
			ps = conn.prepareStatement(getUserRole);
			ps.setInt(1, user.getId());
			result = ps.executeQuery();
			while (result.next()) {

				roleId = result.getInt("USER_ROLE");
			}
			result.close();
			ps.close();

			ps = conn.prepareStatement(getRole);
			ps.setInt(1, roleId);
			result = ps.executeQuery();
			while (result.next()) {
				roleName = new String();
				roleName = result.getString("ROLE_NAME");

			}
			userRole = new UserRoles();
			userRole.setUser(user);
			userRole.setRoles(Roles.valueOf(roleName));

		} catch (DatabaseConnectionException e) {
			logger.debug("could not connect to the database...");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {

			logger.debug("Database error... rolling back transaction...");

			try {
				ConnectionUtil.rollbackTransaction();
			} catch (TransactionException e1) {

				logger.debug("rollback Failure! \n" + e1.getMessage());

			}

		} finally {
			try {
				ps.close();
				ConnectionUtil.closeDBConnection();
			} catch (DatabaseConnectionException e) {
				logger.debug("Error occurred!!! Could not close connection to database.");
				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return userRole;

	}

}

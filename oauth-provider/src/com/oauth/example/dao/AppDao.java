/**
 * 
 */
package com.oauth.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.oauth.example.crypto.Secret;
import com.oauth.example.dao.util.ConnectionUtil;
import com.oauth.example.exceptions.DatabaseConnectionException;
import com.oauth.example.exceptions.TransactionException;
import com.oauth.example.modal.App;

/**
 * @author DEEPAK
 *
 */
public class AppDao {

	private Connection conn = null;

	private PreparedStatement ps;

	private final String insertApp = "INSERT INTO EXAMPLE.APP(APP_ID,APP_NAME,APP_DESCRIPTION,APP_SECRET,CALLBACK_URL) VALUES (?,?,?,?,?)";

	final static Logger logger = Logger.getLogger(AppDao.class);

	public App save(App app) {
		App preparedApp = prepareApp(app);

		try {
			conn = ConnectionUtil.getDBConnectionToMySQL();
			logger.debug("creating app with id " + app.getAppId().toString());
			ps = conn.prepareStatement(insertApp);

			ps.setString(1, app.getAppId().toString());
			ps.setString(2, app.getAppName());
			ps.setString(3, app.getAppDescription());
			ps.setString(4, app.getAppSecret());
			ps.setString(5, app.getCallbackUrl().replace("\\", "\\\\"));

			ps.executeUpdate();
			logger.debug("App created!");
			logger.debug("service end point is: " + app.getCallbackUrl().replace("\\", "\\\\"));

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

		}
		return preparedApp;
	}

	/**
	 * @param app
	 */
	private App prepareApp(App app) {
		UUID uuid = null;
		uuid = generateId();
		app.setAppId(uuid);
		app.setAppSecret(Secret.generateSecretKey(app.getAppId().toString()));
		return app;
	}

	/**
	 * 
	 * @return
	 */
	private UUID generateId() {
		return UUID.randomUUID();
	}
}

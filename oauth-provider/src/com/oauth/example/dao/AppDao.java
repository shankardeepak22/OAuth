/**
 * 
 */
package com.oauth.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

	private ResultSet result;

	private final String insertApp = "INSERT INTO EXAMPLE.APP(APP_ID,APP_NAME,APP_DESCRIPTION,APP_SECRET,CALLBACK_URL) VALUES (?,?,?,?,?)";

	private final String getAllApps = "SELECT * FROM EXAMPLE.APP";

	final static Logger logger = Logger.getLogger(AppDao.class);

	public List<App> getAllConsumers() {
		List<App> apps = new ArrayList<>();

		try {
			conn = ConnectionUtil.getDBConnectionToMySQL();
			logger.debug("getting the details of all the applications\n");
			ps = conn.prepareStatement(getAllApps);
			result = ps.executeQuery();

			while (result.next()) {
				App app = new App();
				app.setAppId(UUID.fromString(result.getString("APP_ID")));
				app.setAppName(result.getString("APP_NAME"));
				app.setAppDescription(result.getString("APP_DESCRIPTION"));
				app.setAppSecret(result.getString("APP_SECRET"));
				app.setCallbackUrl(result.getString("CALLBACK_URL"));
				apps.add(app);

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

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return apps;
	}

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

		} finally {
			try {
				ps.close();
				ConnectionUtil.closeDBConnection();
			} catch (DatabaseConnectionException e) {
				logger.debug("Error occurred!!! Could not close connection to database.");
				e.printStackTrace();
			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
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

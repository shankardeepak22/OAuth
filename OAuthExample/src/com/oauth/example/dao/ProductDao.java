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

import org.apache.log4j.Logger;

import com.oauth.example.dao.util.ConnectionUtil;
import com.oauth.example.exceptions.DatabaseConnectionException;
import com.oauth.example.exceptions.TransactionException;
import com.oauth.example.modal.Product;

/**
 * @author DEEPAK
 *
 */
public class ProductDao {

	private Connection conn = null;

	private PreparedStatement ps;

	private ResultSet result;

	private final String insertProduct = "INSERT INTO CONSUMER.PRODUCTS(PRODUCT_NAME,PRODUCT_DESC) VALUES (?,?)";

	private final String getAllProducts = "SELECT * FROM CONSUMER.PRODUCTS";

	final static Logger logger = Logger.getLogger(ProductDao.class);

	public List<Product> getAllProducts() {

		List<Product> products = new ArrayList<>();

		try {
			conn = ConnectionUtil.getDBConnectionToMySQL();
			logger.debug("getting the details of all the Products\n");
			ps = conn.prepareStatement(getAllProducts);
			result = ps.executeQuery();
			while (result.next()) {
				Product product = new Product();
				product.setProductId(result.getInt("PRODUCT_ID"));
				product.setProductName(result.getString("PRODUCT_NAME"));
				product.setProductDescription(result.getString("PRODUCT_DESC"));
				products.add(product);
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

		return products;
	}

	public Product save(Product product) {
		try {
			conn = ConnectionUtil.getDBConnectionToMySQL();
			ps = conn.prepareStatement(insertProduct);
			ps.setString(1, product.getProductName());
			ps.setString(2, product.getProductDescription());
			ps.executeUpdate();

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
			return null;

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
				return null;
			}
		}

		return product;
	}
}

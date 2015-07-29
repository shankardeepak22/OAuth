package com.oauth.example.consumer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oauth.example.dao.ProductDao;
import com.oauth.example.modal.Product;

/**
 * Servlet implementation class AddProduct
 */
@WebServlet("/AddProduct")
public class AddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddProduct() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("username") != null && session.getAttribute("role") != null) {
			/* String user = session.getAttribute("username").toString(); */
			String role = session.getAttribute("role").toString();
			if (role.equals("ROLE_ADMIN")) {
				Product product;
				ProductDao productDao = new ProductDao();
				String productName = request.getParameter("productName");
				String productDescription = request.getParameter("productDesc");
				if (productName != null && productDescription != null && productName.length() > 0
						&& productDescription.length() > 0) {
					product = new Product();
					product.setProductName(productName);
					product.setProductDescription(productDescription);
					product = productDao.save(product);
					if (product != null)
						request.getRequestDispatcher("/welcomeUser.jsp?added=true").forward(request, response);
					else
						request.getRequestDispatcher("/welcomeUser.jsp?added=false").forward(request, response);

				}
			}
		} else {
			Logout logout = new Logout();
			logout.doGet(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

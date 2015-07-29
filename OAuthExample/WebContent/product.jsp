<%@page import="com.oauth.example.dao.ProductDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.oauth.example.modal.Product"%>
<%@page import="java.util.List"%>

<%
	String username = session.getAttribute("username").toString();
	if (username != null && username.length() > 0) {
		ProductDao productDao = new ProductDao();
		List<Product> products = new ArrayList<>();

		products = productDao.getAllProducts();
%>
<div
	class="jumbotron ">
	<table class="table table-hover table-bordered">
		<thead>
			<tr>
				<th>Product ID</th>
				<th>Product Name</th>
				<th>Product Description</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (Product product : products) {
						int productId = product.getProductId();
						String productName = product.getProductName();
						String producSDesc = product.getProductDescription();
			%>
			<tr>
				<th scope="row"><%=productId%></th>
				<td><%=productName%></td>
				<td><%=producSDesc%></td>
			</tr>

			<%
				}
				}
			%>


		</tbody>
	</table>
</div>

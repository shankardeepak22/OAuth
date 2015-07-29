<%@page import="com.oauth.example.consumer.Reset"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	if (session.getAttribute("username") != null && session.getAttribute("role") != null) {
		String user = session.getAttribute("username").toString();
		String role = session.getAttribute("role").toString();
%>

<!DOCTYPE html >
<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/colorgraph.css">
<link rel="stylesheet" type="text/css" href="css/font.css">
<link rel="stylesheet" type="text/css" href="css/jumbotron-narrow.css">
<link
	href='http://fonts.googleapis.com/css?family=Philosopher|Gudea|Pacifico|Dancing+Script|Lobster+Two'
	rel='stylesheet' type='text/css'>
<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<title>Welcome!</title>
</head>
<body>

	<div class="container">

		<div class="header clearfix">

			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed "
						style="border-style: solid; border-color: black;"
						data-toggle="collapse" data-target="#navbar" aria-expanded="false"
						aria-controls="navbar">
						<span class="glyphicon glyphicon-menu-hamburger"></span>
					</button>

				</div>

				<div id=navbar class="navbar-collapse collapse">
					<nav>
						<ul class="nav navbar-nav  pull-right">
							<li role="presentation"><a href="#"><%=user%></a></li>

							<%
								if (role.equals("ROLE_ADMIN")) {
							%>

							<li role="presentation"><button type="button"
									class="btn btn-success " data-toggle="modal"
									data-target="#addProduct">Add Product</button></li>
							<%
								}
							%>

							<li role="presentation"><a href="Logout">Logout</a></li>
						</ul>
					</nav>
					<h3>Example OAuth Application</h3>
				</div>
			</div>
		</div>





		<!-- Create App Modal -->
		<div id="addProduct" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h2 class="modal-title text-success">Add Your Product!</h2>
					</div>
					<div class="modal-body">
						<%@ include file="addProduct.jsp"%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>

		<%
			if (request.getAttribute("added") != null) {
					String productAdded = request.getAttribute("added").toString();
					if (productAdded != null && productAdded.equals("true")) {
		%>
		<div class="alert alert-info">
			<h3 align="center">Your Product was added!</h3>
		</div>
		<%
			} else if (productAdded != null && productAdded.equals("false")) {
		%>
		<div class="alert alert-danger">
			<h3 align="center">Product could not be added!</h3>
		</div>
		<%
			}
				}
		%>
		<div>
			<%@ include file="product.jsp"%>
		</div>



	</div>

	<%
		} else {
			Reset reset = new Reset();

			session.invalidate();
			reset.doGet(request, response);
		}
	%>


	<!-- /container -->


</body>
</html>
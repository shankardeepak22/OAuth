<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	String username = null;
	String role = null;
	if (session.getAttribute("username") != null) {
		if (session.getAttribute("role") != null) {
			username = session.getAttribute("username").toString();
			role = session.getAttribute("role").toString();
		}
	}
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
		<%
			String isNewUser = (String) request.getParameter("isNewRegUser");
		%>
		<%
			if (isNewUser != null && isNewUser.equals("true")) {
		%>

		<div class="alert alert-info">
			<h3 align="center">User Created!</h3>
		</div>
		<%
			} else if (isNewUser != null && isNewUser.equals("false")) {
		%>
		<div class="alert alert-danger">
			<h3 align="center">Could not Create User!</h3>
		</div>
		<%
			}
		%>
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
						<ul class="nav navbar-nav nav-pills pull-right">

							<%
								if (username != null) {
									if (role != null) {
							%>


							<li role="presentation"><a href="Logout">Logout</a></li>


							<%
								if (role.equals("ROLE_ADMIN")) {
							%>
							<li role="presentation"><button type="button"
									class="btn btn-info " data-toggle="modal"
									data-target="#createApp">Create App</button></li>
							<li role="presentation"><button type="button"
									class="btn btn-success " data-toggle="modal"
									data-target="#createAdmin">Create Admin</button></li>
							<%
								}
							%>
							<%
								}

								} else {
							%>
							<li role="presentation"><button type="button"
									class="btn btn-success " data-toggle="modal"
									data-target="#login">Login</button></li>

							<%
								}
							%>

							<li role="presentation"><button type="button"
									class="btn btn-warning " data-toggle="modal"
									data-target="#aboutOauth">About OAuth</button></li>
						</ul>
					</nav>
					<%
						if (username != null) {
					%>
					<h3>
						Welcome
						<%=username%></h3>
					<%
						} else {
					%>
					<h3>Sample Oauth Provider</h3>
					<%
						}
					%>

				</div>
			</div>
		</div>


		<!-- Create App Modal -->
		<div id="login" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h2 class="modal-title text-success">Welcome Back!</h2>
					</div>
					<div class="modal-body">
						<%@ include file="login.jsp"%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>


		<!-- Create App Modal -->
		<div id="createApp" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h2 class="modal-title text-success">Create New App!</h2>
					</div>
					<div class="modal-body">
						<%@ include file="createApp.jsp"%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>

		<!-- Create App Modal -->
		<div id="createAdmin" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h2 class="modal-title text-success">Create New App!</h2>
					</div>
					<div class="modal-body">
						<%@ include file="createAdmin.jsp"%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>


		<!-- about oauth modal content -->
		<div id="aboutOauth" class="modal fade" role="dialog">

			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title" align="center">About OAuth</h4>
					</div>
					<div class="modal-body">
						<%@ include file="about.jsp"%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>

		</div>

		<%
			if (session.getAttribute("username") == null) {
		%>
		<div>
			<%@ include file="createUser.jsp"%>
		</div>

		<%
			} else {
		%>
		<img alt="welcome" src="images/welcome2.jpg" height="100%"
			width="100%">
		<%
			}
		%>

	</div>




	<!-- /container -->


</body>
</html>
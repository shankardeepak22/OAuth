<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
<title>Sign In Error</title>
</head>
<body>

	<div class="container">
		<div class="alert alert-danger">
			<h3 align="center">Could not Create User!</h3>
		</div>

		<div class="jumbotron ">

			<form action="Login" method="post">
				<fieldset>


					<h2 class="text-success">Please Sign In</h2>
					<hr class="colorgraph">
					<div class="form-group">
						<input type="text" name="userName" id="username"
							required="required" class="form-control input-lg"
							placeholder="UserName">


					</div>
					<div class="form-group">
						<input type="password" name="password" id="password"
							required="required" class="form-control input-lg"
							placeholder="Password">
					</div>

					<hr class="colorgraph">
					<div class="row">
						<div class="">

							<%-- <input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> --%>
							<input type="submit" class="btn btn-lg btn-success btn-block"
								name="signIn" value="Sign In">
						</div>

					</div>
				</fieldset>
			</form>
		</div>

	</div>

</body>
</html>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
	String appDesc = (String) request.getAttribute("CONS_DESC");
	String token = (String) request.getAttribute("TOKEN");
	String callback = (String) request.getAttribute("CALLBACK");
	if (callback == null)
		callback = "";
%>

<!DOCTYPE HTML >

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Single Sign ON!</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/colorgraph.css">
<link rel="stylesheet" type="text/css" href="css/font.css">
<link
	href='http://fonts.googleapis.com/css?family=Philosopher|Gudea|Pacifico|Dancing+Script|Lobster+Two'
	rel='stylesheet' type='text/css'>
<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(this).bind("contextmenu", function(e) {
			e.preventDefault();
		});
	});
</script>
</head>
<body>
	<%--  <jsp:include page="banner.jsp"/> --%>
	<%
		if (appDesc != null && appDesc.length() > 0) {
	%>

	<div class="alert alert-info">
		<h3 align="center">
			<strong>"<%=appDesc%>"
			</strong> is trying to access your information.
		</h3>
	</div>
	<%
		}
	%>


	<div class="container">

		<div class="row" style="margin-top: 20px">


			<br> <br> <img class="pull-left" height="200" width="200"
				src="images/OAuthLogo.png" alt='OAuth Logo' />



			<div
				class="col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">

				<form action="authorize" method="post">
					<fieldset>


						<h2 class="text-success">Please Sign In</h2>
						<hr class="colorgraph">
						<div class="form-group">
							<input type="email" name="userId" id="username"
								class="form-control input-lg" placeholder="Email Address">

							<input type="hidden" name="oauth_token" value="<%=token%>" /> <input
								type="hidden" name="oauth_callback" value="<%=callback%>" />
						</div>
						<div class="form-group">
							<input type="password" name="password" id="password"
								class="form-control input-lg" placeholder="Password">
						</div>
						<span class="button-checkbox"> <a href=""
							class="btn btn-link pull-right">Forgot Password?</a><br>
						</span>
						<hr class="colorgraph">
						<div class="row">
							<div class="col-xs-6 col-sm-6 col-md-6">

								<%-- <input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> --%>
								<input type="submit" class="btn btn-lg btn-success btn-block"
									name="Authorize" value="Sign In">
							</div>
							<div class="col-xs-6 col-sm-6 col-md-6">
								<a href="" class="btn btn-lg btn-primary btn-block">Register</a>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>

	</div>



</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
	String appId = (String) request.getAttribute("appId");
	String appSecret = (String) request.getAttribute("appSecret");
	String callback = (String) request.getAttribute("appCallback");
	String appDesc = (String) request.getAttribute("appDesc");
	String appName = (String) request.getAttribute("appName");

	if (callback == null)
		callback = "";
%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>App Details</title>
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
	<%
		if (appId != null && appSecret != null) {
			if (appDesc != null && appName != null) {
	%>
	<br>
	<br>
	<br>
	<div class="container">
		<div class="well well-lg">
			<div class="row">
				<div class="col-md-6">
					<blockquote>
						<p>
							<%=appName%>
						</p>
						<footer>
							<%=appDesc%>
						</footer>
					</blockquote>

				</div>
				<div class="col-md-6">
					<p>App Id</p>
					</br>
					<code>
						"<%=appId%>"
					</code>

					<p>App Secret</p>
					</br>
					<code>
						"<%=appSecret%>"
					</code>

				</div>
			</div>
		</div>
	</div>

	<%
		}
		}
	%>

</body>
</html>
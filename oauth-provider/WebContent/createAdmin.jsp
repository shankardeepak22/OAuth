
<div class="jumbotron ">

	<h2 class="modal-title text-success" align="center">Sign Up Now!</h2>

	<form action="AdminCreationServlet" method="post">
		<fieldset>

			<hr class="colorgraph">

			<div class="form-group ">
				<input type="text" name="userName" id="username" required="required"
					class="form-control input-lg" placeholder="User Name">
			</div>

			<div class="form-group ">
				<input type="email" name="email" id="email"
					class="form-control input-lg" placeholder="Email ID">
			</div>

			<div class="form-group ">
				<input type="password" name="password" id="password"
					class="form-control input-lg" placeholder="password">
			</div>

			<hr class="colorgraph">
			<div class="row">
				<div>

					<input type="submit" class="btn btn-lg btn-success btn-block"
						name="Register" value="Sign UP!">
				</div>

			</div>
		</fieldset>
	</form>
</div>
<div class="jumbotron col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">

	<form action="Login" method="post">
		<fieldset>


			<h2 class="text-success">Please Sign In</h2>
			<hr class="colorgraph">
			<div class="form-group">
				<input type="text" name="userName" id="username" required="required"
					class="form-control input-lg" placeholder="UserName">


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
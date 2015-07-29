



<form action="AppCreationServlet" method="post">
	<fieldset>

		<hr class="colorgraph">

		<div class="form-group ">
			<input type="text" name="appName" id="appname" required="required"
				class="form-control input-lg" placeholder="App Name">
		</div>

		<div class="form-group ">
			<input type="text" name="appDesc" id="appdesc" required="required"
				class="form-control input-lg" placeholder="App Description">
		</div>

		<div class="form-group ">
			<input type="url" name="callbackUrl" id="callbackurl"
				class="form-control input-lg" placeholder="Call Back URL">
		</div>

		<hr class="colorgraph">
		<div class="row">
			<div class="col-xs-6 col-sm-6 col-md-6">

				<input type="submit" class="btn btn-lg btn-success btn-block"
					name="Authorize" value="Create App">
			</div>

		</div>
	</fieldset>
</form>





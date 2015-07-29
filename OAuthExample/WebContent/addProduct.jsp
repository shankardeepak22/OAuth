<div class="jumbotron ">

	<form action="AddProduct" method="post">
		<fieldset>



			<hr class="colorgraph">
			<div class="form-group">
				<input type="text" name="productName" id="productname"
					required="required" class="form-control input-lg"
					placeholder="Product Name">


			</div>
			<div class="form-group">
				<input type="text" name="productDesc" id="productdesc"
					required="required" class="form-control input-lg"
					placeholder="Product Description">
			</div>

			<hr class="colorgraph">
			<div class="row">
				<div class="">

					<%-- <input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" /> --%>
					<input type="submit" class="btn btn-lg btn-success btn-block"
						name="add" value="Add">
				</div>

			</div>
		</fieldset>
	</form>
</div>
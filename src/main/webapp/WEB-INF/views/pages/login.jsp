<div class="container" style="margin-top:30px">
	<div class='row'>
		<div class="col-sm-4"></div>
		<div class="col-sm-4">
		<h4>Login</h4>		
		<form action="login" method="post" id="join_form" name="frm">
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text">USER ID</span>    
			</div>			
			<input name="id" type="text" class="form-control">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-append">
				<span class="input-group-text">PASSWORD</span>
			</div>
			<input name="pwd" type="password" class="form-control">
			<button class="btn btn-primary" type="submit">Login</button>									
		</div>
		<div class="input-group mb-3">
			<div>
				<span>${message}</span>    
			</div>
		</div>
		</form>
		</div>
		<div class="col-sm-4"></div>		
	</div>
</div>
<script>
$(document).ready(function() {
	var user_name = '<%= request.getAttribute("user_name") %>';
	
	if(user_name == '' || user_name == 'null') {
		$("#loginout").empty();
		$("#loginout").append('<a href="/k_drive/login">' +
				'<span class="glyphicon glyphicon-log-in"></span>Login</a>');
	}
	else {
		$("#loginout").empty();
		$("#loginout").append('<a href="/k_drive/logout">' +
				'<span class="glyphicon glyphicon-log-in"></span>' + user_name + ' Logout</a>');
	}
});
</script>
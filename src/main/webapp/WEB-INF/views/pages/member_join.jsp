<div class="container" style="margin-top:30px">
	<div class='row'>
		<div class="col-sm-4"></div>
		<div class="col-sm-4">
		<h4>Join</h4>
		<p><span class="small">* Required.</span></p>
		<form action="member_join" method="post" id="join_form" name="frm">
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text">N A M E</span>    
			</div>			
			<input name="m_name" type="text" class="form-control">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text">USER ID</span>    
			</div>			
			<input name="m_userid" type="text" class="form-control">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-append">
				<span class="input-group-text">PASSWORD</span>
			</div>
			<input name="m_pwd" type="password" class="form-control">						
		</div>
		<div class="input-group mb-3">
			<div class="input-group-append">
				<span class="input-group-text">CHECKING</span>
			</div>
			<input name="pwd_check" type="password" class="form-control">						
		</div>
		<div class="input-group mb-3">
			<div class="input-group-append">
				<span class="input-group-text"> E-MAIL </span>
			</div>
			<input name="m_email" type="text" class="form-control" placeholder="E-Mail Address">						
		</div>
		<div class="input-group mb-3">
			<div class="input-group-append">
				<span class="input-group-text"> PHONE </span>
			</div>
			<input name="m_phone" type="text" class="form-control" placeholder="010-1234-5678">
			<button class="btn btn-primary" type="submit">Submit</button>						
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
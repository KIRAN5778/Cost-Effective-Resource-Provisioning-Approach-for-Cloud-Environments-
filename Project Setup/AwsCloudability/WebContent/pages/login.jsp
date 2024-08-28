<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../tiles/header.jsp"></jsp:include>
</head>
<body>

	<div class="page login-page">
		<div class="container">
			<div class="form-outer text-center d-flex align-items-center">
				<div class="form-inner">
					<div class="logo text-uppercase">
						<span>AWS</span><strong class="text-primary">Cloudability</strong>
					</div>

					<form id="contact-form" action="javascript:fnSubmit();"
						class="text-left form-validate">
						<div class="form-group-material">
							<input type="text" id="username" name="username" required
								data-msg="Please enter your username" class="input-material">
							<label for="login-username" class="label-material">Username</label>
						</div>
						<div class="form-group-material">
							<input type="password" id="pass" name="pass" required
								data-msg="Please enter your password" class="input-material">
							<label for="login-password" class="label-material">Password</label>
						</div>
						<div class="form-group text-center">
							<button type="submit" class="btn btn-primary">Login</button>
							<!-- This should be submit button but I replaced it with <a> for demo purposes-->
						</div>
					</form>
					<small>Do not have an account? </small><a
						class="signup"  value="Register" onclick="fnRegister()">Signup</a>
				</div>

			</div>
		</div>
		<jsp:include page="../tiles/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="../tiles/footer_inc.jsp"></jsp:include>
</body>
<script>

function fnRegister(){
	
	window.location.href='<%=request.getContextPath()%>/pages/register.jsp';     
	
}


function fnSubmit(){
	

	 var str = $( "#contact-form" ).serialize();
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=checkLogin",
			str,
			function(data) {
		data=$.trim(data);
		if(data=='false'){
			alert('Invalid Credentials. Please try again.');
			$('#contact-form')[0].reset();
		}else{
			window.location.href='<%=request.getContextPath()%>/pages/index.jsp';
							}

						});

	}
</script>
</html>
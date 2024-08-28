<%@page import="java.util.List"%>
<%@page import="com.constant.ServerConstants"%>
<%@page import="com.amazonaws.regions.Regions"%>
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
					<form class="text-left form-validate" id="contact-form" action="javascript:fnSubmit();">
						<div class="form-group-material">
							<input id="fname" type="text" name="fname"
								required data-msg="Please enter your first name"
								class="input-material"> <label for="register-username"
								class="label-material">First Name</label>
						</div>
						<div class="form-group-material">
							<input id="lname" type="text" name="lname"
								required data-msg="Please enter your last name"
								class="input-material"> <label for="register-username"
								class="label-material">Last Name</label>
						</div>
						
						<div class="form-group-material">
							<input id="phoneno" type="text" name="phoneno"
								required data-msg="Please enter your Mobile Number"
								class="input-material"> <label for="register-username"
								class="label-material">Contact Number</label>
						</div>
						<div class="form-group-material">
							<input id="username" type="text" name="username"
								required data-msg="Please enter your user name"
								class="input-material"> <label for="register-username"
								class="label-material">User Name</label>
						</div>
						
						<div class="form-group-material">
							<input id="userpass" type="password"
								name="userpass" required
								data-msg="Please enter your password" class="input-material">
							<label for="register-password" class="label-material">Password
							</label>
						</div>
						<div class="form-group-material">
							<input id="cuserpass" type="password"
								name="cuserpass" required
								data-msg="Please enter your password" class="input-material">
							<label for="register-password" class="label-material">Re-Enter Password
							</label>
						</div>
						<div class="form-group-material">
							<input id="accessKey" type="text" name="accessKey"
								required data-msg="Please enter your user name"
								class="input-material"> <label for="register-username"
								class="label-material">Access Key</label>
						</div>
						<div class="form-group-material">
							<input id="accessSecret" type="text" name="accessSecret"
								required data-msg="Please enter your user name"
								class="input-material"> <label for="register-username"
								class="label-material">Access Secret</label>
						</div>
						
						<div class="form-group text-center">
							<input id="register" type="submit" value="Register"
								class="btn btn-primary">
						</div>
					</form>
					<small>Already have an account? </small><a onclick="fnLogin()"
						value="Login" class="signup">Login</a>
				</div>
			
			</div>
		</div>

<jsp:include page="../tiles/footer.jsp"></jsp:include>

	</div>
	<jsp:include page="../tiles/footer_inc.jsp"></jsp:include>
</body>
<script>

function fnLogin(){
	
	window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
	
}

function fnSubmit(){
	
	if($('#userpass').val()!=$('#cuserpass').val()){
		alert('Password and confirm password do not match!');
		return;
	}
	
	 var str = $( "#contact-form" ).serialize();
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=registerNewUser",
			str,
			function(data) {
data=$.trim(data);  
if(data.indexOf('Successfully')!=-1){
	alert(data);
	$('#contact-form')[0].reset();
	window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
							} else {
								alert(data);
							}

						});

	}
</script>
</html>
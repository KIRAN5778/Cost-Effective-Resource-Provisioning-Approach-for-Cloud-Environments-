<%@page import="com.amazonaws.services.ec2.model.Volume"%>
<%@page import="com.helper.InstanceUserModel"%>
<%@page import="com.amazonaws.services.identitymanagement.model.User"%>
<%@page import="com.helper.GraphModel"%>
<%@page import="com.database.ConnectionManager"%>
<%@page import="com.amazonaws.services.ec2.model.InstanceState"%>
<%@page import="com.appdynamics.extensions.cloudwatch.InstanceModel"%>
<%@page import="com.appdynamics.extensions.cloudwatch.FetchAwsInstances"%>
<%@page import="com.helper.UserModel"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../tiles/header.jsp"></jsp:include>
</head>
<body>
	<%
		UserModel um = null;
		if (session.getAttribute("USER_MODEL") == null) {
	%>
	<script>                  
		window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
	</script>
	<%
		} else {
			um = (UserModel) session.getAttribute("USER_MODEL");
		}
	%>
	<jsp:include page="../tiles/leftmenu.jsp"></jsp:include>
	<div class="page">
		<jsp:include page="../tiles/topmenu.jsp"></jsp:include>
		<div class="breadcrumb-holder">
			<div class="container-fluid">
				<ul class="breadcrumb">
					<li class="breadcrumb-item active"><a href="index.html">Home</a></li>

				</ul>
			</div>
		</div>

	</div>
	
	<div class="row justify-content-center">
		<h2 class="text-center">Welcome to Cloudability System</h2>
	</div>
</body>



</html>
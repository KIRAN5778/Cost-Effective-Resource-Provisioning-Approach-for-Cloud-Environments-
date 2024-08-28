<%@page import="com.amazonaws.services.ec2.model.Instance"%>
<%@page import="com.helper.InstanceUserModel"%>
<%@page import="com.amazonaws.services.ec2.model.InstanceState"%>
<%@page import="com.appdynamics.extensions.cloudwatch.FetchAwsInstances"%>
<%@page import="com.appdynamics.extensions.cloudwatch.InstanceModel"%>
<%@page import="com.helper.UserModel"%>
<%@page import="com.database.ConnectionManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../tiles/header.jsp"></jsp:include>
</head>
<%
	String name = "User";
	UserModel um = (UserModel) session.getAttribute("USER_MODEL");
	String userid = um.getUserid();
	if (um != null) {
%>
<body>
	<jsp:include page="../tiles/leftmenu.jsp"></jsp:include>
	<div class="page">
		<jsp:include page="../tiles/topmenu.jsp"></jsp:include>
		<div class="breadcrumb-holder">
			<div class="container-fluid">
				<ul class="breadcrumb">
					<li class="breadcrumb-item"><a href="index.html">Home</a></li>
					<li class="breadcrumb-item active">Amazone Ec2</li>
				</ul>
			</div>
			<section>
				<div class="container-fluid">
					<!-- Page Header-->
					<header>
						<h1 class="h3 display">AWS</h1>
					</header>
					<div class="row">
						<div class="col-lg-12">
							<div class="card">
								<div class="card-header">
									<h4>EC2 Instance Across All Regions</h4>
								</div>
								<div class="card-body">
									<div class="table-responsive">
										<table class="table table-bordered ">

											<tr>
												<th>Sr.No</th>
												<th>InstanceId</th>
												<th>InstanceType</th>
												<th>Region</th>
												<th>State</th>

												<th>Public DNS (IPv4)</th>
												<th>IPv4 Public IP</th>

												<th>Key Name</th>

												<th>Analysis</th>





												<%
													//FetchAwsInstances fi = FetchAwsInstances.getInstanceList(session, um);

														List<InstanceUserModel> instances = ConnectionManager
																.getUserWiseNode(um.getUserid());
														int i = 0;
														System.err
																.println("-------------------------------------------------------");
														for (InstanceUserModel ins : instances) {
															i = i + 1;
												%>
											
											<tr title="<%=i%>">

												<td><%=i%></td>
												<td><%=ins.getInstanceId()%></td>
												<td><%=ins.getInstanceType()%></td>
												<td><%=ins.getRegion()%></td>
												<td><%=ins.getState()%></td>

												<td><%=ins.getPublicDnsName()%></td>
												<td><%=ins.getPrivateIpAddress()%></td>
												<%-- 												<td><%= "-"%></td> --%>
												<td><%=ins.getKeyName()%></td>
												<%-- 												<td><%= ins.getInstance().getMonitoring()%></td> --%>
												<td><a
													href='<%=request.getContextPath()%>/pages/amazon_ec2_instances_analysis.jsp?ec2=<%=i%>'>Analysis</a></td>
												<%-- 												<td class="text-primary"><%="launch-wizard-2" %></td> --%>
											</tr>
											<%
												}
											%>

										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>

		</div>
		<jsp:include page="../tiles/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="../tiles/footer_inc.jsp"></jsp:include>
</body>
<%
	} else {
%>
<jsp:include page="../pages/login.jsp"></jsp:include>
<%
	}
%>
</html>
<%@page import="com.database.ConnectionManager"%>
<%@page import="com.helper.UserModel"%>
<%@page import="com.amazonaws.services.ec2.model.Volume"%>
<%@page import="com.appdynamics.extensions.cloudwatch.InstanceModel"%>
<%@page import="com.appdynamics.extensions.cloudwatch.FetchAwsInstances"%>
<%@page import="com.amazonaws.services.ec2.model.Instance"%>
<%@page import="com.amazonaws.services.ec2.model.Reservation"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../tiles/header.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../tiles/leftmenu.jsp"></jsp:include>
	<div class="page">
	<jsp:include page="../tiles/topmenu.jsp"></jsp:include>
		<%
			UserModel um=null;
			if(session.getAttribute("USER_MODEL")==null){
		%>
		<script>
		window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
		</script>
		<%
			} else {
				um = (UserModel) session.getAttribute("USER_MODEL");
			}
		%>
		<div class="breadcrumb-holder">
			<div class="container-fluid">
				<ul class="breadcrumb">
					<li class="breadcrumb-item"><a href="index.html">Home</a></li>
					<li class="breadcrumb-item active">Amazone Volumes</li>
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
									<h4>Volumes Used By Account</h4>
								</div>
								<div class="card-body">
									<div class="table-responsive">

										<table class="table table-bordered">
											<tr>
												<th>Volume Id</th>
												<th>Region</th>
												<th>Volume Type</th>
												<th>Volume Size</th>
												<th>Attached Instance Id</th>


												<th>Status</th>

												<th>View Utilization</th>
											</tr>




											<%
												int i = 0;
												List<Volume> volumeList = ConnectionManager.getVolumes(um
														.getUserid());
												System.err
														.println("-------------------------------------------------------");

												if (volumeList != null && volumeList.size() > 0) {
													for (Volume ins : volumeList) {
														i = i + 1;
											%>
											<tr title="<%=i%>">


												<td><%=ins.getVolumeId()%></td>
												<td><%=ins.getAvailabilityZone()%></td>
												<td><%=ins.getVolumeType()%></td>
												<td><%=ins.getSize()%></td>
												<%
													if (ins.getAttachments() != null
																	&& ins.getAttachments().size() > 0) {
												%>
												<td><%=ins.getAttachments().get(0).getInstanceId()%></td>
												<%
													} else {
												%>
												<td>&nbsp;</td>
												<%
													}
												%>

												<td><%=ins.getState()%></td>
												<th><a
													href='<%=request.getContextPath()%>/pages/amazon_ec2_instances_analysis.jsp?ebs=<%=i%>'>View
														Utilization</a></th>
											</tr>
											<%
												}
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
</html>
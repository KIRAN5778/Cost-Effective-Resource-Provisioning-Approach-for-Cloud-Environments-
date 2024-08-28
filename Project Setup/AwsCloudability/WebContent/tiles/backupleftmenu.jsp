 <%@page import="com.helper.UserModel"%>
<nav class="side-navbar mCustomScrollbar _mCS_1">

<%
	UserModel um=null;
	if(session.getAttribute("USER_MODEL")==null){ %>
		<script>
		window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
		</script><%
}else{
	um=(UserModel)session.getAttribute("USER_MODEL");
}
    %>
         
	<div id="mCSB_1"
		class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside"
		style="max-height: none;" tabindex="0">
		<div id="mCSB_1_container" class="mCSB_container"
			style="position: relative; top: 0; left: 0;" dir="ltr">
			<div class="side-navbar-wrapper">
				<!-- Sidebar Header    -->
				<div
					class="sidenav-header d-flex align-items-center justify-content-center">
					<!-- User Info-->
					<div class="sidenav-header-inner text-center">
<!-- 						<img src="img/avatar-7.jpg" alt="person" -->
<!-- 							class="img-fluid rounded-circle mCS_img_loaded"> -->
							 <%if(um!=null){ %>
						<h2 class="h5"> 
						   <%if(um!=null){ %>
                   Hello <%=um.getUsername() %></h2>
                      <%}else{ %>  
               	<h2 class="h5">  Hello Guest </h2> 
                    <%} %>
						<span> <%=um.getRegion() %>
                    <%} %></span>
					</div>
					<!-- Small Brand information, appears on minimized sidebar-->
					<div class="sidenav-header-logo">
						<a href="index.html" class="brand-small text-center"> <strong>B</strong><strong
							class="text-primary">D</strong></a>
					</div>
				</div>
				<!-- Sidebar Navigation Menus-->
				<div class="main-menu">
					<h5 class="sidenav-heading">Main</h5>
					<ul id="side-main-menu" class="side-menu list-unstyled">
<%-- 						<li><a href="<%=request.getContextPath()%>/pages/index.jsp">  <i class="icon-home"></i>Dash board --%>
<!-- 						</a></li> -->
<%-- 						<li><a href="<%=request.getContextPath()%>/pages/amazon_ec2_instances.jsp"> <i class="icon-form"></i>Amazon EC2 --%>
<!-- 						</a></li> -->
<%-- 						<li><a href="<%=request.getContextPath()%>/pages/amazon_ec2_volumes.jsp"> <i class="fa fa-bar-chart"></i>Volumes --%>
<!-- 						</a></li> -->
<%-- 						<li><a href="<%=request.getContextPath()%>/pages/amazon_ec2_instances_analysis.jsp"> <i class="icon-grid"></i>Overvall Usage --%>
<!-- 						</a></li> -->
						
				
					</ul>
				</div>
			
			</div>
		</div>
		<div id="mCSB_1_scrollbar_vertical"
			class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical"
			style="display: block;">
			<div class="mCSB_draggerContainer">
				<div id="mCSB_1_dragger_vertical" class="mCSB_dragger"
					style="position: absolute; min-height: 30px; height: 330px; top: 0px; display: block; max-height: 479px;">
					<div class="mCSB_dragger_bar" style="line-height: 30px;"></div>
				</div>
				<div class="mCSB_draggerRail"></div>
			</div>
		</div>
	</div>
</nav>
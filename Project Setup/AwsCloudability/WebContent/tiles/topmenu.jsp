<header class="header">
	<nav class="navbar">
		<div class="container-fluid">
			<div
				class="navbar-holder d-flex align-items-center justify-content-between">
				<div class="navbar-header">
					<a id="toggle-btn" href="#" class="menu-btn"><i
						class="icon-bars"> </i></a><a href="index.html" class="navbar-brand">
						<div class="brand-text d-none d-md-inline-block">
							<span>AWS</span><strong class="text-primary">Cloudability</strong>
						</div>
					</a>
				</div>

				<ul
					class="nav-menu list-unstyled d-flex flex-md-row align-items-md-center">


					<!-- Log out-->
					<li class="nav-item"><a href="<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=logout"
						class="nav-link logout"> <span
							class="d-none d-sm-inline-block">Logout</span><i
							class="fa fa-sign-out"></i></a></li>
				</ul>
			</div>

		</div>
	</nav>
</header>
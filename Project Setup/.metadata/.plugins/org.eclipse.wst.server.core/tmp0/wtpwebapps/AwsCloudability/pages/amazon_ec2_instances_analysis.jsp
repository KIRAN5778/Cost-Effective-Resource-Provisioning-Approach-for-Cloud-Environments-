<%@page import="com.database.ConnectionManager"%>
<%@page import="com.helper.InstanceUserModel"%>
<%@page import="com.helper.UserModel"%>
<%@page import="com.helper.StringHelper"%>
<%@page import="com.constant.ServerConstants"%>

<%@page import="com.helper.StringHelper"%>
<%@page import="com.amazonaws.services.ec2.model.InstanceState"%>
<%@page import="com.appdynamics.extensions.cloudwatch.InstanceModel"%>
<%@page import="com.amazonaws.services.ec2.model.Volume"%>

<%@page import="com.appdynamics.extensions.cloudwatch.FetchAwsInstances"%>
<%@page import="com.constant.ServerConstants"%>
<%@page import="com.helper.UserModel"%>
<%@page import="java.util.*"%>
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

<%
	int i=1;
%>
<script
	src="<%=request.getContextPath()%>/theme/js/Chart.js"></script>
<body>
	<jsp:include page="../tiles/leftmenu.jsp"></jsp:include>
	<div class="page">
		<%
			UserModel um1=null;
			if(session.getAttribute("USER_MODEL")==null){
		%>
		<script>
		window.location.href='<%=request.getContextPath()%>/pages/login.jsp';
		</script>
		<%
			}else{
			um1=(UserModel)session.getAttribute("USER_MODEL");
				}
				 
			List<InstanceUserModel> instances= ConnectionManager.getUserWiseNode(um1.getUserid());
			List<Volume> volumeList =ConnectionManager.getVolumes(um1.getUserid());
		%>
		<jsp:include page="../tiles/topmenu.jsp"></jsp:include>
		<div class="breadcrumb-holder">
			<div class="container-fluid">
				<ul class="breadcrumb">
					<li class="breadcrumb-item"><a href="index.html">Home</a></li>
					<li class="breadcrumb-item active">Over All Usage</li>
				</ul>
			</div>
		</div>
		<section class="dashboard-header section-padding">
			<div class="container-fluid">
				<div class="row d-flex align-items-md-stretch">
				<div class="col-sm-3">
					<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>EC2 Instances List</h4>
						</div>
						<div class="card-body">
							<select name="EC2Instances" id="EC2Instances"
								class="form-control">
								<option value="">Select</option>
								<%
									for (InstanceUserModel ins: instances) {
																																																																																									             i=i+1;
																																																																																									             
																																																																																									             String status="";
																																																																																									             try{
																																																																																									            	 status=ins.getState();
																																																																																									            
																																																																																									             }catch(Exception e){
																																																																																									            	 
																																																																																									             }
								%>
								<option value="<%=ins.getInstanceId()+"#"+ins.getRegion()%>">
									<%=ins.getInstanceId()+"-"+ins.getRegion()+"-"+status%></option>
								<%
									}
								%>
							</select>

						</div>
					
					</div>
					</div>
					<div class="col-sm-3">
					<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>EC2 Matrix</h4>
						</div>
						<div class="card-body">
						<select name="EC2Matrix" id="EC2Matrix"
																onchange="$('#EBSMatrix').val('');$('#EBSInstances').val('');"
																class="form-control">
																<option value="">Select</option>
																<%
																	for ( i = 0; i < ServerConstants.EC2_MATRIX.length; i++) {
																%>
																<option value="<%=ServerConstants.EC2_MATRIX[i]%>">
																	<%=ServerConstants.EC2_MATRIX[i]%></option>
																<%
																	}
																%>
															</select>
						
						</div>
						</div>
						</div>
						<div class="col-sm-3">
						<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>EC2 Matrix</h4>
						</div>
							<input type="hidden" name="instanceId" id="instanceId"
														value="<%=request.getParameter("instanceId")%>">
						<div class="card-body">
						<input  type="radio"   name="TimeFilter" id="TimeFilter"
																value="day">
																<label for="day">Days</label>
						
						</div>
						</div>
						</div>
						<div class="col-sm-3">
						<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>Time Filter</h4>
						</div>
						<div class="card-body">
						<input type="radio" name="TimeFilterValue"
															id="TimeFilterValue" value="1"><label for="1">1</label>&nbsp;
															<input
															type="radio" name="TimeFilterValue" id="TimeFilterValue"
															value="3"><label for="3">3</label>&nbsp; <input type="radio"
															name="TimeFilterValue" id="TimeFilterValue" value="5"><label for="5">5</label>&nbsp;
															<input
															type="radio" name="TimeFilterValue" id="TimeFilterValue"
															value="10"><label for="10">10</label>&nbsp; <input type="button" id=btn
															name="btn" onclick="fnGenerateGraphs();" value="Search"
															class="btn btn-primary pull-right">
					</div>
					</div>
					</div>
					


				</div>
			
				<div class="row d-flex align-items-md-stretch">
				<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>EBS List</h4>
						</div>
						<div class="card-body">
						<select name="EBSInstances" id="EBSInstances"
																class="form-control">
																<option value="">Select</option>
																<%
																	System.err.println("-------------------------------------------------------");
																																																																																			    	for (Volume volume:volumeList) {
																%>
																<option
																	value="<%=volume.getVolumeId()+"#"+volume.getAvailabilityZone()%>">
																	<%=volume.getVolumeId()+"-"+volume.getAvailabilityZone()+"-"+volume.getSize()%></option>
																<%
																	}
																%>
															</select>
						
						</div>
						</div>
						<div class="card">
						<div class="card-header d-flex align-items-center">
							<h4>Volume Matrix </h4>
						</div>
						<div class="card-body">
							<select name="EBSMatrix" id="EBSMatrix"
																onchange="$('#EC2Matrix').val('');$('#EC2Instances').val('');"
																class="form-control">
																<option value="">Select</option>
																<%
																	for ( i = 0; i < ServerConstants.EBS_MATRIX.length; i++) {
																%>
																<option value="<%=ServerConstants.EBS_MATRIX[i]%>">
																	<%=ServerConstants.EBS_MATRIX[i]%></option>
																<%
																	}
																%>
															</select>
						
						</div>
						</div>
						
						
				</div>
				<div class="row">
				<div class="col-md-8">
				<div class="card">
							<div class="card-header">
								<h4>Chart</h4>
							</div>
							<div class="card-body">
						<canvas id="lineChart"></canvas>
						</div>
						</div>
						</div>
						<div class="col-md-4">
					<div class="card">
						
						<div class="card-body">
	<div class="stats" id="tableId">
												
												</div>
				</div>
			</div>
			</div>
			</div>
			<div class="row d-flex align-items-md-stretch">
			<div class="col-sm-12">
			<div class="card">
												<h4 class="title">AWS Matrix Chart</h4>
												<p class="category">
													<marquee>
														<span class="text-success" id="tipId"
															style="font-size: 16px;  color: black; text-transform: capitalize;"><i
															class="fa fa-long-arrow-up"></i> </span>
													</marquee>
												</p>
											</div>
											</div>
											</div>
											</div>
		</section>

		<jsp:include page="../tiles/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="../tiles/footer_inc.jsp"></jsp:include>
</body>
<%
	String name = "User";
		UserModel um = (UserModel) session.getAttribute("USER_MODEL");
		if (um != null) {
	name = um.getFname();
		}
		
		String ebs=StringHelper.n2s(request.getParameter("ebs"));
		String ec2=StringHelper.n2s(request.getParameter("ec2"));
%>

<%
	if(ebs.length()>0){
%>
<script>
		document.getElementById('EBSInstances').selectedIndex=<%=ebs%>;
		document.getElementById('EBSMatrix').selectedIndex=1;
	</script>
<%
	}
%>

<%
	if(ec2.length()>0){
%>
<script>
		document.getElementById('EC2Matrix').selectedIndex=<%=ec2%>;
		document.getElementById('EC2Instances').selectedIndex=1;
	</script>
<%
	}
%>

<!-- Material Dashboard DEMO methods, don't include it in your project! -->
<script src="<%=request.getContextPath()%>/theme/js/demo.js"></script>
<script type="text/javascript">
$(document).ready(function() {

    // Javascript method's body can be found in assets/js/demos.js
  //  fnLoadGraph();

});


function fnGenerateGraphs(){
	
	ec2=$('#EC2Matrix').val();
	ebs=$('#EBSMatrix').val();
	params='';
	
	
	if(ec2.length>0){
		ec2Instance=$('#EC2Instances').val();

		tokens=ec2Instance.split("#");
		instance_id=tokens[0];
		instance_region=tokens[1];

		params+='parameterName='+ec2+"&parameterType=AWS/EC2&region="+instance_region.toLowerCase()+"&InstanceId="+instance_id;	
	}else if(ebs.length>0){
		ebsInstance=$('#EBSInstances').val();
		tokens=ebsInstance.split("#");
		instance_id=tokens[0];
		instance_region=tokens[1];


		params+='parameterName='+ebs+"&parameterType=AWS/EBS&region="+instance_region.toLowerCase()+"&VolumeId="+instance_id;	
	}
	//$("input[id='TimeFilterValue']:checked").val();
	//$("input[id='TimeFilter']:checked").val();
	
	//params+='&TimeFilterValue='+$('#TimeFilterValue').val()+"&TimeFilter="+$('#TimeFilter').val();	
	params+='&TimeFilterValue='+$("input[id='TimeFilterValue']:checked").val()+"&TimeFilter="+$("input[id='TimeFilter']:checked").val();	
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=generateGraph",
						params,
						function(data) {
							data = JSON.parse($.trim(data));

							object = data[0];
							console.log(object);
							//tokens = data.split('#');
							labels = object['event_date'].split(',');

							vals = object['tagValue'].split(',');
							//$('#tipId').html(tokens[2]);
							var tr = '<table class="table" style="font-size:16px;background-color:pink;color:black;"><thead class="text-primary"><tr><td>Sr.No</td><td>Date</td><td>% Usage</td></tr></thead><tbody>';
							for (i = 0; i < labels.length; i++)
								tr += '<tr><td>' + (i + 1) + '</td><td>'
										+ labels[i] + '</td><td>' + vals[i]
										+ '</td></tr>';

							tr += '</tbody></table>';
							$('#tableId').html('<i class=""></i>' + tr);
							fnLoadGraph(labels, vals);

						});

	}

	function getMax(a) {
		max = -999;
		for (i = 0; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}
	function fnLoadGraph(labels, values) {
		/*  dataDailySalesChart = {
		          labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
		          series: [
		              [12, 17, 7, 17, 23, 18, 38]
		          ]
		      };
		 */

		dataDailySalesChart = {
			labels : labels,
			series : [ values ]
		};

		let
		b = values.map(function(item) {
			return parseFloat(item);
		});
		val = getMax(b);
		val = val + val * 0.2;
		console.log(b);
		console.log(val);
		let
		areaChartData = {
			labels : labels,
			datasets : [ {
				label : 'Performance Matrix',
				fillColor : 'rgba(100, 214, 122, 1)',
				strokeColor : 'rgba(110, 214, 222, 1)',
				pointColor : 'rgba(210, 214, 222, 1)',
				pointStrokeColor : '#c1c7d1',
				pointHighlightFill : '#fff',
				pointHighlightStroke : 'rgba(220,220,220,1)',
				data : values
			} ]
		};
		let
		areaChartOptions = {
			//Boolean - If we should show the scale at all
			showScale : true,
			//Boolean - Whether grid lines are shown across the chart
			scaleShowGridLines : false,
			//String - Colour of the grid lines
			scaleGridLineColor : 'rgba(0,0,0,.05)',
			//Number - Width of the grid lines
			scaleGridLineWidth : 1,
			//Boolean - Whether to show horizontal lines (except X axis)
			scaleShowHorizontalLines : true,
			//Boolean - Whether to show vertical lines (except Y axis)
			scaleShowVerticalLines : true,
			//Boolean - Whether the line is curved between points
			bezierCurve : true,
			//Number - Tension of the bezier curve between points
			bezierCurveTension : 0.3,
			//Boolean - Whether to show a dot for each point
			pointDot : false,
			//Number - Radius of each point dot in pixels
			pointDotRadius : 4,
			//Number - Pixel width of point dot stroke
			pointDotStrokeWidth : 1,
			//Number - amount extra to add to the radius to cater for hit detection outside the drawn point
			pointHitDetectionRadius : 20,
			//Boolean - Whether to show a stroke for datasets
			datasetStroke : true,
			//Number - Pixel width of dataset stroke
			datasetStrokeWidth : 2,
			//Boolean - Whether to fill the dataset with a color
			datasetFill : true,
			//String - A legend template

			//Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
			maintainAspectRatio : true,
			//Boolean - whether to make the chart responsive to window resizing
			responsive : true,
			showTooltips : false
		};
		
		  var lineChartCanvas = $('#lineChart').get(0).getContext('2d')
          var lineChart = new Chart(lineChartCanvas,{
              type: 'line',
              data: areaChartData,
              options: lineChartOptions
          })
          var lineChartOptions = areaChartOptions
          lineChartOptions.datasetFill = false
         // lineChart.Line(areaChartData, lineChartOptions)
	}

	function fnGenerateGraph() {
		var chart = new Chartist.Line('.ct-chart', {
			labels : [ 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ],
			series : [ [ 1, 5, 2, 5, 4, 3 ], [ 2, 3, 4, 8, 1, 2 ],
					[ 5, 4, 3, 2, 1, 0.5 ] ]
		}, {
			low : 0,
			showArea : true,
			showPoint : false,
			fullWidth : true
		});

		chart.on('draw', function(data) {
			if (data.type === 'line' || data.type === 'area') {
				data.element.animate({
					d : {
						begin : 2000 * data.index,
						dur : 2000,
						from : data.path.clone().scale(1, 0).translate(0,
								data.chartRect.height()).stringify(),
						to : data.path.clone().stringify(),
						easing : Chartist.Svg.Easing.easeOutQuint
					}
				});
			}
		});
	}
</script>


</html>
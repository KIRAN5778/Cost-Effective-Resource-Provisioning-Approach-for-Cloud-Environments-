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
		<section class="charts">
			<div class="container-fluid">
				<!-- Page Header-->
				<header>
					<h1 class="h3 display">Dashboard</h1>
				</header>
				<div class="row">
					<div class="col-lg-6">
						<div class="card line-chart-example">
							<div class="card-header d-flex align-items-center">
								<h4>CPU Utilization All Nodes</h4>
							</div>
							<div class="card-body">
								   <canvas id="dailySalesChart"></canvas>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="card bar-chart-example">
							<div class="card-header d-flex align-items-center">
								<h4>EBS MemoryReads NodeWise</h4>
							</div>
							<div class="card-body">
							 <canvas id="lineChart"></canvas>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="card pie-chart-example">
							<div class="card-header d-flex align-items-center">
								<h4>Decision</h4>
							</div>
							<div class="card-body">
								<div class="chart-container table-responsive">
									<table class="table table-bordered">
                   <thead>
                          <tr>
                             <th>srNo</th>
                             <th>Instance ID</th>
                             <th>Last Use </th>
                             <th>CPU</th>
                             <th>Network</th>
                             <th>Volume</th>
                             <th>Decision</th>
                             </tr>      
                   </thead>
                   
                   <% 
                    List l=ConnectionManager.getDecisionData(um.getUserid());  
                      for(int i=0;i<l.size();i++)
                      {   
                    	 GraphModel gm=(GraphModel)l.get(i);   
                	   %>
                       <tbody>
                          <tr>
                            <td><%=i+1%></td>
                            <td><%=gm.getInstanceId()%></td>
                            <td><%=gm.getDates()%></td>
                             <%double cpu=gm.getCpu();
                               double cpu1 =(double) Math.round(cpu * 100)/100;
                             %>
                             <td><%=cpu1%></td>
                             <%double net=gm.getNet();
                               double net1 =(double) Math.round(net * 1)/1;
                             %>	
                             <td><%=net1%></td>
                              <%double vol=gm.getVal();
                                double vol1 =(double) Math.round(vol * 1)/1;
                             %>
                              <td><%=vol1%></td>
                               <% if(gm.getDecision().equalsIgnoreCase("Deallocate")){ %>
                              <td><span class="label label-danger" style="font-size: 14px;"><%=gm.getDecision()%></span></td>
                              <%} else{%>
                             <td><span class="label label-success"" style="font-size: 14px;"><%=gm.getDecision()%></span></td>
                               <%} %>
                          </tr>
                       </tbody>
                   <%}%>
                  </table>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="card polar-chart-example">
							<div class="card-header d-flex align-items-center">
								<h4>EC2 Distribution</h4>
							</div>
							<div class="card-body">
								<div class="chart-container">
								    <canvas id="doughnut-chart"></canvas> 
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</section>
		<jsp:include page="../tiles/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="../tiles/footer_inc.jsp"></jsp:include>

<%
	List<InstanceUserModel> instances= ConnectionManager.getUserWiseNode(um.getUserid());
	List<Volume> volumeList =ConnectionManager.getVolumes(um.getUserid());
  %>
<select name="EBSInstances" id="EBSInstances"  style="display: none;"
															class="form-control">
															<option value="">Select</option>
															<%
															 int i=1;
															
															
														
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
														<select name="EC2Instances" id="EC2Instances" style="display: none;" 
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
															<option
																value="<%=ins.getInstanceId()+"#"+ins.getRegion()%>">
																<%=ins.getInstanceId()+"-"+ins.getRegion()+"-"+status%></option>
															<%
																}
															%>
														</select>
														</body>
 	<script>
														cpuLabels=[];
														cpuData=[];
														ebsLabels=[];
														ebsData=[];
														</script>
											
														<%
														List<HashMap> cpuData=ConnectionManager.getGraphDBData(um.getUserid(), "CPUUtilization", 10,"%d-%b");
														
														for( i=0;i<cpuData.size();i++){
															HashMap data=(HashMap)cpuData.get(i);
															//String[] labels=dataArray[0].split(",");
															//String[] values=dataArray[1].split(",");
															%>
															<script>
															cpuLabels='<%=data.get("event_date")%>'.split(',');
											   				cpuData.push([<%=data.get("tagValue")%>]);
															</script>
															<%
														}
														
														
														List<HashMap> ebsData=ConnectionManager.getGraphDBData(um.getUserid(), "DiskReadBytes", 10,"%d-%b");
														
														for( i=0;i<ebsData.size();i++){
															HashMap data=(HashMap)ebsData.get(i);
														
															%>
															<script>
															ebsLabels='<%=data.get("event_date")%>'.split(',');
															ebsData.push([<%=data.get("tagValue")%>]);
															</script>
														
															<%
														}
														
													
														
														%>
														
														
												
	<script>
	
  $(function () {
	  
	  
	  
	  $(document).ready(function(){
		  console.log('dailySalesChart CPUUtilization');
			ec2='AWS/EC2';
		  document.getElementById('EC2Instances').selectedIndex=1;
	    	//fnGenerateGraphs('CPUUtilization','day','10','#dailySalesChart','line');
	    	
	    	fnLoadGraph('#dailySalesChart',cpuLabels,cpuData);
	    	//document.getElementById('EC2Instances').selectedIndex=2;
	    	fnGenerateGraphs('CPUUtilization','day','10','#pieChart','pi');
	    	
	    	
	    	console.log('dailySalesChart NetworkIn');
	    	ec2='';
	    	ebs='AWS/EBS';
	    	fnLoadGraph('#lineChart',ebsLabels,ebsData);
	//fnGenerateGraphs('VolumeReadBytes','day','10','#lineChart','line');
	    	console.log('dailySalesChart NetworkOut');
	    //	fnGenerateGraphs('NetworkOut','day','10','#completedTasksChart','line');
	    	
	    

	    	
	    });
		ec2='AWS/EC2';
		cpu=0;
	    function fnGenerateGraphs(parameter,filter1,filter2,graphId,graphType){
	    	
	     
	         
	    	params='';
	    	
	    	document.getElementById('EC2Instances').selectedIndex=1;
	    	document.getElementById('EBSInstances').selectedIndex=1;
	    	
	    	
	    	if(ec2.length>0){
	    		ec2Instance=$('#EC2Instances').val();
	    		tokens=ec2Instance.split("#");
	    		instance_id=tokens[0];
	    		instance_region=tokens[1];
	    		params+='parameterName='+parameter+"&parameterType=AWS/EC2&region="+instance_region.toLowerCase()+"&InstanceId="+instance_id;	
	    	}else if(ebs.length>0){
	    		ebsInstance=$('#EBSInstances').val();
	    		tokens=ebsInstance.split("#");
	    		instance_id=tokens[0];
	    		instance_region=tokens[1];
	    		params+='parameterName='+parameter+"&parameterType=AWS/EBS&region="+instance_region.toLowerCase()+"&VolumeId="+instance_id;	
	    	}
	    	
	    	//$("input[id='TimeFilterValue']:checked").val();
	    	//$("input[id='TimeFilter']:checked").val();
	    	
	    	//params+='&TimeFilterValue='+$('#TimeFilterValue').val()+"&TimeFilter="+$('#TimeFilter').val();	
	    	params+='&TimeFilterValue='+filter2+'&TimeFilter='+filter1;	
	    	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=generateGraph",
	    			params,
	    			function(data) {
	    		data=$.trim(data);
	    	console.log(data);
	    	tokens=data.split('#');
	    	
	    	labels=tokens[0].split(',');
	    	vals=tokens[1].split(',');
	    	if(parameter=='CPUUtilization'){
	    		cpu=vals[0];
	    	}
	    	$(graphId+"_tip").html(tokens[2]);
	    	//var tr='<table class="table" style="font-size:16px;background-color:lavenderblush;color:black;"><thead class="text-primary"><tr><td>Sr.No</td><td>Date</td><td>% Usage</td></tr></thead><tbody>';
	    	//for(i=0;i<labels.length;i++)	
	    //	tr+='<tr><td>'+(i+1)+'</td><td>'+labels[i]+'</td><td>'+vals[i]+'</td></tr>';
	    	
	    //	tr+='</tbody></table>';
	    	//$('#tableId').html('<i class="fa fa-long-arrow-up"></i>'+tr);
	    	values=doubled(tokens[1].split(','));
	    	val=Math.max.apply(null, values);
	    	console.log("data"+data);
	    	if(graphType=='line'){
	    		fnLoadGraph(graphId,tokens[0].split(','),[values]);
	    	}else if(graphType=='pi'){
	    		//generatePi(graphId,tokens[0].split(','),values);
	    	}
	    	try{
	    		$(graphId+"_head").html(val+"");	
	    	}catch (e) {
				// TODO: handle exception
			}
	    	
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
		function getRandomInt(max) {
			  return Math.floor(Math.random() * Math.floor(max));
			}

		function fnLoadGraph(graphId,labels, values) {
			/*  dataDailySalesChart = {
			          labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
			          series: [
			              [12, 17, 7, 17, 23, 18, 38]
			          ]
			      };
			 */dataDailySalesChart = {  
				labels : labels,
				series : [ values ]
			};

			var b = values.map(function(item) {
				return parseFloat(item);
			});
			val = getMax(b);
			val = val + val * 0.2;
			console.log("b ="+b);
			console.log("val ="+val);
			<% List l4=ConnectionManager.getGraphData();
			   String node="";
 			   for(int g=0;g<l4.size();g++)
			   {
				   GraphModel gm=(GraphModel)l4.get(g);
				   node=node+"'"+gm.getInstanceId()+"',";
						   
			   }
 			   if(node.endsWith(","))
 			   {
 				   int length=node.length();
 				   node =node.substring(0, length-1);
 			   }
			%>
			 node=[<%=node%>];
			
			dataset=[];
			 // color=['#F5A9E144','#A9F5F244','#A9BCF544','#F6CED844','#E1F5A944','#F5BCA944'];
			  color=['#F5A9E144','#A9F5F244','#A9BCF544','#F6CED844','#E1F5A944','#F5BCA944'];
			  for(i=0;i<values.length;i++){
			 
				/*  obj={ */      
						 /*    type :'line',
							fillColor : color[i%color.length],
							strokeColor :'rgba(210, 214, 222, 1)',
							pointColor : 'rgba(210, 214, 222, 1)',
						    pointStrokeColor : '#c1c7d1',
							pointHighlightFill : '#fff',
							pointHighlightStroke : 'rgba(220,220,220,1)', 
							data : values[i],
							label:abc,
							yAxisID: 'y-axis-'+i,
							fill: true, 
				
							
						    data:values[i],
					        label: 'hello',
					        fillColor : color[i%color.length],
					    	strokeColor :'rgba(210, 214, 222, 1)',
							pointColor : 'rgba(210, 214, 222, 1)',
						    pointStrokeColor : '#c1c7d1',
							pointHighlightFill : '#fff',
					        borderColor: "#8e5ea2",
					        fill: true,
							};  
							 */
					
						         /*    label: '# of Votes',
						            data: [12, 19, 3, 5, 2, 3],
						            backgroundColor: [
						                'rgba(255, 99, 132, 0.2)',
						                'rgba(54, 162, 235, 0.2)',
						                'rgba(255, 206, 86, 0.2)',
						                'rgba(75, 192, 192, 0.2)',
						                'rgba(153, 102, 255, 0.2)',
						                'rgba(255, 159, 64, 0.2)'
						            ],
			   */
				 obj={ label : 'Node '+i+" "+node[i],
						type :'line',           		
						fillColor : color[i],
						strokeColor :'rgba(210, 114, 222, 1)',
						pointColor : 'rgba(210, 114, 222, 1)',
						pointStrokeColor : '#c1c7d1',
						pointHighlightFill : '#fff',
						pointHighlightStroke : 'rgba(220,220,220,1)',
						data : values[i],
					    //yAxisID: 'y-axis-'+i,
						};
			 dataset.push(obj);
				
			  }
			  
			
			
			var areaChartData = {
				labels : labels,
				datasets : dataset
				
			}
			
			
			
			var areaChartOptions = {
				//Boolean - If we should show the scale at all
				//dispaly :true,
				showScale : true,
				//Boolean - Whether grid lines are shown across the chart
				scaleShowGridLines : true,
				//String - Colour of the grid lines
				scaleGridLineColor : 'rgba(0,0,0,.09)',
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
				pointDot : true,
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
				
                  title: {
						display: true,
						fontColor:	'#666',
						text: 'Chart.js Bar Chart - Multi Axis'
					},
				showTitle:true,
				//Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
				maintainAspectRatio : true,
				//Boolean - whether to make the chart responsive to window resizing
				responsive : true,
				 scaleShowLabels : true,
				  legend: {
			            display: true,
			            labels: {
			                fontColor: 'rgb(255, 99, 132)'
			            }
					
			        },

			        scales: {
						yAxes: [{
							type: 'linear', // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
							display: true,
							position: 'left',
							id: 'y-axis-1',
						}]}
			};
			var lineChartCanvas = $(graphId).get(0).getContext('2d');
	    	//var lineChart = new Chart(lineChartCanvas); 
	    	  var lineChartOptions = areaChartOptions;
			var lineChart = new Chart(lineChartCanvas,{
			    type: 'line',
			    data: areaChartData,
			    options: lineChartOptions,
			})
		
			lineChartOptions.datasetFill = false;
			//lineChart.Line(areaChartData, lineChartOptions); 
		}
	    
	    
	    
	    function fnLoadGraph2(graphId,labels,values){
	    	   /*  dataDailySalesChart = {
	    	             labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
	    	             series: [
	    	                 [12, 17, 7, 17, 23, 18, 38]
	    	             ]
	    	         };
	    	*/
	    	
	    	dataDailySalesChart = {
	    	        labels: labels,
	    	        series: [
	    	values
	    	        ]
	    	    };
	    	console.log(values);
	    	val=Math.max.apply(null, values);
	    	console.log(val);
	    	         optionsDailySalesChart = {
	    	             lineSmooth: Chartist.Interpolation.cardinal({
	    	                 tension: 0
	    	             }),
	    	             low: 0,
	    	             high: val+50, // creative tim: we recommend you to set the high sa the biggest value + something for a better look
	    	             chartPadding: {
	    	                 top: 0,
	    	                 right: 20,
	    	                 bottom: 0,
	    	                 left: 10
	    	             },
	    	         }

	    	         var dailySalesChart = new Chartist.Line(graphId, dataDailySalesChart, optionsDailySalesChart);
            
	    	         md.startAnimationForLineChart(dailySalesChart);

	    	 }
	    function doubled (arr) {
	        var doubled = [];
	        for (var i = 0; i < arr.length; i++) {
	            doubled.push(arr[i] * 2);
	        }
	        return doubled;
	    }
	  
	   
	       <% List l3=ConnectionManager.getPidata();
	          
	            String  tagValue=(String)l3.get(0);
	            String  dates=(String)l3.get(1);
	              
	          
	        %>
	        labels='<%=dates%>'.split(',');    
	        values=[<%=tagValue%>];
	        generatePi("doughnut-chart",labels,values);
	  function generatePi(graphId,labels,values){
		   	
		   new Chart(document.getElementById("doughnut-chart"), {
			    type: 'doughnut',
			    data: {
			      labels:labels,
			      datasets: [
			        {
			          label: "Population (millions)",
			          backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#e8c3b9","#c45850","#25BB60","#82E42B","#E4AC2B","#2BC2E4","#0D5363","#0CD909","#0968D9","#611684"],
			          data:values
			        }
			      ]
			    },
			    options: {
			      title: {
			        display: true,
			        text: 'EC2 Distribution'
			      }
			    }
			});
	 	
	  }
  
  });
</script>
</html>
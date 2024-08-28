/** 
 * Copyright 2013 AppDynamics 
 * 
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.auto.fetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.DimensionFilter;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Volume;
import com.appdynamics.extensions.cloudwatch.AmazonCloudWatchMonitor;
import com.appdynamics.extensions.cloudwatch.FetchAwsInstances;
import com.appdynamics.extensions.cloudwatch.InstanceModel;
import com.appdynamics.extensions.cloudwatch.configuration.Configuration;
import com.appdynamics.extensions.cloudwatch.metricsmanager.MetricsManagerFactory;
import com.constant.ServerConstants;
import com.database.ConnectionManager;
import com.helper.InstanceUserModel;
import com.helper.StringHelper;
import com.helper.UserModel;

public class AutoFetchAWSStats {
	public static Datapoint dp1;
	private static Configuration config;
	private static AmazonCloudWatch awsCloudWatch;
	private static AmazonCloudWatchMonitor monitor;
	private static MetricsManagerFactory metricsManagerFactory;
	static {
		AutoFetchAWSStats.init();
	}

	// Initialization for local testing. Bit hacky since we are not using the
	// monitor->execute()
	public static void init() {
		// try {
		// config = ConfigurationUtil
		// .getConfigurations(ServerConstants.project_url
		// + "/AWSConfigurations.xml");
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// }
		try {
			// credentialsProvider.getCredentials();
			AWSCredentials credentials = getCredentials();
			awsCloudWatch = new AmazonCloudWatchClient(credentials);
		} catch (Exception e) {
			System.err.println("ERROR IN CONNECTION ");
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.",
					e);
		}

		monitor = new AmazonCloudWatchMonitor();

		// monitor.setAmazonCloudWatch(awsCloudWatch);
		// monitor.setDisabledMetrics(config.disabledMetrics);
		metricsManagerFactory = new MetricsManagerFactory(monitor);
	}

	public static void main(String[] args) {
		// init();
		// testMetrics("AWS/Billing");
		// testForMetrics();

		// testForDataPoints();
		prefetchAWSStatistics();

		HashMap parameters = new HashMap();
		parameters.put("parameterType", "AWS/EC2");
		parameters.put("parameterName", "CPUUtilization");
		parameters.put("statisticsType", "Average");// Minimum, Maximum,
													// Average, SampleCount, Sum
		parameters.put("region", "us-east-1");
		// parameters.put("region", "us-east-1");
		parameters.put("InstanceId", "i-04e26422957c8ebba");
		parameters.put("TimeFilter", "day");
		parameters.put("TimeFilterValue", "10");
		// fetchEC2Parameters(parameters);

		parameters.clear();
		parameters.put("parameterType", "AWS/EBS");
		parameters.put("parameterName", "VolumeReadBytes");
		parameters.put("statisticsType", "Average");// Minimum, Maximum,
													// Average, SampleCount, Sum
		parameters.put("region", "us-east-1");
		// parameters.put("region", "us-east-1");
		parameters.put("VolumeId", "vol-063320caf81c9acfe");
		parameters.put("TimeFilter", "day");
		parameters.put("TimeFilterValue", "10");
		// fetchEC2Parameters(parameters);

		// testForDataPointsBilling();
		// testForDataPointsEBS();
		// testForDataPoints();
		// testDimensions();
		// System.out.println("Finished execution");
		// Map<String, String> taskArguments = new HashMap<String, String>();
		// taskArguments.put("configurations",
		// ServerConstants.WORKSPACE_PATH+"src/main/resources/conf/AWSConfigurations.xml");
		// taskArguments.put("metric-prefix",
		// "Custom Metrics|Amazon Cloud Watch|");
		// AmazonCloudWatchMonitor monitor = new AmazonCloudWatchMonitor();
		// monitor.execute(taskArguments, null);

	}

	public static AWSCredentials getCredentials() {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		AWSCredentials credentials = null;
		try {
			// credentialsProvider.getCredentials();
			BasicAWSCredentials ba = new BasicAWSCredentials(
					ServerConstants.accessKey, ServerConstants.secretKey);

			AWSStaticCredentialsProvider aw = new AWSStaticCredentialsProvider(
					ba);

			return aw.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.",
					e);
		}
	}

	private static void testForDataPointsBilling() {
		List<Dimension> dimensions = new ArrayList<Dimension>();
		Dimension dimension = new Dimension();
		dimension.setName("LinkedAccount");
		dimension.setValue("163547019982");
		// dimensions.add(dimension);

		Dimension dimension1 = new Dimension();
		dimension1.setName("Currency");
		dimension1.setValue("USD");
		dimensions.add(dimension1);

		Dimension dimension21 = new Dimension();
		dimension21.setName("ServiceName");
		dimension21.setValue("EstimatedCharges"); // AmazonS3 AWSDataTransfer

		dimensions.add(dimension21);
		awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get("us-east-1"));// us-east-1
																				// ap-south-1
		// Minimum, Maximum, Average, SampleCount, Sum ].
		GetMetricStatisticsRequest getMetricStatisticsRequest = createGetMetricStatisticsRequest2(
				"AWS/Billing", "EstimatedCharges", "Sum", dimensions);
		GetMetricStatisticsResult getMetricStatisticsResult = awsCloudWatch
				.getMetricStatistics(getMetricStatisticsRequest);
		System.out.println(getMetricStatisticsResult);

	}

	private static void testForDataPoints() {
		String[] metrics = { "CPUUtilization", "NetworkIn", "NetworkOut",
				"DiskReadBytes", "DiskWriteBytes", "DiskReadOps",
				"DiskWriteOps", "StatusCheckFailed",
				"StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get("ap-south-1"));// us-east-1
																				// ap-south-1
		List<Dimension> dimensions = new ArrayList<Dimension>();
		// Dimension dimension = new Dimension();
		// dimension.setName("InstanceId");
		// dimension.setValue("i-055cedb841b4cb04e");
		// dimensions.add(dimension);
		Dimension dimension2 = new Dimension();
		dimension2.setName("InstanceType");
		dimension2.setValue("t2.micro");
		dimensions.add(dimension2);

		for (String s : metrics) {
			System.out.println(s);
			GetMetricStatisticsRequest getMetricStatisticsRequest = createGetMetricStatisticsRequest(
					"AWS/EC2", s, "Average", dimensions);
			GetMetricStatisticsResult getMetricStatisticsResult = awsCloudWatch
					.getMetricStatistics(getMetricStatisticsRequest);
			System.out.println(getMetricStatisticsResult.getDatapoints());
		}
		// metrics = { "NetworkIn", "NetworkOut", "DiskReadBytes",
		// "DiskWriteBytes", "DiskReadOps", "DiskWriteOps", "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		System.out.println("======================================");
		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest("AWS/EBS","VolumeWriteBytes",
		// "Average", dimensions);

	}

	public static String fetchEC2ParametersAll(
			List<InstanceUserModel> instances, List<Volume> volumeList) {
		String instanceIds = "";
		String regionIds = "";
		for (InstanceUserModel ins : instances) {
			instanceIds += ins.getInstanceId() + ",";
			regionIds += ins.getRegion() + ",";
		}
		return fetchEC2ParametersAll(instanceIds, regionIds);
	}

	public static String fetchEBSParametersAll(FetchAwsInstances fi) {
		String instanceIds = "";
		String regionIds = "";
		for (Volume ins : fi.getVolumes()) {
			instanceIds += ins.getVolumeId() + ",";
			regionIds += ins.getAvailabilityZone() + ",";
		}
		return fetchEBSParametersAll(instanceIds, regionIds);
	}

	// TimeFilter
	public static String fetchEC2ParametersAll(String instanceIds,
			String regions) {
		HashMap parameters = new HashMap();
		parameters.put("parameterType", "AWS/EC2");
		parameters.put("parameterName", "CPUUtilization");
		parameters.put("statisticsType", "Average");// Minimum, Maximum,
													// Average, SampleCount, Sum

		// parameters.put("region", "us-east-1");
		// parameters.put("InstanceId", "i-04e26422957c8ebba");
		parameters.put("TimeFilter", "day");
		parameters.put("TimeFilterValue", "10");
		// String instanceIds = StringHelper.n2s(parameters.get("instanceIds"));
		String[] instances = instanceIds.split(",");
		String[] regions_ = regions.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < instances.length; i++) {
			String s = preProcessRegion(regions_[i]);
			parameters.put("region", s);

			parameters.put("InstanceId", instances[i]);
			System.out
					.println("-----------------------------------------------------*"
							+ parameters);
			String data = fetchEC2Parameters(parameters);
			sb.append(data);
			sb.append("####");
		}
		return sb.toString();
	}

	public static String fetchEBSParametersAll(String instanceIds,
			String regions) {
		HashMap parameters = new HashMap();
		parameters.put("parameterType", "AWS/EBS");
		parameters.put("parameterName", "VolumeReadBytes");
		parameters.put("statisticsType", "Average");// Minimum, Maximum,
													// Average, SampleCount, Sum

		// parameters.put("region", "us-east-1");
		// parameters.put("InstanceId", "i-04e26422957c8ebba");
		parameters.put("TimeFilter", "day");
		parameters.put("TimeFilterValue", "10");
		// String instanceIds = StringHelper.n2s(parameters.get("instanceIds"));
		String[] instances = instanceIds.split(",");
		String[] regions_ = regions.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < instances.length; i++) {
			String s = preProcessRegion(regions_[i]);
			parameters.put("region", s);

			parameters.put("VolumeId", instances[i]);
			System.out
					.println("-----------------------------------------------------"
							+ parameters);
			String data = fetchEC2Parameters(parameters);
			sb.append(data);
			sb.append("####");
		}
		return sb.toString();
	}

	public static String preProcessRegion(String region) {
		region = region.toLowerCase();
		if (region.endsWith("a")) {
			region = region.substring(0, region.lastIndexOf("a"));
		}
		if (region.endsWith("b")) {
			region = region.substring(0, region.lastIndexOf("b"));
		}

		region = region.replaceAll("_", "-");
		if (region.equalsIgnoreCase("all")) {
			region = "";
		}

		return region;
	}

	public static void prefetchAWSStatistics() {

		List users = ConnectionManager.getAllUsers();
		String[] parameterType = new String[] { "AWS/EC2", "AWS/EBS" };
		String[][] parametersNames = new String[][] {

		ServerConstants.EC2_MATRIX, ServerConstants.EBS_MATRIX };

		final String timeFilter = "Day";
		final String TimeFilterValue = "30";
		final String statisticsType = "Average";
		for (int i = 0; i < users.size(); i++) {
			UserModel user = (UserModel) users.get(i);

			String userid = user.getUserid();
			ServerConstants.accessKey = user.getAccesskey();
			ServerConstants.secretKey = user.getSecretkey();
			System.out.println(userid + " user ServerConstants.accessKey  "
					+ ServerConstants.accessKey + " ServerConstants.secretKey "
					+ ServerConstants.secretKey);

			// Access Key ID:
			// AKIAI6KGA7BWXYIRGGXA
			// Secret Access Key:
			// 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

			if (ServerConstants.accessKey != null
					&& ServerConstants.accessKey.length() == 0) {
				System.err.println("Access Key and Secret for user id => "
						+ user.getUserid() + " " + user.getUsername()
						+ " is INVALID. Pls check credentials.");
				continue;
			}
			AutoFetchAWSStats.init();

			FetchAwsInstances fi = new FetchAwsInstances();
			boolean success = fi.fetchInstances("all");
			if (!success) {
				System.err.println("Access Key and Secret for user id => "
						+ user.getUserid() + " " + user.getUsername()
						+ " is INVALID. Pls check credentials.");
				continue;
			}
			for (InstanceModel ins : fi.getInstances()) {
				i = i + 1;
				InstanceState state = null;
				String status = "";
				try {
					state = ins.getInstance().getState();
					status = state.getName();
				} catch (Exception e) {

				}
				String instanceId = ins.getInstance().getInstanceId();
				String region = preProcessRegion(ins.getRegion());
				HashMap parameters = new HashMap();
				for (int j = 0; j < ServerConstants.EC2_MATRIX.length; j++) {
					String parameterName = ServerConstants.EC2_MATRIX[j];
					String parameterTypeN = parameterType[0];

					parameters.put("parameterType", parameterTypeN);
					parameters.put("parameterName", parameterName);
					parameters.put("statisticsType", statisticsType);// Minimum,
																		// Maximum,
					// Average, SampleCount, Sum
					parameters.put("region", region);
					// parameters.put("region", "us-east-1");
					parameters.put("InstanceId", instanceId);
					parameters.put("TimeFilter", timeFilter);
					parameters.put("TimeFilterValue", TimeFilterValue);
					String dateValues = getEC2Parameters(parameters);
					try {
						System.out.println(dateValues + " value");
						String axis = dateValues.split("#")[0];
						String values = dateValues.split("#")[1];
						System.out.println(dateValues);
						// store all data
//						ConnectionManager.storeData(values, parameters, axis,
//								12);

					} catch (Exception e) {
						// TODO: handle exception
						System.err.println("Error Fetchign Data for "
								+ instanceId + " " + parameterName);
					}
				}
				System.out
						.println("*************************^******************");
				System.out.println("status ::" + status);
				String instanceIdd = parameters.get("InstanceId").toString();
				System.out.println("instanceid ::" + instanceIdd);
				System.out.println("region ::" + region);
				System.out.println("instanceid ::"
						+ ins.getInstance().getInstanceType());
				String instanceTypee = ins.getInstance().getInstanceType();
//				int rs = ConnectionManager.storeInstances(ins.getInstance(),
//						region, userid);
				System.out
						.println("*************************^******************");

			}

			fi.fetchVolumes("all");
			for (Volume ins : fi.getVolumes()) {
				i = i + 1;
//				ConnectionManager.storeVolume(ins, userid);
				String Volumid = ins.getVolumeId();
				String region = preProcessRegion(ins.getAvailabilityZone());
				HashMap parameters = new HashMap();
				for (int j = 0; j < ServerConstants.EBS_MATRIX.length; j++) {
					try {
						String parameterName = ServerConstants.EBS_MATRIX[j];
						String parameterTypeN = parameterType[1];
						parameters.put("parameterType", parameterTypeN);
						parameters.put("parameterName", parameterName);
						parameters.put("statisticsType", statisticsType);// Minimum,
																			// Maximum,
						// Average, SampleCount, Sum
						parameters.put("region", region);
						// parameters.put("region", "us-east-1");
						parameters.put("VolumeId", Volumid);
						parameters.put("TimeFilter", timeFilter);
						parameters.put("TimeFilterValue", TimeFilterValue);
						String dateValues = getEC2Parameters(parameters);

						String axis = dateValues.split("#")[0];
						String values = dateValues.split("#")[1];

						System.out.println(dateValues);
//						ConnectionManager.storeData(values, parameters, axis,
//								userid);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}

		}

		// ConnectionManager.storeData(values, parameters, axis);

		System.out.println("test");
		// store a data in database

	}

	public static String fetchEC2Parameters(HashMap parameters) {
		// awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get("ap-south-1"));
		String parameterName = StringHelper
				.n2s(parameters.get("parameterName"));
		String parameterType = StringHelper
				.n2s(parameters.get("parameterType")); // "AWS/EC2" "AWS/EBS"
		String InstanceId = StringHelper.n2s(parameters.get("InstanceId"));
		String InstanceType = StringHelper.n2s(parameters.get("InstanceType"));
		String region = StringHelper.n2s(parameters.get("region"));
		String VolumeId = StringHelper.n2s(parameters.get("VolumeId"));

		region = preProcessRegion(region);

		String statisticsType = StringHelper.n2s(parameters
				.get("statisticsType")); // statisticsType "Minimum" "Maximum"
											// "Sum" "Average" "SampleCount"
											// "pNN.NN"

		String TimeFilter = StringHelper.n2s(parameters.get("TimeFilter"));
		int TimeFilterValue = StringHelper.n2i(parameters
				.get("TimeFilterValue"));
		// String[] metrics = {"CPUUtilization", "NetworkIn", "NetworkOut",
		// "DiskReadBytes", "DiskWriteBytes", "DiskReadOps", "DiskWriteOps",
		// "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };

		if (region.length() > 0)
			awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get(region));// us-east-1
		else
			awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get(
					"ap-south-1"));// us-east-1
		// ap-south-1
		List<Dimension> dimensions = new ArrayList<Dimension>();
		if (InstanceId.length() > 0) {
			Dimension dimension = new Dimension();
			dimension.setName("InstanceId");
			dimension.setValue(InstanceId);
			dimensions.add(dimension);
		}
		if (VolumeId.length() > 0) {
			Dimension dimension = new Dimension();
			dimension.setName("VolumeId");
			dimension.setValue(VolumeId);
			dimensions.add(dimension);
		}

		if (InstanceType.length() > 0) {
			Dimension dimension2 = new Dimension();
			dimension2.setName("InstanceType");
			dimension2.setValue(InstanceType);
			dimensions.add(dimension2);
		}
		// for (String s : metrics) {

		// parameter ********************************
		System.out.println("parameter ::" + parameters);
		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest(
		// "AWS/EC2", parameterName, statisticsType, dimensions);

		GetMetricStatisticsRequest getMetricStatisticsRequest = createGetMetricStatisticsRequest2(
				parameterType, parameterName, statisticsType, dimensions,
				TimeFilter, TimeFilterValue);
		GetMetricStatisticsResult getMetricStatisticsResult = awsCloudWatch
				.getMetricStatistics(getMetricStatisticsRequest);
		List<Datapoint> list = getMetricStatisticsResult.getDatapoints();
		System.out.println("size of list " + list.size());
		Collections.sort(list, new Comparator<Datapoint>() {
			public int compare(Datapoint o1, Datapoint o2) {
				if (o1.getTimestamp().getTime() > o2.getTimestamp().getTime()) {
					return 1;
				} else if (o1.getTimestamp().getTime() < o2.getTimestamp()
						.getTime()) {
					return -1;
				} else {
					return 0;
				}

			}
		});

		String axis = new String();
		String values = new String();
		boolean shouldDeallocate = false;
		for (int i = 0; i < list.size(); i++) {
			Datapoint dp = list.get(i);
			dp1 = dp;
			System.out.println("dp" + dp);
			// store data in data

		}
		int cntMin = 0, cntMax = 0;
		for (int i = 0; i < list.size(); i++) {
			Datapoint dp = list.get(i);
			values += (Math.round(dp.getAverage() * 100.0) / 100.0) + ",";

			if (parameterName.equalsIgnoreCase("CPUUtilization")) {
				if (dp.getAverage() < ServerConstants.MIN_CPU_THRESHOLD) {
					cntMin++;
				}
				if (dp.getAverage() > ServerConstants.MAX_CPU_THRESHOLD) {
					cntMax++;
				}
			} else {
				if (dp.getAverage() < ServerConstants.MIN_MEM_THRESHOLD) {
					cntMin++;
				}
				if (dp.getAverage() > ServerConstants.MAX_MEM_THRESHOLD) {
					cntMax++;
				}
			}

			axis += "" + dateTokkmm(dp.getTimestamp(), TimeFilter) + ",";
		}
		System.out.println("cnt " + cntMin + " " + cntMax + " " + list.size());
		String message = "";
		if (cntMin == list.size()) {

			message = parameterName + " is below "
					+ ServerConstants.MIN_CPU_THRESHOLD + " % for consecutive "
					+ TimeFilterValue + " " + TimeFilter
					+ ". Consider deallocating the node for optimal usage";
		}
		if (cntMax == list.size()) {
			message = parameterName + " is above "
					+ ServerConstants.MAX_CPU_THRESHOLD + " % for consecutive "
					+ TimeFilterValue + " " + TimeFilter
					+ ". Node is Optimally used.";
		}
		if (values.length() > 1) {
			values = values.substring(0, values.length() - 1);
			axis = axis.substring(0, axis.length() - 1);
		}
		// }
		// metrics = { "NetworkIn", "NetworkOut", "DiskReadBytes",
		// "DiskWriteBytes", "DiskReadOps", "DiskWriteOps", "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		System.out.println("================Axis/Values======================");
		System.out.println(axis);
		System.out.println(values);
		System.out.println("================Axis/Values======================");
		// System.out.println(dp1.getTimestamp());

		// ConnectionManager.storeData(values, parameters, axis);

		System.out.println("test");
		// store a data in database

		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest("AWS/EBS","VolumeWriteBytes",
		// "Average", dimensions);
		return axis + "#" + values + "#" + message;// ;
	}

	public static String getEC2Parameters(HashMap parameters) {
		// awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get("ap-south-1"));
		String parameterName = StringHelper
				.n2s(parameters.get("parameterName"));
		String parameterType = StringHelper
				.n2s(parameters.get("parameterType")); // "AWS/EC2" "AWS/EBS"
		String InstanceId = StringHelper.n2s(parameters.get("InstanceId"));
		String InstanceType = StringHelper.n2s(parameters.get("InstanceType"));
		String region = StringHelper.n2s(parameters.get("region"));
		String VolumeId = StringHelper.n2s(parameters.get("VolumeId"));

		region = preProcessRegion(region);

		String statisticsType = StringHelper.n2s(parameters
				.get("statisticsType")); // statisticsType "Minimum" "Maximum"
											// "Sum" "Average" "SampleCount"
											// "pNN.NN"

		String TimeFilter = StringHelper.n2s(parameters.get("TimeFilter"));
		int TimeFilterValue = StringHelper.n2i(parameters
				.get("TimeFilterValue"));
		// String[] metrics = {"CPUUtilization", "NetworkIn", "NetworkOut",
		// "DiskReadBytes", "DiskWriteBytes", "DiskReadOps", "DiskWriteOps",
		// "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		System.out
				.println("region " + region + " " + monitor.getRegionvsurls());
		if (region.length() > 0)
			awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get(region));// us-east-1
		else
			awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get(
					"ap-south-1"));// us-east-1
		// ap-south-1
		List<Dimension> dimensions = new ArrayList<Dimension>();
		if (InstanceId.length() > 0) {
			Dimension dimension = new Dimension();
			dimension.setName("InstanceId");
			dimension.setValue(InstanceId);
			dimensions.add(dimension);
		}
		if (VolumeId.length() > 0) {
			Dimension dimension = new Dimension();
			dimension.setName("VolumeId");
			dimension.setValue(VolumeId);
			dimensions.add(dimension);
		}

		if (InstanceType.length() > 0) {
			Dimension dimension2 = new Dimension();
			dimension2.setName("InstanceType");
			dimension2.setValue(InstanceType);
			dimensions.add(dimension2);
		}
		// for (String s : metrics) {

		// parameter ********************************
		System.out.println("parameter ::" + parameters);
		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest(
		// "AWS/EC2", parameterName, statisticsType, dimensions);

		GetMetricStatisticsRequest getMetricStatisticsRequest = createGetMetricStatisticsRequest2(
				parameterType, parameterName, statisticsType, dimensions,
				TimeFilter, TimeFilterValue);
		GetMetricStatisticsResult getMetricStatisticsResult = awsCloudWatch
				.getMetricStatistics(getMetricStatisticsRequest);
		List<Datapoint> list = getMetricStatisticsResult.getDatapoints();
		System.out.println("size of list " + list.size());
		Collections.sort(list, new Comparator<Datapoint>() {
			public int compare(Datapoint o1, Datapoint o2) {
				if (o1.getTimestamp().getTime() > o2.getTimestamp().getTime()) {
					return 1;
				} else if (o1.getTimestamp().getTime() < o2.getTimestamp()
						.getTime()) {
					return -1;
				} else {
					return 0;
				}

			}
		});

		String axis = new String();
		String values = new String();
		boolean shouldDeallocate = false;
		for (int i = 0; i < list.size(); i++) {
			Datapoint dp = list.get(i);
			dp1 = dp;
			System.out.println("dp" + dp);
			// store data in data

		}
		int cntMin = 0, cntMax = 0;
		for (int i = 0; i < list.size(); i++) {
			Datapoint dp = list.get(i);
			values += (Math.round(dp.getAverage() * 100.0) / 100.0) + ",";
			SimpleDateFormat simpledateformat = new SimpleDateFormat(
					"dd:MMM:YYYY");
			axis += "" + simpledateformat.format(dp.getTimestamp()) + ",";
		}

		if (values.length() > 1) {
			values = values.substring(0, values.length() - 1);
			axis = axis.substring(0, axis.length() - 1);
		}
		// }
		// metrics = { "NetworkIn", "NetworkOut", "DiskReadBytes",
		// "DiskWriteBytes", "DiskReadOps", "DiskWriteOps", "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		System.out.println("================Axis/Values======================");
		System.out.println(axis);
		System.out.println(values);
		System.out.println("================Axis/Values======================");
		// System.out.println(dp1.getTimestamp());

		// ConnectionManager.storeData(values, parameters, axis);

		System.out.println("test");
		// store a data in database

		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest("AWS/EBS","VolumeWriteBytes",
		// "Average", dimensions);
		return axis + "#" + values;// ;
	}

	public static String dateTokkmm(java.util.Date myDate, String timeFilter) {

		if (myDate == null) {
			return "";
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat("kk:mm");
		if (timeFilter.equalsIgnoreCase("Hour")) {
			simpledateformat = new SimpleDateFormat("hh a");
		} else if (timeFilter.equalsIgnoreCase("Minute")) {
			simpledateformat = new SimpleDateFormat("mm:ss");
		} else if (timeFilter.equalsIgnoreCase("day")) {
			simpledateformat = new SimpleDateFormat("dd:MMM:YYYY");
		} else if (timeFilter.equalsIgnoreCase("month")) {
			simpledateformat = new SimpleDateFormat("MMM:YY");
		}
		String dateString = "";

		dateString = simpledateformat.format(myDate);
		return dateString;

	}

	private static void testForDataPointsEBS() {
		String[] metrics = { "VolumeReadBytes", "VolumeIdleTime",
				"VolumeWriteOps", "VolumeQueueLength", "VolumeWriteBytes",
				"VolumeReadOps", "BurstBalance", "VolumeTotalReadTime",
				"VolumeTotalWriteTime" };
		awsCloudWatch.setEndpoint(monitor.getRegionvsurls().get("ap-south-1"));// us-east-1
																				// ap-south-1
		List<Dimension> dimensions = new ArrayList<Dimension>();
		Dimension dimension = new Dimension();
		dimension.setName("VolumeId");
		dimension.setValue("vol-08d72f6ca220c4f3e");
		dimensions.add(dimension);
		// Dimension dimension2 = new Dimension();
		// dimension2.setName("InstanceType");
		// dimension2.setValue("t2.micro");
		// dimensions.add(dimension2);
		for (String s : metrics) {
			System.out.println(s);
			GetMetricStatisticsRequest getMetricStatisticsRequest = createGetMetricStatisticsRequest2(
					"AWS/EBS", s, "Sum", dimensions);
			GetMetricStatisticsResult getMetricStatisticsResult = awsCloudWatch
					.getMetricStatistics(getMetricStatisticsRequest);
			System.out.println(getMetricStatisticsResult.getDatapoints());
		}
		// metrics = { "NetworkIn", "NetworkOut", "DiskReadBytes",
		// "DiskWriteBytes", "DiskReadOps", "DiskWriteOps", "StatusCheckFailed",
		// "StatusCheckFailed_Instance", "StatusCheckFailed_System" };
		System.out.println("======================================");
		// GetMetricStatisticsRequest getMetricStatisticsRequest =
		// createGetMetricStatisticsRequest("AWS/EBS","VolumeWriteBytes",
		// "Average", dimensions);

	}

	public static void testDimensions() {
		ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
		Dimension d1 = new Dimension();
		d1.setName("ClusterIdentifier");
		d1.setValue("cluster1");
		dimensions.add(d1);
		GetMetricStatisticsRequest request = createGetMetricStatisticsRequest(
				"AWS/EC2", "CPUUtilization", "Average", dimensions);
		GetMetricStatisticsResult result = awsCloudWatch
				.getMetricStatistics(request);
		System.out.println(result.getDatapoints());
		System.out.println("done");
	}

	public static GetMetricStatisticsRequest createGetMetricStatisticsRequest(
			String namespace, String metricName, String statisticsType,
			List<Dimension> dimensions) {
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest()
				.withStartTime(
						DateTime.now(DateTimeZone.UTC).minusDays(10).toDate())
				.withNamespace(namespace)
				.withDimensions(dimensions)
				.withPeriod(60 * 60 * 24)
				.withMetricName(metricName)
				.withStatistics(statisticsType)
				.withEndTime(
						DateTime.now(DateTimeZone.UTC).minusDays(1).toDate());
		return getMetricStatisticsRequest;
	}

	public static GetMetricStatisticsRequest createGetMetricStatisticsRequest2(
			String namespace, String metricName, String statisticsType,
			List<Dimension> dimensions) {
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest()
				.withStartTime(
						DateTime.now(DateTimeZone.UTC).minusMinutes(10)
								.toDate())
				.withNamespace(namespace)
				.withDimensions(dimensions)
				.withPeriod(30)
				.withMetricName(metricName)
				.withStatistics(statisticsType)
				.withEndTime(
						DateTime.now(DateTimeZone.UTC).minusMinutes(1).toDate());
		return getMetricStatisticsRequest;
	}

	public static GetMetricStatisticsRequest createGetMetricStatisticsRequest2(
			String namespace, String metricName, String statisticsType,
			List<Dimension> dimensions, String timeFilter, int TimeFilterValue) {
		GetMetricStatisticsRequest getMetricStatisticsRequest = null;
		System.out.println("TimeFilterValue " + TimeFilterValue);
		System.out.println("timeFilter " + timeFilter);
		System.out.println("metricName " + metricName);

		if (timeFilter.equalsIgnoreCase("hour")) {
			System.out.println("Hour " + TimeFilterValue);
			getMetricStatisticsRequest = new GetMetricStatisticsRequest()
					.withStartTime(
							DateTime.now(DateTimeZone.UTC)
									.minusHours(TimeFilterValue).toDate())
					.withNamespace(namespace).withDimensions(dimensions)
					.withPeriod(60 * 60)
					// multiples of 60 in seconds 60*60
					.withMetricName(metricName).withStatistics(statisticsType)
					.withEndTime(DateTime.now(DateTimeZone.UTC).toDate());
		} else if (timeFilter.equalsIgnoreCase("day")) {
			System.out.println("Days");
			getMetricStatisticsRequest = new GetMetricStatisticsRequest()
					.withStartTime(
							DateTime.now(DateTimeZone.UTC)
									.minusDays(TimeFilterValue).toDate())
					.withNamespace(namespace).withDimensions(dimensions)
					.withPeriod(60 * 60 * 24)
					// multiples of 60 in seconds 60*60*24
					.withMetricName(metricName).withStatistics(statisticsType)
					.withEndTime(DateTime.now(DateTimeZone.UTC).toDate());
		} else if (timeFilter.equalsIgnoreCase("minute")) {
			getMetricStatisticsRequest = new GetMetricStatisticsRequest()
					.withStartTime(
							DateTime.now(DateTimeZone.UTC)
									.minusMinutes(TimeFilterValue).toDate())
					.withNamespace(namespace).withDimensions(dimensions)
					.withPeriod(60)
					// multiples of 60 in seconds
					.withMetricName(metricName).withStatistics(statisticsType)
					.withEndTime(DateTime.now(DateTimeZone.UTC).toDate());
		} else if (timeFilter.equalsIgnoreCase("month")) {
			getMetricStatisticsRequest = new GetMetricStatisticsRequest()
					.withStartTime(
							DateTime.now(DateTimeZone.UTC)
									.minusMonths(TimeFilterValue).toDate())
					.withNamespace(namespace).withDimensions(dimensions)
					.withPeriod(60 * 60 * 24 * 30)
					// multiples of 60 in seconds 60*60*24*30
					.withMetricName(metricName).withStatistics(statisticsType)
					.withEndTime(DateTime.now(DateTimeZone.UTC).toDate());
		}

		return getMetricStatisticsRequest;
	}

	/*
	 * private static void testMetrics(String namespace) { MetricsManager
	 * metricsManager = metricsManagerFactory.createMetricsManager(namespace,
	 * "monitoring.us-west-2.amazonaws.com"); Map metrics =
	 * metricsManager.gatherMetrics();
	 * System.out.println("Finished testing metrics"); }
	 */

	private static void testForMetrics() {
		awsCloudWatch.setEndpoint("monitoring.us-west-2.amazonaws.com");
		ListMetricsRequest request = new ListMetricsRequest();
		List<DimensionFilter> filters = new ArrayList<DimensionFilter>();
		DimensionFilter dimensionFilter = new DimensionFilter();
		dimensionFilter.withName("InstanceId");
		filters.add(dimensionFilter);
		request.setNamespace("AWS/EC2");
		request.setDimensions(filters);
		ListMetricsResult listMetrics = awsCloudWatch.listMetrics(request);
		System.out.println(listMetrics.getMetrics());
	}
}
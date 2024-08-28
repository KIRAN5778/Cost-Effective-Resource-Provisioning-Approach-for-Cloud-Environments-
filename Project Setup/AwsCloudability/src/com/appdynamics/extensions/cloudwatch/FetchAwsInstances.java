package com.appdynamics.extensions.cloudwatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.http.client.CredentialsProvider;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeVolumesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Volume;
import com.constant.ServerConstants;
import com.helper.InstanceUserModel;
import com.helper.UserModel;

public class FetchAwsInstances implements Serializable {

	public static List<InstanceUserModel> instanceUser = new ArrayList<InstanceUserModel>();
	List<InstanceModel> instances = null;
	List<Volume> volumes = null;

	public FetchAwsInstances() {
		// TODO Auto-generated constructor stub
		instances = new ArrayList<>();
		volumes = new ArrayList<Volume>();
	}

	public static void main(String[] args) {
		FetchAwsInstances fi = new FetchAwsInstances();
		fi.fetchInstances("us-east-1");
	}

	public List<InstanceModel> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceModel> instances) {
		this.instances = instances;
	}

	public static FetchAwsInstances getInstanceList(HttpSession session,
			UserModel um) {
		FetchAwsInstances fi = null;
		if (session.getAttribute("FetchAwsInstances") == null) {
			fi = new FetchAwsInstances();
			if (um == null)
				fi.fetchInstances("all");
			else
				fi.fetchInstances(um.getRegion());

			session.setAttribute("FetchAwsInstances", fi);
		} else {
			fi = (FetchAwsInstances) session.getAttribute("FetchAwsInstances");
		}

		return fi;
	}

	public static FetchAwsInstances getInstanceVolumeList(HttpSession session,
			UserModel um) {
		FetchAwsInstances fi = null;
		if (session.getAttribute("fetchVolumes") == null) {
			fi = new FetchAwsInstances();
			if (um == null)
				fi.fetchVolumes("all");
			else
				fi.fetchVolumes(um.getRegion());

			session.setAttribute("fetchVolumes", fi);
		} else {
			fi = (FetchAwsInstances) session.getAttribute("fetchVolumes");
		}
		return fi;
	}

	public boolean fetchInstances(String userRegion) {

		boolean allRegions = false;
		if (userRegion == null || userRegion.length() == 0
				|| userRegion.equalsIgnoreCase("all")) {
			allRegions = true;
		}

		AWSCredentials credentials = getCredentials();
		List<Regions> regions = ServerConstants.workingRegions();
		for (int i = 0; i < regions.size(); i++) {

			Regions region = regions.get(i);
			if (!allRegions) {
				if (!region.getName().equalsIgnoreCase(userRegion)

				) {
					continue;
				}
			}
			System.out.println("regions[i] " + region.getName());
			try {

				AmazonEC2 ec2 = AmazonEC2ClientBuilder
						.standard()
						.withCredentials(
								new AWSStaticCredentialsProvider(credentials))
						.withRegion(region)// us-west-2
						.build();

				DescribeVolumesResult res = ec2.describeVolumes();
				List<Volume> listVolumes = res.getVolumes();
				for (Volume volume : listVolumes) {
					System.err.println(volume);
				}

				ArrayList<Instance> list = getInstances(ec2);

				for (Instance instance : list) {

					System.out.println(region.toString() + " " + instance);
					InstanceModel imodel = new InstanceModel(instance,
							region.toString());

					instances.add(imodel);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return false;
			}

		}
		System.out.println("You have " + instances.size()
				+ " Amazon EC2 instance(s) running.");
		return true;
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}

	public void fetchVolumes(String userRegion) {
		boolean allRegions = false;
		if (userRegion == null || userRegion.length() == 0
				|| userRegion.equalsIgnoreCase("all")) {
			allRegions = true;
		}

		AWSCredentials credentials = getCredentials();
		List<Regions> region = ServerConstants.workingRegions();
		for (int i = 0; i < region.size(); i++) {

			Regions regions = region.get(i);
			if (!allRegions) {
				if (!regions.getName().equalsIgnoreCase(userRegion)) {
					continue;
				}
			}
			System.out.println("regions[i] " + regions);
			try {

				AmazonEC2 ec2 = AmazonEC2ClientBuilder
						.standard()
						.withCredentials(
								new AWSStaticCredentialsProvider(credentials))
						.withRegion(regions)// us-west-2
						.build();

				DescribeVolumesResult res = ec2.describeVolumes();
				List<Volume> listVolumes = res.getVolumes();
				for (Volume volume : listVolumes) {
					System.err.println(volume);
					volumes.add(volume);
				}

				// ArrayList<Instance> list = getInstances(ec2);
				//
				// for (Instance instance : list) {
				//
				// System.out.println(regions[i].toString() + " " + instance);
				// InstanceModel imodel = new InstanceModel(instance,
				// regions[i].toString());
				// instances.add(imodel);
				// }
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}
		System.out.println("You have " + instances.size()
				+ " Amazon EC2 instance(s) running.");
	}

	public ArrayList<Instance> getInstances(AmazonEC2 ec2) {
		DescribeAvailabilityZonesResult availabilityZonesResult = ec2
				.describeAvailabilityZones();

		List<com.amazonaws.services.ec2.model.AvailabilityZone> zones = availabilityZonesResult
				.getAvailabilityZones();
		// for (AvailabilityZone availabilityZone : zones) {
		// System.out.println(" Zone " + availabilityZone);
		// }
		System.out.println("You have access to "
				+ availabilityZonesResult.getAvailabilityZones().size()
				+ " Availability Zones.");

		DescribeInstancesResult describeInstancesRequest = ec2
				.describeInstances();
		List<Reservation> reservations = describeInstancesRequest
				.getReservations();
		ArrayList<Instance> instances = new ArrayList<Instance>();
		for (Reservation reservation : reservations) {
			System.out.println(reservation.getInstances());

			instances.addAll(reservation.getInstances());
		}
		System.out.println(instances.size());
		// System.out.println("check >>>>>>>>>>>>>");
		System.out.println("You have " + instances.size()
				+ " Amazon EC2 instance(s) running.");

		return instances;
	}

	public AWSCredentials getCredentials() {
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
}

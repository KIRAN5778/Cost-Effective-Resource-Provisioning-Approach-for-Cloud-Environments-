package com.constant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.regions.Regions;

import com.helper.SvmClassifier;

public class ServerConstants {
	
	public static final SvmClassifier svm = new SvmClassifier();


	
	public static final String PROJECT_DATASET_DIR = "D:\\work\\project\\Cloudability-main-final\\Cloudability";
	public static final String SVM_DIR = PROJECT_DATASET_DIR + "\\svm\\";
	public static final String SVM_MODEL_FILE = SVM_DIR + "svm.model";
	public static final String SVM_TRAINING_SET = SVM_DIR + "svmTraining.arff";
	public static final String APPLICATION_NAME = "Cloudability";

	public static String user;

	public static final int MIN_CPU_THRESHOLD = 10;
	public static final int MAX_CPU_THRESHOLD = 70;

	public static final int MIN_MEM_THRESHOLD = 1 * 1000 * 1000;
	public static final int MAX_MEM_THRESHOLD = 5 * 1000 * 1000;

	public static final String db_driver = "com.mysql.jdbc.Driver";
	public static final String db_user = "root";
	public static final String db_pwd = "";
	public static final String db_url = "jdbc:mysql://localhost/cloudability";

/*
	add two keys here
	 */
	public static List<Regions> workingRegions() {
		Regions[] regions = Regions.values();
		return Arrays
				.stream(regions)
				.filter(region -> (!region.getName().equalsIgnoreCase(
						"us-gov-west-1") && !region.getName().equalsIgnoreCase(
						"cn-north-1"))).collect(Collectors.toList());

	}

	public static void main(String[] args) {
		System.out.println(ServerConstants.workingRegions());
	}

	public static final String[] EBS_MATRIX = { "VolumeReadBytes",
			"VolumeIdleTime", "VolumeWriteOps", "VolumeQueueLength",
			"VolumeWriteBytes", "VolumeReadOps", "BurstBalance",
			"VolumeTotalReadTime", "VolumeTotalWriteTime" };

	public static final String[] EC2_MATRIX = { "CPUUtilization", "NetworkIn",
			"NetworkOut", "DiskReadBytes", "DiskWriteBytes", "DiskReadOps",
			"DiskWriteOps" };
	// , "StatusCheckFailed",
	// "StatusCheckFailed_Instance", "StatusCheckFailed_System"

	public static final String LOCAL_UPLOAD = "test";

	static {
		File f = new File(LOCAL_UPLOAD);
		if (!f.exists()) {
			f.mkdirs();
			System.out.println("created folder:");
		}
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap catagoryFriendlyNamesMap = new HashMap();

}

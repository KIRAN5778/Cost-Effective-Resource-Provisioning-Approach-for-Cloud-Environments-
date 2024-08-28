package com.appdynamics.extensions.cloudwatch;

import com.amazonaws.services.ec2.model.Instance;

public class InstanceModel {
	Instance instance;
	String region = "";

	public InstanceModel(Instance current, String region) {
		// TODO Auto-generated constructor stub
		this.instance = current;
		this.region = region;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}

package com.helper;

public class GraphModel {
	
	String statsid;
	
	String tagName;
	String maxDate;

	String instanceId;
	 String valuess;
	 String dates;
	 String tagValue;
	 
	 double val;
	 double net;
	 double cpu;
	 
	 String decision;
	 
	 
	public String getDecision() {
		return decision;
	}
	public void setDecision(String  decision) {
		this.decision = decision;
	}
	public String getTagValue() {
	return tagValue;
	}
	public void setTagValue(String tagValue) {
	this.tagValue = tagValue;
	}
	public double getVal() {
		return val;
	}
	public void setVal(double val) {
		this.val = val;
	}
	public double getNet() {
		return net;
	}
	public void setNet(double net) {
		this.net = net;
	}
	public double getCpu() {
		return cpu;
	}
	public void setCpu(double cpu) {
		this.cpu = cpu;	
	}
	public String getMaxDate() {
	return maxDate;
	}
	public void setMaxDate(String maxDate) {
	this.maxDate = maxDate;
	}
	 public String getStatsid() {
			return statsid;
		}
		public void setStatsid(String statsid) {
			this.statsid = statsid;
		}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getValuess() {
		return valuess;
	}
	public void setValuess(String valuess) {
		this.valuess = valuess;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}

}

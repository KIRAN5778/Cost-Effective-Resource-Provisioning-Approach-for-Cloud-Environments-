package com.helper;

import java.io.Serializable;

public class UserModel implements Serializable {
	static final long serialVersionUID = 1121L;
	String userid, username, pass, phoneno, fname, lname, region, regionName, accesskey, secretkey;
	String preferredCatagories="";
	String preferredCatagoriesDisplay="";
	public String getUserid() {
		return userid;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setUserid(String userid) {
		this.userid = userid;   
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPreferredCatagories() {
		return preferredCatagories;
	}
	public void setPreferredCatagories(String preferredCatagories) {
		this.preferredCatagories = preferredCatagories;
	}
	public String getPreferredCatagoriesDisplay() {
		return preferredCatagoriesDisplay;
	}
	public void setPreferredCatagoriesDisplay(String preferredCatagoriesDisplay) {
		this.preferredCatagoriesDisplay = preferredCatagoriesDisplay;
	}	

}

package com.chrisali.easylogbook.beans;

public enum PilotLicenses {
	STUDENT 	("Student"),
	PRIVATE 	("Private"),
	SPORT	 	("Sport"),
	COMMERCIAL  ("Commercial"),
	ATP 		("Airline Transport"),
	CFI			("Certified Flight Instructor");
	
	private String license;
	
	private PilotLicenses (String license) {this.license = license;}

	public String getLicense() {return license;}
}

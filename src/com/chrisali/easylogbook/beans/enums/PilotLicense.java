package com.chrisali.easylogbook.beans.enums;

public enum PilotLicense {
	STUDENT 	("Student"),
	PRIVATE 	("Private"),
	SPORT	 	("Sport"),
	COMMERCIAL  ("Commercial"),
	ATP 		("Airline Transport"),
	CFI			("Certified Flight Instructor"),
	INSTRUMENT  ("Instrument");
	
	private String license;
	
	private PilotLicense (String license) {this.license = license;}

	public String getLicense() {return license;}
}

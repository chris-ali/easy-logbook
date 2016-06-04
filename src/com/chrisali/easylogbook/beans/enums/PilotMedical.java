package com.chrisali.easylogbook.beans.enums;

public enum PilotMedical {
	FIRST_CLASS   ("First Class"),
	SECOND_CLASS  ("Second Class"),
	THIRD_CLASS   ("Third Class");
	
	private String medical;
	
	private PilotMedical (String medical) {this.medical = medical;}

	public String getMedical() {return medical;}
}

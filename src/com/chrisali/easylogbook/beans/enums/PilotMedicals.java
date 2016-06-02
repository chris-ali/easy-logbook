package com.chrisali.easylogbook.beans.enums;

public enum PilotMedicals {
	FIRST_CLASS   ("First Class"),
	SECOND_CLASS  ("Second Class"),
	THIRD_CLASS   ("Third Class");
	
	private String medical;
	
	private PilotMedicals (String medical) {this.medical = medical;}

	public String getMedical() {return medical;}
}

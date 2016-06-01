package com.chrisali.easylogbook.beans;

public enum PilotExaminations {
	CHECKRIDE   ("Checkride"),
	BFR 		("Biennial Flight Review"),
	CFI_RENEWAL ("CFI Renewal"),
	IPC 		("Instrument Proficiency Check");
	
	private String exam;
	
	private PilotExaminations (String exam) {this.exam = exam;}

	public String getExam() {return exam;}
}

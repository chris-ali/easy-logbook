package com.chrisali.easylogbook.beans.enums;

public enum PilotExamination {
	CHECKRIDE   ("Checkride"),
	BFR 		("Biennial Flight Review"),
	CFI_RENEWAL ("CFI Renewal"),
	IPC 		("Instrument Proficiency Check");
	
	private String exam;
	
	private PilotExamination (String exam) {this.exam = exam;}

	public String getExam() {return exam;}
}

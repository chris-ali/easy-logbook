package com.chrisali.easylogbook.model.enums;

public enum ClassRating {
	SINGLELAND  ("Single Engine Land"),
	MULTILAND   ("Multi Engine Land"),
	SINGLESEA   ("Single Engine Sea"),
	MULTISEA    ("Multi Engine Sea"),
	GYROPLANE 	("Gyroplane"),
	HELICOPTER 	("Helicopter"),
	AIRSHIP 	("Airship"),
	FREEBALLOON ("Free Balloon");
	
	private String classRating;
	
	private ClassRating (String classRating) {this.classRating = classRating;}

	public String getClassRating() {return classRating;}
}

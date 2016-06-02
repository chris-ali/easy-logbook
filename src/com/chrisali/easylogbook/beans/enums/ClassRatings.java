package com.chrisali.easylogbook.beans.enums;

public enum ClassRatings {
	SINGLELAND  ("Single Engine Land"),
	MULTILAND   ("Multi Engine Land"),
	SINGLESEA   ("Single Engine Sea"),
	MULTISEA    ("Multi Engine Sea"),
	GYROPLANE 	("Gyroplane"),
	HELICOPTER 	("Helicopter"),
	AIRSHIP 	("Airship"),
	FREEBALLOON ("Free Balloon");
	
	private String classRating;
	
	private ClassRatings (String classRating) {this.classRating = classRating;}

	public String getClassRating() {return classRating;}
}

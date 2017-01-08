package com.chrisali.easylogbook.model.enums;

public enum CategoryRating {
	AIRPLANE    ("Airplane"),
	LTA		    ("Lighter than Air"),
	POWERLIFT   ("Powered Lift"),
	WEIGHTSHIFT ("Weight Shift Control"),
	GLIDER 		("Glider"),
	ROTORCRAFT 	("Rotorcraft");
	
	private String categoryRating;
	
	private CategoryRating (String categoryRating) {this.categoryRating = categoryRating;}

	public String getCategoryRating() {return categoryRating;}
}

package com.chrisali.easylogbook.beans.enums;

public enum CategoryRatings {
	AIRPLANE    ("Airplane"),
	LTA		    ("Lighter than Air"),
	POWERLIFT   ("Powered Lift"),
	WEIGHTSHIFT ("Weight Shift Control"),
	GLIDER 		("Glider"),
	ROTORCRAFT 	("Rotorcraft");
	
	private String categoryRating;
	
	private CategoryRatings (String categoryRating) {this.categoryRating = categoryRating;}

	public String getCategoryRating() {return categoryRating;}
}

package com.chrisali.easylogbook.beans;

public enum PilotRatings {
	STUDENT 	("Student"),
	PRIVATE 	("Private"),
	COMMERCIAL  ("Commercial"),
	ATP 		("Airline Transport"),
	INSTRUMENT  ("Instrument"),
	MULTIENGINE ("Multi Engine"),
	GLIDER 		("Glider"),
	ROTORCRAFT 	("Rotorcraft"),
	SEA			("Sea");
	
	private String rating;
	
	private PilotRatings (String rating) {this.rating = rating;}

	public String getRating() {return rating;}
}

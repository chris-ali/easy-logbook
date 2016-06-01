package com.chrisali.easylogbook.beans;

public enum PilotRatings {
	INSTRUMENT  ("Instrument"),
	MULTIENGINE ("Multi Engine"),
	GLIDER 		("Glider"),
	ROTORCRAFT 	("Rotorcraft"),
	SEA			("Sea");
	
	private String rating;
	
	private PilotRatings (String rating) {this.rating = rating;}

	public String getRating() {return rating;}
}

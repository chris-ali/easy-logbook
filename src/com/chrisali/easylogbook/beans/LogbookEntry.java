package com.chrisali.easylogbook.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="logbook_entries")
public class LogbookEntry implements Serializable {

	private static final long serialVersionUID = -5818892846481861250L;

	@Id
	@GeneratedValue
	private int id;
	
	private int logbooks_id;
	
	@OneToOne
	@JoinColumn(name="tailNumber")
	private Aircraft aircraft;
	
	//TODO Needs a pattern matching regexp
	private String date;
	
	// Route
	@NotBlank
	@Size(min=2, max=4)
	private String from;
	@NotBlank
	@Size(min=2, max=4)
	private String to;
	
	private int instrumentApproaches;
	private int dayLandings;
	private int nightLandings;
	
	// Category/Class
	private float airplaneSel = 0.0f;
	private float airplaneMel = 0.0f;
	private float turbine = 0.0f;
	private float glider = 0.0f;
	private float rotorcraft = 0.0f;

	// Conditions of Flight
	private float night = 0.0f;
	private float actualInstrument = 0.0f;
	private float simulatedInstrument = 0.0f;
	private float groundTrainer = 0.0f;
	
	// Type of Piloting
	private float crossCountry = 0.0f;
	private float dualReceived = 0.0f;
	private float dualGiven = 0.0f;
	private float pilotInCommand = 0.0f;
	private float secondInCommand = 0.0f;
	
	private float totalDuration = 0.0f;
	
	@Size(max=200)
	private String remarks = "";
	
	public LogbookEntry() {
		this.aircraft = new Aircraft();
	}

	public LogbookEntry(int id, Logbook logbook, Aircraft aircraft, String date) {
		this(logbook, aircraft, date);
		this.id = id;
	}
	
	public LogbookEntry(Logbook logbook, Aircraft aircraft, String date) {
		this.logbooks_id = logbook.getId();
		this.aircraft = aircraft;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLogbooks_id() {
		return logbooks_id;
	}

	public void setLogbooks_id(int logbooks_id) {
		this.logbooks_id = logbooks_id;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}

	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getInstrumentApproaches() {
		return instrumentApproaches;
	}

	public void setInstrumentApproaches(int instrumentApproaches) {
		this.instrumentApproaches = instrumentApproaches;
	}

	public int getDayLandings() {
		return dayLandings;
	}

	public void setDayLandings(int dayLandings) {
		this.dayLandings = dayLandings;
	}

	public int getNightLandings() {
		return nightLandings;
	}

	public void setNightLandings(int nightLandings) {
		this.nightLandings = nightLandings;
	}

	public float getAirplaneSel() {
		return airplaneSel;
	}

	public void setAirplaneSel(float airplaneSel) {
		this.airplaneSel = airplaneSel;
	}

	public float getAirplaneMel() {
		return airplaneMel;
	}

	public void setAirplaneMel(float airplaneMel) {
		this.airplaneMel = airplaneMel;
	}

	public float getTurbine() {
		return turbine;
	}

	public void setTurbine(float turbine) {
		this.turbine = turbine;
	}

	public float getGlider() {
		return glider;
	}

	public void setGlider(float glider) {
		this.glider = glider;
	}

	public float getRotorcraft() {
		return rotorcraft;
	}

	public void setRotorcraft(float rotorcraft) {
		this.rotorcraft = rotorcraft;
	}

	public float getNight() {
		return night;
	}

	public void setNight(float night) {
		this.night = night;
	}

	public float getActualInstrument() {
		return actualInstrument;
	}

	public void setActualInstrument(float actualInstrument) {
		this.actualInstrument = actualInstrument;
	}

	public float getSimulatedInstrument() {
		return simulatedInstrument;
	}

	public void setSimulatedInstrument(float simulatedInstrument) {
		this.simulatedInstrument = simulatedInstrument;
	}

	public float getGroundTrainer() {
		return groundTrainer;
	}

	public void setGroundTrainer(float groundTrainer) {
		this.groundTrainer = groundTrainer;
	}

	public float getCrossCountry() {
		return crossCountry;
	}

	public void setCrossCountry(float crossCountry) {
		this.crossCountry = crossCountry;
	}

	public float getDualReceived() {
		return dualReceived;
	}

	public void setDualReceived(float dualReceived) {
		this.dualReceived = dualReceived;
	}

	public float getDualGiven() {
		return dualGiven;
	}

	public void setDualGiven(float dualGiven) {
		this.dualGiven = dualGiven;
	}

	public float getPilotInCommand() {
		return pilotInCommand;
	}

	public void setPilotInCommand(float pilotInCommand) {
		this.pilotInCommand = pilotInCommand;
	}

	public float getSecondInCommand() {
		return secondInCommand;
	}

	public void setSecondInCommand(float secondInCommand) {
		this.secondInCommand = secondInCommand;
	}

	public float getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(float totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}

package com.chrisali.easylogbook.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.chrisali.easylogbook.validation.FormValidationGroup;
import com.chrisali.easylogbook.validation.PersistenceValidationGroup;
import com.chrisali.easylogbook.validation.ValidDuration;

@Entity
@Table(name="logbook_entries")
public class LogbookEntry implements Serializable {

	private static final long serialVersionUID = -5818892846481861250L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="logbooks_id", referencedColumnName="id")
	private Logbook logbook;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="aircraft_id", referencedColumnName="id"),
		@JoinColumn(name="tailNumber", referencedColumnName="tailNumber"),
		@JoinColumn(name="make", referencedColumnName="make"),
		@JoinColumn(name="model", referencedColumnName="model")
	})
	private Aircraft aircraft;
	
	//TODO Needs a pattern matching regexp
	@NotBlank(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String date;
	
	// Route
	@NotBlank
	@Size(min=2, max=4, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String origin;
	@NotBlank
	@Size(min=2, max=4, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String destination;
	
	@Range(min=0, max=255, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private int instrumentApproaches = 0;
	@Range(min=0, max=255, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private int dayLandings = 0;
	@Range(min=0, max=255, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private int nightLandings = 0;
	
	// Category/Class
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float airplaneSel = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float airplaneMel = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float turbine = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float glider = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float rotorcraft = 0.0f;

	// Conditions of Flight
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float night = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float actualInstrument = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float simulatedInstrument = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float groundTrainer = 0.0f;
	
	// Type of Piloting
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float crossCountry = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float dualReceived = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float dualGiven = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float pilotInCommand = 0.0f;
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float secondInCommand = 0.0f;
	
	@ValidDuration(groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private float totalDuration = 0.0f;
	
	@Size(max=200, groups={PersistenceValidationGroup.class, FormValidationGroup.class})
	private String remarks = "";
	
	public LogbookEntry() {
		this.aircraft = new Aircraft();
	}

	public LogbookEntry(int id, Logbook logbook, Aircraft aircraft, String date) {
		this(logbook, aircraft, date);
		this.id = id;
	}
	
	public LogbookEntry(Logbook logbook, Aircraft aircraft, String date) {
		this.logbook = logbook;
		this.aircraft = aircraft;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Logbook getLogbook() {
		return logbook;
	}

	public void setLogbook(Logbook logbook) {
		this.logbook = logbook;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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
	
	@Override
	public String toString() {
		return "LogbookEntry [id=" + id + ", logbook=" + logbook + ", aircraft=" + aircraft + ", date=" + date
				+ ", origin=" + origin + ", destination=" + destination + ", instrumentApproaches="
				+ instrumentApproaches + ", dayLandings=" + dayLandings + ", nightLandings=" + nightLandings
				+ ", airplaneSel=" + airplaneSel + ", airplaneMel=" + airplaneMel + ", turbine=" + turbine + ", glider="
				+ glider + ", rotorcraft=" + rotorcraft + ", night=" + night + ", actualInstrument=" + actualInstrument
				+ ", simulatedInstrument=" + simulatedInstrument + ", groundTrainer=" + groundTrainer
				+ ", crossCountry=" + crossCountry + ", dualReceived=" + dualReceived + ", dualGiven=" + dualGiven
				+ ", pilotInCommand=" + pilotInCommand + ", secondInCommand=" + secondInCommand + ", totalDuration="
				+ totalDuration + ", remarks=" + remarks + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(actualInstrument);
		result = prime * result + ((aircraft == null) ? 0 : aircraft.hashCode());
		result = prime * result + Float.floatToIntBits(airplaneMel);
		result = prime * result + Float.floatToIntBits(airplaneSel);
		result = prime * result + Float.floatToIntBits(crossCountry);
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + dayLandings;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + Float.floatToIntBits(dualGiven);
		result = prime * result + Float.floatToIntBits(dualReceived);
		result = prime * result + Float.floatToIntBits(glider);
		result = prime * result + Float.floatToIntBits(groundTrainer);
		result = prime * result + id;
		result = prime * result + instrumentApproaches;
		result = prime * result + ((logbook == null) ? 0 : logbook.hashCode());
		result = prime * result + Float.floatToIntBits(night);
		result = prime * result + nightLandings;
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + Float.floatToIntBits(pilotInCommand);
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + Float.floatToIntBits(rotorcraft);
		result = prime * result + Float.floatToIntBits(secondInCommand);
		result = prime * result + Float.floatToIntBits(simulatedInstrument);
		result = prime * result + Float.floatToIntBits(totalDuration);
		result = prime * result + Float.floatToIntBits(turbine);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogbookEntry other = (LogbookEntry) obj;
		if (Float.floatToIntBits(actualInstrument) != Float.floatToIntBits(other.actualInstrument))
			return false;
		if (aircraft == null) {
			if (other.aircraft != null)
				return false;
		} else if (!aircraft.equals(other.aircraft))
			return false;
		if (Float.floatToIntBits(airplaneMel) != Float.floatToIntBits(other.airplaneMel))
			return false;
		if (Float.floatToIntBits(airplaneSel) != Float.floatToIntBits(other.airplaneSel))
			return false;
		if (Float.floatToIntBits(crossCountry) != Float.floatToIntBits(other.crossCountry))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dayLandings != other.dayLandings)
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (Float.floatToIntBits(dualGiven) != Float.floatToIntBits(other.dualGiven))
			return false;
		if (Float.floatToIntBits(dualReceived) != Float.floatToIntBits(other.dualReceived))
			return false;
		if (Float.floatToIntBits(glider) != Float.floatToIntBits(other.glider))
			return false;
		if (Float.floatToIntBits(groundTrainer) != Float.floatToIntBits(other.groundTrainer))
			return false;
		if (id != other.id)
			return false;
		if (instrumentApproaches != other.instrumentApproaches)
			return false;
		if (logbook == null) {
			if (other.logbook != null)
				return false;
		} else if (!logbook.equals(other.logbook))
			return false;
		if (Float.floatToIntBits(night) != Float.floatToIntBits(other.night))
			return false;
		if (nightLandings != other.nightLandings)
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (Float.floatToIntBits(pilotInCommand) != Float.floatToIntBits(other.pilotInCommand))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (Float.floatToIntBits(rotorcraft) != Float.floatToIntBits(other.rotorcraft))
			return false;
		if (Float.floatToIntBits(secondInCommand) != Float.floatToIntBits(other.secondInCommand))
			return false;
		if (Float.floatToIntBits(simulatedInstrument) != Float.floatToIntBits(other.simulatedInstrument))
			return false;
		if (Float.floatToIntBits(totalDuration) != Float.floatToIntBits(other.totalDuration))
			return false;
		if (Float.floatToIntBits(turbine) != Float.floatToIntBits(other.turbine))
			return false;
		return true;
	}
}

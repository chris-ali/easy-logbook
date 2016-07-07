package com.chrisali.easylogbook.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.dao.AircraftDao;
import com.chrisali.easylogbook.dao.LogbookDao;
import com.chrisali.easylogbook.dao.LogbookEntryDao;

@Service("aircraftService")
public class AircraftService {

	@Autowired
	private AircraftDao aircraftDao;
	
	@Autowired
	private LogbookEntryDao logbookEntryDao;
	
	@Autowired
	private LogbookDao logbookDao;
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public void createOrUpdate(Aircraft aircraft) {
		aircraftDao.createOrUpdate(aircraft);
	}
	
	@Secured("ROLE_ADMIN")
	public List<Aircraft> getAllAircraft() {
		return aircraftDao.getAircraft();
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<Aircraft> getAircraft(String username) {
		return aircraftDao.getAircraft(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, int id) {
		return aircraftDao.getAircraft(username, id);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, String tailNumber) {
		return aircraftDao.getAircraft(username, tailNumber);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public boolean delete(String username, int id) {
		return aircraftDao.delete(username, id);
	}
	
	public boolean exists(String username, String tailNumber) {
		return aircraftDao.exists(username, tailNumber);
	}
	
	/**
	 * Calculates the total time logged on a single aircraft across all aircraft
	 * 
	 * @param username
	 * @param aircraftId
	 * @return float of totals for an aircraft
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public float aircraftTotals(String username, int aircraftId) {
		// Replace with SQL query: SELECT SUM(totalDuration) FROM logbook_entries e WHERE e.aircraft_id = ?;
		List<Logbook> logbooks = logbookDao.getLogbooks(username);
		List<LogbookEntry> logbookEntriesByAircraft = new LinkedList<>();
		
		float totalDuration = 0.0f;
		
		// Add all logbook entries pertaining to aircraft in question to single list
		for (Logbook logbook : logbooks)
			logbookEntriesByAircraft.addAll(logbookEntryDao.getLogbookEntriesByAircraft(logbook.getId(), aircraftId));
		
		// Iterate through list of entries and add together all summable fields
		for(LogbookEntry entry : logbookEntriesByAircraft)
			totalDuration += entry.getTotalDuration();
		
		return totalDuration;
	}
}

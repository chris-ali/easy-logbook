package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.dao.LogbookDao;
import com.chrisali.easylogbook.dao.LogbookEntryDao;

@Service("logbookService")
public class LogbookService {
	
	@Autowired
	private LogbookDao logbookDao;
	
	@Autowired
	private LogbookEntryDao logbookEntryDao;
	
	/**
	 * Creates or updates {@link Logbook} object into database
	 * 
	 * @param logbook
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public void createOrUpdate(Logbook logbook) {
		logbookDao.createOrUpdate(logbook);
	}
	
	/**
	 * @return all {@link Logbook} objects in database
	 */
	@Secured("ROLE_ADMIN")
	public List<Logbook> getAllLogbooks() {
		return logbookDao.getLogbooks();
	}
	
	/**
	 * @param username
	 * @return List of {@link Logbook} belonging to user
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<Logbook> getLogbooks(String username) {
		return logbookDao.getLogbooks(username);
	}
	
	/**
	 * @param username
	 * @param name of {@link Logbook}
	 * @return logbook from database for a username and logbook name
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public Logbook getLogbook(String username, String name) {
		return logbookDao.getLogbook(username, name);
	}
	
	/**
	 * @param username
	 * @param id of {@link Logbook}
	 * @return logbook from database for a username and logbook id
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public Logbook getLogbook(String username, int id) {
		return logbookDao.getLogbook(username, id);
	}
	
	/**
	 * Deletes {@link Logbook} object from database; deletes all {@link LogbookEntry} objects 
	 * associated with logbook beforehand 
	 * 
	 * @param username
	 * @param id
	 * @return if logbook was successfully deleted
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(String username, int id) {
		List<LogbookEntry> logbookEntries = logbookEntryDao.getAllLogbookEntries(id);
		
		// Need to delete all entries belonging to this logbook first
		for(LogbookEntry entry : logbookEntries)
			logbookEntryDao.delete(id, entry.getId());
		
		return logbookDao.delete(username, id);
	}
	
	/**
	 * @param username
	 * @param name of {@link Logbook}
	 * @return if logbook object exists in database
	 */
	public boolean exists(String username, String name) {
		return logbookDao.exists(username, name);
	}
	
	/**
	 * Calculates a total of all entries in a single logbook  
	 * 
	 * @param username
	 * @param id of logbook
	 * @return {@link LogbookEntry} of totals for a logbook
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public LogbookEntry logbookTotals(String username, int id) {
		List<LogbookEntry> logbookEntries = logbookEntryDao.getAllLogbookEntries(id);
		LogbookEntry totals = new LogbookEntry();
		
		// Iterate through list of entries and add together all summable fields
		for(LogbookEntry entry : logbookEntries) {
			totals.setInstrumentApproaches((entry.getInstrumentApproaches() + totals.getInstrumentApproaches()));
			totals.setDayLandings((entry.getDayLandings() + totals.getDayLandings()));
			totals.setNightLandings((entry.getNightLandings() + totals.getNightLandings()));
			
			totals.setAirplaneSel((entry.getAirplaneSel() + totals.getAirplaneSel()));
			totals.setAirplaneMel((entry.getAirplaneMel() + totals.getAirplaneMel()));
			totals.setTurbine((entry.getTurbine() + totals.getTurbine()));
			totals.setGlider((entry.getGlider() + totals.getGlider()));
			totals.setRotorcraft((entry.getRotorcraft() + totals.getRotorcraft()));
			
			totals.setNight((entry.getNight() + totals.getNight()));
			totals.setActualInstrument((entry.getActualInstrument() + totals.getActualInstrument()));
			totals.setSimulatedInstrument((entry.getSimulatedInstrument() + totals.getSimulatedInstrument()));
			totals.setGroundTrainer((entry.getGroundTrainer() + totals.getGroundTrainer()));
			
			totals.setCrossCountry((entry.getCrossCountry() + totals.getCrossCountry()));
			totals.setDualReceived((entry.getDualReceived() + totals.getDualReceived()));
			totals.setDualGiven((entry.getDualGiven() + totals.getDualGiven()));
			totals.setPilotInCommand((entry.getPilotInCommand() + totals.getPilotInCommand()));
			totals.setSecondInCommand((entry.getSecondInCommand() + totals.getSecondInCommand()));
			
			totals.setTotalDuration((entry.getTotalDuration() + totals.getTotalDuration()));
		}
		
		return totals;
	}
	
	/**
	 * Calculates an overall total of all entries for all {@link Logbook} owned by a particular user
	 * 
	 * @param username
	 * @return {@link LogbookEntry} of totals for all logbooks belonging to a user
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public LogbookEntry overallLogbookTotals(String username) {
		List<Logbook> logbooks = logbookDao.getLogbooks(username);
		
		// Create blank logbook entry
		LogbookEntry overallTotals = new LogbookEntry();
		
		// Get all totals for each logbook; add totals to overallTotals
		for(Logbook logbook : logbooks) {
			LogbookEntry totals = logbookTotals(username, logbook.getId());
			
			overallTotals.setInstrumentApproaches((totals.getInstrumentApproaches() + overallTotals.getInstrumentApproaches()));
			overallTotals.setDayLandings((totals.getDayLandings() + overallTotals.getDayLandings()));
			overallTotals.setNightLandings((totals.getNightLandings() + overallTotals.getNightLandings()));
			
			overallTotals.setAirplaneSel((totals.getAirplaneSel() + overallTotals.getAirplaneSel()));
			overallTotals.setAirplaneMel((totals.getAirplaneMel() + overallTotals.getAirplaneMel()));
			overallTotals.setTurbine((totals.getTurbine() + overallTotals.getTurbine()));
			overallTotals.setGlider((totals.getGlider() + overallTotals.getGlider()));
			overallTotals.setRotorcraft((totals.getRotorcraft() + overallTotals.getRotorcraft()));
			
			overallTotals.setNight((totals.getNight() + overallTotals.getNight()));
			overallTotals.setActualInstrument((totals.getActualInstrument() + overallTotals.getActualInstrument()));
			overallTotals.setSimulatedInstrument((totals.getSimulatedInstrument() + overallTotals.getSimulatedInstrument()));
			overallTotals.setGroundTrainer((totals.getGroundTrainer() + overallTotals.getGroundTrainer()));
			
			overallTotals.setCrossCountry((totals.getCrossCountry() + overallTotals.getCrossCountry()));
			overallTotals.setDualReceived((totals.getDualReceived() + overallTotals.getDualReceived()));
			overallTotals.setDualGiven((totals.getDualGiven() + overallTotals.getDualGiven()));
			overallTotals.setPilotInCommand((totals.getPilotInCommand() + overallTotals.getPilotInCommand()));
			overallTotals.setSecondInCommand((totals.getSecondInCommand() + overallTotals.getSecondInCommand()));
			
			overallTotals.setTotalDuration((totals.getTotalDuration() + overallTotals.getTotalDuration()));
		}
		
		return overallTotals;
	}
}

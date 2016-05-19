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
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public void createOrUpdate(Logbook logbook) {
		logbookDao.createOrUpdate(logbook);
	}
	
	@Secured("ROLE_ADMIN")
	public List<Logbook> getAllLogbooks() {
		return logbookDao.getLogbooks();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<Logbook> getLogbooks(String username) {
		return logbookDao.getLogbooks(username);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public Logbook getLogbook(String username, String name) {
		return logbookDao.getLogbook(username, name);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public Logbook getLogbook(String username, int id) {
		return logbookDao.getLogbook(username, id);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(String username, int id) {
		List<LogbookEntry> logbookEntries = logbookEntryDao.getLogbookEntries(id);
		
		// Need to delete all entries belonging to this logbook first
		for(LogbookEntry entry : logbookEntries)
			logbookEntryDao.delete(id, entry.getId());
		
		return logbookDao.delete(username, id);
	}

	public boolean exists(String username, String name) {
		return logbookDao.exists(username, name);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public LogbookEntry logbookTotals(String username, int id) {
		List<LogbookEntry> logbookEntries = logbookEntryDao.getLogbookEntries(id);
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
}

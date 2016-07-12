package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.dao.LogbookEntryDao;

@Service("logbookEntryService")
public class LogbookEntryService {

	@Autowired
	private LogbookEntryDao logbookEntryDao;
	
	/**
	 * Creates or updates {@link LogbookEntry} in database
	 * 
	 * @param logbookEntry
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public void createOrUpdate(LogbookEntry logbookEntry) {
		logbookEntryDao.createOrUpdate(logbookEntry);
	}
	
	/**
	 * @param logbookId
	 * @param pageNumber
	 * @param resultsSize
	 * @return List of {@link LogbookEntry} paginated by pageNumber and resultsSize
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<LogbookEntry> getPaginatedLogbookEntries(int logbookId, 
														 int pageNumber,
														 int resultsSize) {
		return logbookEntryDao.getPaginatedLogbookEntries(logbookId, 
														  pageNumber, 
														  resultsSize);
	}
	
	/**
	 * @param logbookId
	 * @return List of {@link LogbookEntry} objects in a {@link Logbook}
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<LogbookEntry> getAllLogbookEntries(int logbookId) {
		return logbookEntryDao.getAllLogbookEntries(logbookId);
	}
	
	/**
	 * @param logbookId
	 * @param aircraftId
	 * @return List of {@link LogbookEntry} objects for given {@link Logbook} id and {@link Aircraft} id
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<LogbookEntry> getLogbookEntriesByAircraft(int logbookId, int aircraftId) {
		return logbookEntryDao.getLogbookEntriesByAircraft(logbookId, aircraftId);
	}
	
	/**
	 * @param logbookId
	 * @param entryId
	 * @return {@link LogbookEntry} object for given {@link Logbook} id and logbook entry id 
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public LogbookEntry getLogbookEntry(int logbookId, int entryId) {
		return logbookEntryDao.getLogbookEntry(logbookId, entryId);
	}
	
	/**
	 * @param logbookId
	 * @return total number of {@link LogbookEntry} objects in {@link Logbook}
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public Long getTotalNumberLogbookEntries(int logbookId){
		return logbookEntryDao.getTotalNumberLogbookEntries(logbookId);
	}
	
	/**
	 * @param logbookId
	 * @param entryId
	 * @return if {@link LogbookEntry} was deleted from {@link Logbook}
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(int logbookId, int entryId) {
		return logbookEntryDao.delete(logbookId, entryId);
	}
	
	/**
	 * @param logbookId
	 * @param entryId
	 * @return if {@link LogbookEntry} exists in database
	 */
	public boolean exists(int logbookId, int entryId) {
		return logbookEntryDao.exists(logbookId, entryId);
	}
}

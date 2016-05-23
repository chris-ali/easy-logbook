package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.dao.LogbookEntryDao;

@Service("logbookEntryService")
public class LogbookEntryService {

	@Autowired
	private LogbookEntryDao logbookEntryDao;
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public void createOrUpdate(LogbookEntry logbookEntry) {
		logbookEntryDao.createOrUpdate(logbookEntry);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<LogbookEntry> getLogbookEntries(int logbookId) {
		return logbookEntryDao.getLogbookEntries(logbookId);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public List<LogbookEntry> getLogbookEntries(int logbookId, int aircraftId) {
		return logbookEntryDao.getLogbookEntries(logbookId, aircraftId);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public LogbookEntry getLogbookEntry(int logbookId, int entryId) {
		return logbookEntryDao.getLogbookEntry(logbookId, entryId);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(int logbookId, int entryId) {
		return logbookEntryDao.delete(logbookId, entryId);
	}

	public boolean exists(int logbookId, int entryId) {
		return logbookEntryDao.exists(logbookId, entryId);
	}
}

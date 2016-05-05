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

	public boolean exists(String username, int id) {
		return logbookDao.exists(username, id);
	}
}

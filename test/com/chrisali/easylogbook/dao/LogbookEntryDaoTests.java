package com.chrisali.easylogbook.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.beans.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookEntryDaoTests extends DaoTestData implements DaoTests {

	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@Override
	public void testCreateRetrieve() {
		addTestData();

		List<Logbook> logbookList = logbookDao.getLogbooks();
		List<User> users = usersDao.getPaginatedUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList.size());
		assertEquals("Four users should be created and retrieved", 4, users.size());
		
		List<Logbook> logbooksUser1 = logbookDao.getLogbooks(user1.getUsername());
		
		assertEquals("One logbook should belong to user1", 1, logbooksUser1.size());
		
		Logbook logbook = logbookDao.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
		
		assertEquals("Two entries should be in this logbook", 2, (long)logbookEntryDao.getTotalNumberLogbookEntries(logbook1.getId()));
		
		assertEquals("One entry should be in this logbook", 1, (long)logbookEntryDao.getTotalNumberLogbookEntries(logbook2.getId()));
		
		List<LogbookEntry> logbookEntries1 = logbookEntryDao.getPaginatedLogbookEntries(logbook1.getId(), 0, 10);
		assertEquals("Two entries should be in this logbook", 2, logbookEntries1.size());
		
		List<LogbookEntry> logbookEntries2 = logbookEntryDao.getPaginatedLogbookEntries(logbook2.getId(), 0, 10);
		assertEquals("One entry should be in this logbook", 1, logbookEntries2.size());
		
		List<LogbookEntry> logbookEntries3 = logbookEntryDao.getLogbookEntriesByAircraft(logbook2.getId(), aircraft2.getId());
		assertEquals("One entry should be in this logbook belonging to this aircraft", 1, logbookEntries3.size());
		
		List<LogbookEntry> logbookEntries4 = logbookEntryDao.getLogbookEntriesByAircraft(logbook1.getId(), aircraft2.getId());
		assertEquals("Zero entries should be in this logbook belonging to this aircraft", 0, logbookEntries4.size());
	}
	
	@Test
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook entry in logbook1 should exist in database", logbookEntryDao.exists(logbook1.getId(), logbookEntry1.getId()));
		assertFalse("Logbook entry not belonging to logbook1 should not exist in database", logbookEntryDao.exists(logbook1.getId(), logbookEntry2.getId()));
	}
	
	@Test
	@Override
	public void testDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		List<LogbookEntry> logbookEntryList1 = logbookEntryDao.getAllLogbookEntries(logbook2.getId());
		assertEquals("One logbook entry should be in logbook2", 1, logbookEntryList1.size());
		
		assertTrue("Entry belonging to logbook2 should be deleted from database", logbookEntryDao.delete(logbook2.getId(), logbookEntry2.getId()));
		assertFalse("Entry not belonging to logbook1 should not be deleted from database", logbookEntryDao.delete(logbook2.getId(), logbookEntry3.getId()));
		
		List<LogbookEntry> logbookEntryList2 = logbookEntryDao.getAllLogbookEntries(logbook2.getId());
		
		assertEquals("Zero entries should remain in logbook2", 0, logbookEntryList2.size());
		
		assertTrue("logbook2 should be deleted from database", logbookDao.delete(user2.getUsername(), logbook2.getId()));
	}
	
	@Test
	@Override
	public void testUpdate() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		logbookEntry2.setOrigin("KSFO");
		logbookEntry2.setSecondInCommand(3.8f);
		logbookEntry2.setPilotInCommand(9.0f);
		logbookEntryDao.createOrUpdateIntoDb(logbookEntry2);
		LogbookEntry updatedLogbookEntry1 = logbookEntryDao.getLogbookEntry(logbook2.getId(), logbookEntry2.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry2, updatedLogbookEntry1);
		
		logbookEntry3.setAircraft(aircraft1);
		logbookEntryDao.createOrUpdateIntoDb(logbookEntry3);
		LogbookEntry updatedLogbookEntry2 = logbookEntryDao.getLogbookEntry(logbook1.getId(), logbookEntry3.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry3, updatedLogbookEntry2);
	}
	
	@Test
	public void testTotaling() {
		addTestData();
		
		float totalTimeForAircraft1 = aircraftDao.loggedTimeAircraft(aircraft1.getId());
		float totalTimeForAircraft2 = aircraftDao.loggedTimeAircraft(aircraft2.getId());
		
		assertEquals("Total time for aircraft 1 is  0.8", 0.8f, totalTimeForAircraft1, 0.1f);
		assertEquals("Total time for aircraft 2 is 12.8", 12.8f, totalTimeForAircraft2, 0.1f);
	}
}

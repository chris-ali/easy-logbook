package com.chrisali.easylogbook.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.AircraftDao;
import com.chrisali.easylogbook.dao.LogbookDao;
import com.chrisali.easylogbook.dao.LogbookEntryDao;
import com.chrisali.easylogbook.dao.UsersDao;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookEntryDaoTests {
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private AircraftDao aircraftDao;
	
	@Autowired
	private LogbookDao logbookDao;
	
	@Autowired
	private LogbookEntryDao logbookEntryDao;
	
	@Autowired
	private DataSource dataSource;
	
	// Test Users
	private User user1 = new User("johnwpurcell", "John Purcell", "hellothere", "john@test.com", 
									true, "ROLE_USER");
	private User user2 = new User("richardhannay", "Richard Hannay", "the39steps", "richard@test.com", 
									true, "ROLE_ADMIN");
	
	private Logbook logbook1 = new Logbook(user1, "MyLogbook");
	private Logbook logbook2 = new Logbook(user2, "MyLogbook");
	private Logbook logbook3 = new Logbook(user1, "MyLogbook");
	
	private Aircraft aircraft1 = new Aircraft(user1, "Cessna", "152", "N89061");
	private Aircraft aircraft2 = new Aircraft(user2, "Boeing", "777", "JA6055");
	private Aircraft aircraft3 = new Aircraft(user1, "Piper", "Seneca", "D-EATH");
	
	private LogbookEntry logbookEntry1 = new LogbookEntry(logbook1, aircraft1, new String("2016-05-04"));
	private LogbookEntry logbookEntry2 = new LogbookEntry(logbook2, aircraft2, new String("2011-02-26"));
	private LogbookEntry logbookEntry3 = new LogbookEntry(logbook1, aircraft3, new String("1995-12-18"));
	
	private void addTestData() {
		usersDao.createOrUpdate(user1);
		usersDao.createOrUpdate(user2);
		
		aircraftDao.createOrUpdate(aircraft1);
		aircraftDao.createOrUpdate(aircraft2);
		aircraftDao.createOrUpdate(aircraft3);
		
		logbookDao.createOrUpdate(logbook1);
		logbookDao.createOrUpdate(logbook2);
		logbookDao.createOrUpdate(logbook3);
		
		logbookEntry1.setOrigin("KTTN");
		logbookEntry1.setDestination("KRDG");
		logbookEntry1.setDayLandings(1);
		logbookEntry1.setAirplaneSel(0.8f);
		logbookEntry1.setPilotInCommand(0.8f);
		logbookEntry1.setDualGiven(0.8f);
		logbookEntry1.setTotalDuration(0.8f);
		logbookEntryDao.createOrUpdate(logbookEntry1);
		
		logbookEntry2.setOrigin("KEWR");
		logbookEntry2.setDestination("RJAA");
		logbookEntry2.setDayLandings(1);
		logbookEntry2.setAirplaneMel(12.8f);
		logbookEntry2.setTurbine(12.8f);
		logbookEntry2.setActualInstrument(12.8f);
		logbookEntry2.setPilotInCommand(12.8f);
		logbookEntry2.setTotalDuration(12.8f);
		logbookEntryDao.createOrUpdate(logbookEntry2);
		
		logbookEntry3.setOrigin("N87");
		logbookEntry3.setDestination("CYTZ");
		logbookEntry3.setDayLandings(1);
		logbookEntry3.setAirplaneSel(2.8f);
		logbookEntry3.setActualInstrument(2.8f);
		logbookEntry3.setPilotInCommand(2.8f);
		logbookEntry3.setTotalDuration(2.8f);
		logbookEntryDao.createOrUpdate(logbookEntry3);
		
	}
	
	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		
		jdbc.execute("delete from logbook_entries");
		jdbc.execute("delete from logbooks");
		jdbc.execute("delete from aircraft");
		jdbc.execute("delete from users");
	}
	
	@Test
	public void testCreateRetrieve() {
		addTestData();

		List<Logbook> logbookList = logbookDao.getLogbooks();
		List<User> users = usersDao.getAllUsers();
		
		assertEquals("Three logbooks should be created and retrieved", 3, logbookList.size());
		assertEquals("Two users should be created and retrieved", 2, users.size());
		
		List<Logbook> logbooksUser1 = logbookDao.getLogbooks(user1.getUsername());
		
		assertEquals("Two logbooks should belong to user1", 2, logbooksUser1.size());
		
		Logbook logbook = logbookDao.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
		
		List<LogbookEntry> logbookEntries1 = logbookEntryDao.getLogbookEntries(logbook1.getId());
		assertEquals("Two entries should be in this logbook", 2, logbookEntries1.size());
		
		List<LogbookEntry> logbookEntries2 = logbookEntryDao.getLogbookEntries(logbook2.getId());
		assertEquals("One entry should be in this logbook", 1, logbookEntries2.size());
	}
	
	@Test
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook entry in logbook1 should exist in database", logbookEntryDao.exists(logbook1.getId(), logbookEntry1.getId()));
		assertFalse("Logbook entry not belonging to logbook1 should not exist in database", logbookEntryDao.exists(logbook1.getId(), logbookEntry2.getId()));
	}
	
	@Test
	public void testDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		assertEquals("Three logbooks should be created and retrieved", 3, logbookList1.size());
		
		List<LogbookEntry> logbookEntryList1 = logbookEntryDao.getLogbookEntries(logbook2.getId());
		assertEquals("One logbook entry should be in logbook2", 1, logbookEntryList1.size());
		
		assertTrue("Entry belonging to logbook2 should be deleted from database", logbookEntryDao.delete(logbook2.getId(), logbookEntry2.getId()));
		assertFalse("Entry not belonging to logbook1 should not be deleted from database", logbookEntryDao.delete(logbook2.getId(), logbookEntry3.getId()));
		
		List<LogbookEntry> logbookEntryList2 = logbookEntryDao.getLogbookEntries(logbook2.getId());
		
		assertEquals("Zero entries should remain in logbook2", 0, logbookEntryList2.size());
		
		assertTrue("logbook2 should be deleted from database", logbookDao.delete(user2.getUsername(), logbook2.getId()));
	}
	
	@Test
	public void testUpdate() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		assertEquals("Three logbooks should be created and retrieved", 3, logbookList1.size());
		
		logbookEntry2.setOrigin("KSFO");
		logbookEntry2.setSecondInCommand(3.8f);
		logbookEntry2.setPilotInCommand(9.0f);
		logbookEntryDao.createOrUpdate(logbookEntry2);
		LogbookEntry updatedLogbookEntry1 = logbookEntryDao.getLogbookEntry(logbook2.getId(), logbookEntry2.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry2, updatedLogbookEntry1);
		
		logbookEntry3.setAircraft(aircraft1);
		logbookEntryDao.createOrUpdate(logbookEntry3);
		LogbookEntry updatedLogbookEntry2 = logbookEntryDao.getLogbookEntry(logbook1.getId(), logbookEntry3.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry3, updatedLogbookEntry2);
	}
}

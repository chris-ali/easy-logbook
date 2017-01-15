package com.chrisali.easylogbook.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import com.chrisali.easylogbook.model.Logbook;
import com.chrisali.easylogbook.model.LogbookEntry;
import com.chrisali.easylogbook.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/config/dao-context.xml",
									"classpath:com/chrisali/easylogbook/config/service-context.xml",
									"classpath:com/chrisali/easylogbook/config/security-context.xml" })
@TestExecutionListeners(listeners = { ServletTestExecutionListener.class,
								   	  DependencyInjectionTestExecutionListener.class,
								      DirtiesContextTestExecutionListener.class,
								      TransactionalTestExecutionListener.class,
								      WithSecurityContextTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookEntryServiceTests extends ServiceTestData implements ServiceTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	@Override
	public void testCreateRetrieve() {
		addTestData();

		List<Logbook> logbookList = logbookService.getAllLogbooks();
		List<User> users = usersService.getPaginatedUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList.size());
		assertEquals("Four users should be created and retrieved", 4, users.size());
		
		List<Logbook> logbooksUser1 = logbookService.getLogbooks(user1.getUsername());
		
		assertEquals("One logbook should belong to user1", 1, logbooksUser1.size());
		
		Logbook logbook = logbookService.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
		
		assertEquals("Two entries should be in this logbook", 2, (long)logbookEntryService.getTotalNumberLogbookEntries(logbook1.getId()));
		
		assertEquals("One entry should be in this logbook", 1, (long)logbookEntryService.getTotalNumberLogbookEntries(logbook2.getId()));
		
		List<LogbookEntry> logbookEntries1 = logbookEntryService.getPaginatedLogbookEntries(logbook1.getId(), 0, 10);
		assertEquals("Two entries should be in this logbook", 2, logbookEntries1.size());
		
		List<LogbookEntry> logbookEntries2 = logbookEntryService.getPaginatedLogbookEntries(logbook2.getId(), 0, 10);
		assertEquals("One entry should be in this logbook", 1, logbookEntries2.size());
		
		List<LogbookEntry> logbookEntries3 = logbookEntryService.getLogbookEntriesByAircraft(logbook2.getId(), aircraft2.getId());
		assertEquals("One entry should be in this logbook belonging to this aircraft", 1, logbookEntries3.size());
		
		List<LogbookEntry> logbookEntries4 = logbookEntryService.getLogbookEntriesByAircraft(logbook1.getId(), aircraft2.getId());
		assertEquals("Zero entries should be in this logbook belonging to this aircraft", 0, logbookEntries4.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testCreateRetrieveNoAuth() {
		testCreateRetrieve();
	}
	
	@Test
	@WithMockUser(username="admin", roles="USER")
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook entry in logbook1 should exist in database", logbookEntryService.exists(logbook1.getId(), logbookEntry1.getId()));
		assertFalse("Logbook entry not belonging to logbook1 should not exist in database", logbookEntryService.exists(logbook1.getId(), logbookEntry2.getId()));
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testExistsNoAuth() {
		testExists();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	@Override
	public void testDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		List<LogbookEntry> logbookEntryList1 = logbookEntryService.getAllLogbookEntries(logbook2.getId());
		assertEquals("One logbook entry should be in logbook2", 1, logbookEntryList1.size());
		
		assertTrue("Entry belonging to logbook2 should be deleted from database", logbookEntryService.delete(logbook2.getId(), logbookEntry2.getId()));
		assertFalse("Entry not belonging to logbook1 should not be deleted from database", logbookEntryService.delete(logbook2.getId(), logbookEntry3.getId()));
		
		List<LogbookEntry> logbookEntryList2 = logbookEntryService.getAllLogbookEntries(logbook2.getId());
		
		assertEquals("Zero entries should remain in logbook2", 0, logbookEntryList2.size());
		
		assertTrue("logbook2 should be deleted from database", logbookService.delete(user2.getUsername(), logbook2.getId()));
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testDeleteNoAuth() {
		testDelete();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	@Override
	public void testUpdate() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		logbookEntry2.setOrigin("KSFO");
		logbookEntry2.setSecondInCommand(3.8f);
		logbookEntry2.setPilotInCommand(9.0f);
		logbookEntryService.createOrUpdate(logbookEntry2);
		LogbookEntry updatedLogbookEntry1 = logbookEntryService.getLogbookEntry(logbook2.getId(), logbookEntry2.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry2, updatedLogbookEntry1);
		
		logbookEntry3.setAircraft(aircraft1);
		logbookEntryService.createOrUpdate(logbookEntry3);
		LogbookEntry updatedLogbookEntry2 = logbookEntryService.getLogbookEntry(logbook1.getId(), logbookEntry3.getId());
		
		assertEquals("Logbook entries should be equal", logbookEntry3, updatedLogbookEntry2);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testUpdateNoAuth() {
		testUpdate();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testTotaling() {
		addTestData();
		
		float totalTimeForAircraft1 = aircraftService.loggedTimeAircraft(aircraft1.getId());
		float totalTimeForAircraft2 = aircraftService.loggedTimeAircraft(aircraft2.getId());
		
		assertEquals("Total time for aircraft 1 is  0.8", 0.8f, totalTimeForAircraft1, 0.1f);
		assertEquals("Total time for aircraft 2 is 12.8", 12.8f, totalTimeForAircraft2, 0.1f);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testTotalingNoAuth() {
		testTotaling();
	}
}

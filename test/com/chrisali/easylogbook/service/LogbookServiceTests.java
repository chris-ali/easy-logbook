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
public class LogbookServiceTests extends ServiceTestData implements ServiceTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	@Override
	public void testCreateRetrieve() {
		usersService.createOrUpdate(user1);
		
		
		List<User> users1 = usersService.getPaginatedUsers(0, 10);
		
		assertEquals("One user should be created and retrieved", 1, users1.size());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersService.createOrUpdate(user2);
		
		logbookService.createOrUpdate(logbook1);
		logbookService.createOrUpdate(logbook2);
		
		List<Logbook> logbookList1 = logbookService.getAllLogbooks();
		
		assertEquals("Two logbooks should be created and retrieved", 2, logbookList1.size());
		
		usersService.createOrUpdate(user3);
		usersService.createOrUpdate(user4);
		
		logbookService.createOrUpdate(logbook3);
		logbookService.createOrUpdate(logbook4);
		logbookService.createOrUpdate(logbook5);

		List<Logbook> logbookList2 = logbookService.getAllLogbooks();
		List<User> users2 = usersService.getPaginatedUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Logbook> logbooksUser2 = logbookService.getLogbooks(user2.getUsername());
		
		assertEquals("Two logbooks should belong to user2", 2, logbooksUser2.size());
		
		Logbook logbook = logbookService.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testCreateRetrieveNoAuth() {
		testCreateRetrieve();
	}
	
	@Test
	@WithMockUser(username="test", roles="USER")
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook should exist in database", logbookService.exists(user3.getUsername(), logbook4.getName()));
		assertFalse("Logbook not belonging to user 3 should not exist in database", logbookService.exists(user3.getUsername(), "123456"));
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
		
		assertTrue("Logbook belonging to user4 should be deleted from database", logbookService.delete(user4.getUsername(), logbook5.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", logbookService.delete(user4.getUsername(), logbook2.getId()));
		
		List<Logbook> logbookList2 = logbookService.getLogbooks(user4.getUsername());
		
		assertEquals("Zero logbooks should belong to user4", 0, logbookList2.size());
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
		
		List<Logbook> logbookList2 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		
		logbook2.setName("MyUpdatedLogbook");
		logbookService.createOrUpdate(logbook2);
		Logbook updatedLogbook = logbookService.getLogbook(user2.getUsername(), logbook2.getId());
		
		assertEquals("Logbooks should be equal", logbook2, updatedLogbook);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testUpdateNoAuth() {
		testUpdate();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testLogbookTotals() {
		addTestData();
		
		LogbookEntry totals = logbookService.logbookTotals(user1.getUsername(), logbook1.getId());
		
		assertEquals("Total landings in logbook should be 2", 2, totals.getDayLandings());
		assertEquals("Total PIC in logbook should be 3.6", 3.6f, totals.getPilotInCommand(), 0.1f);
		assertEquals("Total single engine time in logbook should be 3.6", 3.6f, totals.getAirplaneSel(), 0.1f);
		assertEquals("Total dual given in logbook should be 0.8", 0.8f, totals.getDualGiven(), 0.1f);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testLogbookTotalsNoAuth() {
		testLogbookTotals();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testOverallLogbookTotals() {
		addTestData();
		
		LogbookEntry overallTotals = logbookService.overallLogbookTotals(user2.getUsername());
		
		assertEquals("Total landings in logbooks should be 1", 1, overallTotals.getDayLandings());
		assertEquals("Total PIC in logbooks should be 12.8", 12.8f, overallTotals.getPilotInCommand(), 0.1f);
		assertEquals("Total insturment time logbooks should be 12.8", 12.8f, overallTotals.getActualInstrument(), 0.1f);
		assertEquals("Total time logbooks should be 12.8", 12.8f, overallTotals.getTotalDuration(), 0.1f);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testOverallLogbookTotalsNoAuth() {
		testLogbookTotals();
	}
}

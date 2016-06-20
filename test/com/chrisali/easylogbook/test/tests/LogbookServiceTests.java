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

import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.LogbookService;
import com.chrisali.easylogbook.services.UsersService;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/service-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
							       DependencyInjectionTestExecutionListener.class,
							       DirtiesContextTestExecutionListener.class,
							       TransactionalTestExecutionListener.class,
							       WithSecurityContextTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookServiceTests {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private LogbookService logbookService;
	
	@Autowired
	private DataSource dataSource;
	
	// Test Users
	private User user1 = new User("johnwpurcell", "John Purcell", "hello", "john@test.com", 
			true, "ROLE_USER");
	private User user2 = new User("richardhannay", "Richard Hannay", "the39steps", "richard@test.com", 
				true, "ROLE_ADMIN");
	private User user3 = new User("iloveviolins", "Sue Black", "suetheviolinist", "sue@test.com", 
				true, "ROLE_USER");
	private User user4 = new User("liberator", "Rog Blake", "rogerblake", "rog@test.com", 
				false, "user");
	
	private Logbook logbook1 = new Logbook(user1, "MyLogbook");
	private Logbook logbook2 = new Logbook(user2, "MyLogbook");
	private Logbook logbook3 = new Logbook(user2, "MyLogbook");
	private Logbook logbook4 = new Logbook(user3, "MyLogbook");
	private Logbook logbook5 = new Logbook(user4, "MyLogbook");
	
	private void addTestData() {
		usersService.createOrUpdate(user1);
		usersService.createOrUpdate(user2);
		usersService.createOrUpdate(user3);
		usersService.createOrUpdate(user4);
		
		logbookService.createOrUpdate(logbook1);
		logbookService.createOrUpdate(logbook2);
		logbookService.createOrUpdate(logbook3);
		logbookService.createOrUpdate(logbook4);
		logbookService.createOrUpdate(logbook5);
	}
	
	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		
		jdbc.execute("delete from logbook_entries");
		jdbc.execute("delete from logbooks");
		jdbc.execute("delete from aircraft");
		jdbc.execute("delete from pilot_details");
		jdbc.execute("delete from users");
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testAdminCreateRetrieve() {
		usersService.createOrUpdate(user1);
		
		
		List<User> users1 = usersService.getAllUsers(0, 10);
		
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
		List<User> users2 = usersService.getAllUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Logbook> logbooksUser2 = logbookService.getLogbooks(user2.getUsername());
		
		assertEquals("Two logbooks should belong to user2", 2, logbooksUser2.size());
		
		Logbook logbook = logbookService.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthCreateRetrieve() {
		usersService.createOrUpdate(user1);
		
		
		List<User> users1 = usersService.getAllUsers(0, 10);
		
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
		List<User> users2 = usersService.getAllUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Logbook> logbooksUser2 = logbookService.getLogbooks(user2.getUsername());
		
		assertEquals("Two logbooks should belong to user2", 2, logbooksUser2.size());
		
		Logbook logbook = logbookService.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
	}
	
	@Test
	@WithMockUser(username="test", roles="USER")
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook should exist in database", logbookService.exists(user3.getUsername(), logbook4.getName()));
		assertFalse("Logbook not belonging to user 3 should not exist in database", logbookService.exists(user3.getUsername(), "123456"));
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthExists() {
		addTestData();
		
		assertTrue("Logbook should exist in database", logbookService.exists(user3.getUsername(), logbook4.getName()));
		assertFalse("Logbook not belonging to user 3 should not exist in database", logbookService.exists(user3.getUsername(), "123456"));
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testAdminDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		assertTrue("Logbook belonging to user4 should be deleted from database", logbookService.delete(user4.getUsername(), logbook5.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", logbookService.delete(user4.getUsername(), logbook2.getId()));
		
		List<Logbook> logbookList2 = logbookService.getLogbooks(user4.getUsername());
		
		assertEquals("Zero logbooks should belong to user4", 0, logbookList2.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		assertTrue("Logbook belonging to user4 should be deleted from database", logbookService.delete(user4.getUsername(), logbook5.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", logbookService.delete(user4.getUsername(), logbook2.getId()));
		
		List<Logbook> logbookList2 = logbookService.getLogbooks(user4.getUsername());
		
		assertEquals("Zero logbooks should belong to user4", 0, logbookList2.size());
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testAdminUpdate() {
		addTestData();
		
		List<Logbook> logbookList2 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		
		logbook2.setName("MyUpdatedLogbook");
		logbookService.createOrUpdate(logbook2);
		Logbook updatedLogbook = logbookService.getLogbook(user2.getUsername(), logbook2.getId());
		
		assertEquals("Logbooks should be equal", logbook2, updatedLogbook);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthUpdate() {
		addTestData();
		
		List<Logbook> logbookList2 = logbookService.getAllLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		
		logbook2.setName("MyUpdatedLogbook");
		logbookService.createOrUpdate(logbook2);
		Logbook updatedLogbook = logbookService.getLogbook(user2.getUsername(), logbook2.getId());
		
		assertEquals("Logbooks should be equal", logbook2, updatedLogbook);
	}
}

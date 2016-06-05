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

import com.chrisali.easylogbook.beans.Logbook;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.LogbookDao;
import com.chrisali.easylogbook.dao.UsersDao;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookDaoTests {
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private LogbookDao logbookDao;
	
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
		usersDao.createOrUpdate(user1);
		usersDao.createOrUpdate(user2);
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
		
		logbookDao.createOrUpdate(logbook1);
		logbookDao.createOrUpdate(logbook2);
		logbookDao.createOrUpdate(logbook3);
		logbookDao.createOrUpdate(logbook4);
		logbookDao.createOrUpdate(logbook5);
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
	public void testCreateRetrieve() {
		usersDao.createOrUpdate(user1);
		
		
		List<User> users1 = usersDao.getAllUsers();
		
		assertEquals("One user should be created and retrieved", 1, users1.size());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersDao.createOrUpdate(user2);
		
		logbookDao.createOrUpdate(logbook1);
		logbookDao.createOrUpdate(logbook2);
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		
		assertEquals("Two logbooks should be created and retrieved", 2, logbookList1.size());
		
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
		
		logbookDao.createOrUpdate(logbook3);
		logbookDao.createOrUpdate(logbook4);
		logbookDao.createOrUpdate(logbook5);

		List<Logbook> logbookList2 = logbookDao.getLogbooks();
		List<User> users2 = usersDao.getAllUsers();
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Logbook> logbooksUser2 = logbookDao.getLogbooks(user2.getUsername());
		
		assertEquals("Two logbooks should belong to user2", 2, logbooksUser2.size());
		
		Logbook logbook = logbookDao.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
	}
	
	@Test
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook should exist in database", logbookDao.exists(user3.getUsername(), logbook4.getName()));
		assertFalse("Logbook not belonging to user 3 should not exist in database", logbookDao.exists(user3.getUsername(), "123456"));
	}
	
	@Test
	public void testDelete() {
		addTestData();
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList1.size());
		
		assertTrue("Logbook belonging to user4 should be deleted from database", logbookDao.delete(user4.getUsername(), logbook5.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", logbookDao.delete(user4.getUsername(), logbook2.getId()));
		
		List<Logbook> logbookList2 = logbookDao.getLogbooks(user4.getUsername());
		
		assertEquals("Zero logbooks should belong to user4", 0, logbookList2.size());
	}
	
	@Test
	public void testUpdate() {
		addTestData();
		
		List<Logbook> logbookList2 = logbookDao.getLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		
		logbook2.setName("MyUpdatedLogbook");
		logbookDao.createOrUpdate(logbook2);
		Logbook updatedLogbook = logbookDao.getLogbook(user2.getUsername(), logbook2.getId());
		
		assertEquals("Logbooks should be equal", logbook2, updatedLogbook);
	}
}

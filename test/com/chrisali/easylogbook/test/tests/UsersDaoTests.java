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

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.UsersDao;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UsersDaoTests {
	
	@Autowired
	private UsersDao usersDao;
	
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
	
	private void addTestData() {
		usersDao.createOrUpdate(user1);
		usersDao.createOrUpdate(user2);
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
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
		
		List<User> users1 = usersDao.getAllUsers(0, 10);
		
		assertEquals("One user should be created and retrieved", 1, users1.size());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersDao.createOrUpdate(user2);
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
		
		List<User> users2 = usersDao.getAllUsers(0, 10);
		
		assertEquals("Four users should be created and retrieved", 4, users2.size());
	}
	
	@Test
	public void testExists() {
		addTestData();
		
		assertTrue("User should exist in database", usersDao.exists(user2.getUsername()));
		assertFalse("User should not exist in database", usersDao.exists("notAUser"));
	}
	
	@Test
	public void testDelete() {
		addTestData();
		
		List<User> users1 = usersDao.getAllUsers(0, 10);
		assertEquals("Four users should be created and retrieved", 4, users1.size());
		
		assertTrue("User be deleted from database", usersDao.delete(user2.getUsername()));
		assertTrue("User be deleted from database", usersDao.delete(user1.getUsername()));
		
		List<User> users2 = usersDao.getAllUsers(0, 10);
		
		assertEquals("Two users should be left in database", 2, users2.size());
	}
	
	@Test
	public void testUpdate() {
		addTestData();
		
		List<User> users1 = usersDao.getAllUsers(0, 10);
		assertEquals("Four users should be created and retrieved", 4, users1.size());
		
		user2.setName("Chris Ali");
		usersDao.createOrUpdate(user2);
		User updatedUser2 = usersDao.getUser(user2.getUsername());
		
		assertEquals("Users should be equal", user2, updatedUser2);
		
		user3.setEmail("test@test.com");
		usersDao.createOrUpdate(user3);
		User updatedUser3 = usersDao.getUser(user3.getUsername());
		
		assertEquals("Users should be equal", user3, updatedUser3);
	}
}

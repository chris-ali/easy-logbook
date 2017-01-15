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

import com.chrisali.easylogbook.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/config/dao-context.xml",
									"classpath:com/chrisali/easylogbook/config/security-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UsersDaoTests extends DaoTestData implements DaoTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@Override
	public void testCreateRetrieve() {
		usersDao.createOrUpdateIntoDb(user1);
		
		List<User> users1 = usersDao.getPaginatedUsers(0, 10);
		
		assertEquals("One user should be created and retrieved", 1, (long)usersDao.getTotalNumberUsers());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersDao.createOrUpdateIntoDb(user2);
		usersDao.createOrUpdateIntoDb(user3);
		usersDao.createOrUpdateIntoDb(user4);
		
		assertEquals("Four users should be created and retrieved", 4, (long)usersDao.getTotalNumberUsers());
	}
	
	@Test
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("User should exist in database", usersDao.exists(user2.getUsername()));
		assertFalse("User should not exist in database", usersDao.exists("notAUser"));
	}
	
	@Test
	@Override
	public void testDelete() {
		addTestData();
		
		assertEquals("Four users should be created and retrieved", 4, (long)usersDao.getTotalNumberUsers());
		
		assertTrue("User be deleted from database", usersDao.delete(user2.getUsername()));
		assertTrue("User be deleted from database", usersDao.delete(user1.getUsername()));
		
		assertEquals("Two users should be left in database", 2, (long)usersDao.getTotalNumberUsers());
	}
	
	@Test
	@Override
	public void testUpdate() {
		addTestData();

		assertEquals("Four users should be created and retrieved", 4, (long)usersDao.getTotalNumberUsers());
		
		user2.setName("Chris Ali");
		usersDao.createOrUpdateIntoDb(user2);
		User updatedUser2 = usersDao.getUser(user2.getUsername());
		
		assertEquals("Users should be equal", user2, updatedUser2);
		
		user3.setEmail("test@test.com");
		usersDao.createOrUpdateIntoDb(user3);
		User updatedUser3 = usersDao.getUser(user3.getUsername());
		
		assertEquals("Users should be equal", user3, updatedUser3);
	}
}

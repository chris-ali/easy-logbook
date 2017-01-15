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

import com.chrisali.easylogbook.model.Logbook;
import com.chrisali.easylogbook.model.User;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/config/dao-context.xml",
									"classpath:com/chrisali/easylogbook/config/security-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class LogbookDaoTests extends DaoTestData implements DaoTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@Override
	public void testCreateRetrieve() {
		usersDao.createOrUpdateIntoDb(user1);
		
		
		List<User> users1 = usersDao.getPaginatedUsers(0, 10);
		
		assertEquals("One user should be created and retrieved", 1, users1.size());
		assertEquals("Inserted user should match retrieved", user1, users1.get(0));
		
		usersDao.createOrUpdateIntoDb(user2);
		
		logbookDao.createOrUpdateIntoDb(logbook1);
		logbookDao.createOrUpdateIntoDb(logbook2);
		
		List<Logbook> logbookList1 = logbookDao.getLogbooks();
		
		assertEquals("Two logbooks should be created and retrieved", 2, logbookList1.size());
		
		usersDao.createOrUpdateIntoDb(user3);
		usersDao.createOrUpdateIntoDb(user4);
		
		logbookDao.createOrUpdateIntoDb(logbook3);
		logbookDao.createOrUpdateIntoDb(logbook4);
		logbookDao.createOrUpdateIntoDb(logbook5);

		List<Logbook> logbookList2 = logbookDao.getLogbooks();
		List<User> users2 = usersDao.getPaginatedUsers(0, 10);
		
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Logbook> logbooksUser2 = logbookDao.getLogbooks(user2.getUsername());
		
		assertEquals("Two logbooks should belong to user2", 2, logbooksUser2.size());
		
		Logbook logbook = logbookDao.getLogbook(user1.getUsername(), logbook1.getId());
		assertEquals("User 1's retrieved aircraft should match logbook1", logbook, logbook1);
	}
	
	@Test
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Logbook should exist in database", logbookDao.exists(user3.getUsername(), logbook4.getName()));
		assertFalse("Logbook not belonging to user 3 should not exist in database", logbookDao.exists(user3.getUsername(), "123456"));
	}
	
	@Test
	@Override
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
	@Override
	public void testUpdate() {
		addTestData();
		
		List<Logbook> logbookList2 = logbookDao.getLogbooks();
		assertEquals("Five logbooks should be created and retrieved", 5, logbookList2.size());
		
		logbook2.setName("MyUpdatedLogbook");
		logbookDao.createOrUpdateIntoDb(logbook2);
		Logbook updatedLogbook = logbookDao.getLogbook(user2.getUsername(), logbook2.getId());
		
		assertEquals("Logbooks should be equal", logbook2, updatedLogbook);
	}
}

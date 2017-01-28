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
import org.springframework.test.context.web.WebAppConfiguration;

import com.chrisali.easylogbook.config.ApplicationConfig;
import com.chrisali.easylogbook.config.DataSourceTestConfig;
import com.chrisali.easylogbook.config.SecurityConfig;
import com.chrisali.easylogbook.config.WebAppInitializer;
import com.chrisali.easylogbook.config.WebMvcConfig;
import com.chrisali.easylogbook.model.Aircraft;
import com.chrisali.easylogbook.model.User;

@ActiveProfiles("test")
@ContextConfiguration(classes = { ApplicationConfig.class, 
								  DataSourceTestConfig.class, 
								  SecurityConfig.class, 
								  WebMvcConfig.class, 
								  WebAppInitializer.class })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AircraftDaoTests extends DaoTestData implements DaoTests {

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
		
		aircraftDao.createOrUpdateIntoDb(aircraft1);
		aircraftDao.createOrUpdateIntoDb(aircraft3);
		
		List<Aircraft> aircraftList1 = aircraftDao.getAircraft();
		
		assertEquals("Two aircraft should be created and retrieved", 2, aircraftList1.size());
		
		usersDao.createOrUpdateIntoDb(user2);
		usersDao.createOrUpdateIntoDb(user3);
		usersDao.createOrUpdateIntoDb(user4);
		
		aircraftDao.createOrUpdateIntoDb(aircraft2);
		aircraftDao.createOrUpdateIntoDb(aircraft4);
		aircraftDao.createOrUpdateIntoDb(aircraft5);
		aircraftDao.createOrUpdateIntoDb(aircraft6);
		aircraftDao.createOrUpdateIntoDb(aircraft7);

		List<Aircraft> aircraftList2 = aircraftDao.getAircraft();
		List<User> users2 = usersDao.getPaginatedUsers(0, 10);
		
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Aircraft> aircraftUser4 = aircraftDao.getAircraft(user4.getUsername());
		
		assertEquals("Three aircraft should belong to user 4", 3, aircraftUser4.size());
		
		Aircraft aircraft = aircraftDao.getAircraft(user1.getUsername(), aircraft1.getId());
		assertEquals("User 1's retrieved aircraft should match aircraft2", aircraft, aircraft1);
	}
	
	@Test
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Aircraft should exist in database", aircraftDao.exists(user3.getUsername(), aircraft4.getTailNumber()));
		assertFalse("Aircraft not belonging to user 3 should not exist in database", aircraftDao.exists(user3.getUsername(), "123456"));
	}
	
	@Test
	@Override
	public void testDelete() {
		addTestData();
		
		List<Aircraft> aircraftList1 = aircraftDao.getAircraft();
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList1.size());
		
		assertTrue("Aircraft G-CLLD belonging to user4 should be deleted from database", aircraftDao.delete(user4.getUsername(), aircraft6.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", aircraftDao.delete(user4.getUsername(), aircraft1.getId()));
		
		List<Aircraft> aircraftList2 = aircraftDao.getAircraft(user4.getUsername());
		
		assertEquals("Two aircraft should belong to user4", 2, aircraftList2.size());
	}
	
	@Test
	@Override
	public void testUpdate() {
		addTestData();
		
		List<Aircraft> aircraftList1 = aircraftDao.getAircraft();
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList1.size());
		
		aircraft5.setMake("Lockheed");
		aircraft5.setMake("L1011");
		aircraftDao.createOrUpdateIntoDb(aircraft5);
		Aircraft updatedAircraft1 = aircraftDao.getAircraft(user4.getUsername(), aircraft5.getId());
		
		assertEquals("Aircraft should be equal", aircraft5, updatedAircraft1);
		
		aircraft1.setTailNumber("N6267Q");
		aircraftDao.createOrUpdateIntoDb(aircraft1);
		Aircraft updatedAircraft2 = aircraftDao.getAircraft(user1.getUsername(), aircraft1.getId());
		
		assertEquals("Aircraft should be equal", aircraft1, updatedAircraft2);
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

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
public class AircraftServiceTests extends ServiceTestData implements ServiceTests {

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
		
		aircraftService.createOrUpdate(aircraft1);
		aircraftService.createOrUpdate(aircraft2);
		
		List<Aircraft> aircraftList1 = aircraftService.getAllAircraft();
		
		assertEquals("Two aircraft should be created and retrieved", 2, aircraftList1.size());
		
		usersService.createOrUpdate(user2);
		usersService.createOrUpdate(user3);
		usersService.createOrUpdate(user4);
		
		aircraftService.createOrUpdate(aircraft3);
		aircraftService.createOrUpdate(aircraft4);
		aircraftService.createOrUpdate(aircraft5);
		aircraftService.createOrUpdate(aircraft6);
		aircraftService.createOrUpdate(aircraft7);

		List<Aircraft> aircraftList2 = aircraftService.getAllAircraft();
		List<User> users2 = usersService.getPaginatedUsers(0, 10);
		
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Aircraft> aircraftUser4 = aircraftService.getAircraft(user4.getUsername());
		
		assertEquals("Three aircraft should belong to user 4", 3, aircraftUser4.size());
		
		Aircraft aircraft = aircraftService.getAircraft(user2.getUsername(), aircraft2.getId());
		assertEquals("User 2's retrieved aircraft should match aircraft2", aircraft, aircraft2);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testCreateRetrieveNoAuth() {
		testCreateRetrieve();
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	@Override
	public void testExists() {
		addTestData();
		
		assertTrue("Aircraft should exist in database", aircraftService.exists(user3.getUsername(), aircraft4.getTailNumber()));
		assertFalse("Aircraft not belonging to user 3 should not exist in database", aircraftService.exists(user3.getUsername(), "123456"));
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
		
		List<Aircraft> aircraftList1 = aircraftService.getAllAircraft();
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList1.size());
		
		assertTrue("Aircraft G-CLLD belonging to user4 should be deleted from database", aircraftService.delete(user4.getUsername(), aircraft6.getId()));
		assertFalse("Aircraft not belonging to user4 should not be deleted from database", aircraftService.delete(user4.getUsername(), aircraft1.getId()));
		
		List<Aircraft> aircraftList2 = aircraftService.getAircraft(user4.getUsername());
		
		assertEquals("Two aircraft should belong to user4", 2, aircraftList2.size());
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
		
		List<Aircraft> aircraftList1 = aircraftService.getAllAircraft();
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList1.size());
		
		aircraft5.setMake("Lockheed");
		aircraft5.setMake("L1011");
		aircraftService.createOrUpdate(aircraft5);
		Aircraft updatedAircraft1 = aircraftService.getAircraft(user4.getUsername(), aircraft5.getId());
		
		assertEquals("Aircraft should be equal", aircraft5, updatedAircraft1);
		
		aircraft1.setTailNumber("N6267Q");
		aircraftService.createOrUpdate(aircraft1);
		Aircraft updatedAircraft2 = aircraftService.getAircraft(user1.getUsername(), aircraft1.getId());
		
		assertEquals("Aircraft should be equal", aircraft1, updatedAircraft2);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testUpdateNoAuth() {
		testUpdate();
	}
	
	@Test
	@WithMockUser(username="admin", roles={"USER","ADMIN"})
	public void testTotalling() {
		addTestData();
		
		float totalTimeForAircraft1 = aircraftService.loggedTimeAircraft(aircraft1.getId());
		float totalTimeForAircraft2 = aircraftService.loggedTimeAircraft(aircraft2.getId());
		
		assertEquals("Total time for aircraft 1 is  0.8", 0.8f, totalTimeForAircraft1, 0.1f);
		assertEquals("Total time for aircraft 2 is 12.8", 12.8f, totalTimeForAircraft2, 0.1f);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testTotallingNoAuth() {
		testTotalling();
	}
}

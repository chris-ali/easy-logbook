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
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.AircraftDao;
import com.chrisali.easylogbook.dao.UsersDao;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AircraftDaoTests {
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private AircraftDao aircraftDao;
	
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
				true, "user");
	
	private Aircraft aircraft1 = new Aircraft(user1, "Cessna", "152", "N89061");
	private Aircraft aircraft2 = new Aircraft(user1, "Cessna", "172", "N6379F");
	private Aircraft aircraft3 = new Aircraft(user2, "Piper", "Seneca", "D-EATH");
	private Aircraft aircraft4 = new Aircraft(user3, "Piper", "Archer", "N12345");
	private Aircraft aircraft5 = new Aircraft(user4, "Boeing", "777", "JA7065");
	private Aircraft aircraft6 = new Aircraft(user4, "Airbus", "A321", "G-CLLD");
	private Aircraft aircraft7 = new Aircraft(user4, "Piper", "Archer", "C-TOPS");
	
	private void addTestData() {
		usersDao.createOrUpdate(user1);
		usersDao.createOrUpdate(user2);
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
		
		aircraftDao.createOrUpdate(aircraft1);
		aircraftDao.createOrUpdate(aircraft2);
		aircraftDao.createOrUpdate(aircraft3);
		aircraftDao.createOrUpdate(aircraft4);
		aircraftDao.createOrUpdate(aircraft5);
		aircraftDao.createOrUpdate(aircraft6);
		aircraftDao.createOrUpdate(aircraft7);
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
		
		aircraftDao.createOrUpdate(aircraft1);
		aircraftDao.createOrUpdate(aircraft2);
		
		List<Aircraft> aircraftList1 = aircraftDao.getAircraft();
		
		assertEquals("Two aircraft should be created and retrieved", 2, aircraftList1.size());
		
		usersDao.createOrUpdate(user2);
		usersDao.createOrUpdate(user3);
		usersDao.createOrUpdate(user4);
		
		aircraftDao.createOrUpdate(aircraft3);
		aircraftDao.createOrUpdate(aircraft4);
		aircraftDao.createOrUpdate(aircraft5);
		aircraftDao.createOrUpdate(aircraft6);
		aircraftDao.createOrUpdate(aircraft7);

		List<Aircraft> aircraftList2 = aircraftDao.getAircraft();
		List<User> users2 = usersDao.getAllUsers(0, 10);
		
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList2.size());
		assertEquals("Four users should be created and retrieved", 4, users2.size());
		
		List<Aircraft> aircraftUser4 = aircraftDao.getAircraft(user4.getUsername());
		
		assertEquals("Three aircraft should belong to user 4", 3, aircraftUser4.size());
		
		Aircraft aircraft = aircraftDao.getAircraft(user1.getUsername(), aircraft2.getId());
		assertEquals("User 1's retrieved aircraft should match aircraft2", aircraft, aircraft2);
	}
	
	@Test
	public void testExists() {
		addTestData();
		
		assertTrue("Aircraft should exist in database", aircraftDao.exists(user3.getUsername(), aircraft4.getTailNumber()));
		assertFalse("Aircraft not belonging to user 3 should not exist in database", aircraftDao.exists(user3.getUsername(), "123456"));
	}
	
	@Test
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
	public void testUpdate() {
		addTestData();
		
		List<Aircraft> aircraftList1 = aircraftDao.getAircraft();
		assertEquals("Seven aircraft should be created and retrieved", 7, aircraftList1.size());
		
		aircraft5.setMake("Lockheed");
		aircraft5.setMake("L1011");
		aircraftDao.createOrUpdate(aircraft5);
		Aircraft updatedAircraft1 = aircraftDao.getAircraft(user4.getUsername(), aircraft5.getId());
		
		assertEquals("Aircraft should be equal", aircraft5, updatedAircraft1);
		
		aircraft1.setTailNumber("N6267Q");
		aircraftDao.createOrUpdate(aircraft1);
		Aircraft updatedAircraft2 = aircraftDao.getAircraft(user1.getUsername(), aircraft1.getId());
		
		assertEquals("Aircraft should be equal", aircraft1, updatedAircraft2);
	}
}

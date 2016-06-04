package com.chrisali.easylogbook.test.tests;

import static org.junit.Assert.*;

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

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.beans.enums.CategoryRating;
import com.chrisali.easylogbook.beans.enums.ClassRating;
import com.chrisali.easylogbook.beans.enums.PilotExamination;
import com.chrisali.easylogbook.beans.enums.PilotLicense;
import com.chrisali.easylogbook.beans.enums.PilotMedical;
import com.chrisali.easylogbook.dao.PilotDetailsDao;
import com.chrisali.easylogbook.dao.UsersDao;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PilotDetailsDaoTests {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private PilotDetailsDao pilotDetailsDao;
	
	@Autowired
	private DataSource dataSource;
	
	// Test Users
	private User user1 = new User("johnwpurcell", "John Purcell", "hellothere", "john@test.com", 
									true, "ROLE_USER");
	private User user2 = new User("richardhannay", "Richard Hannay", "the39steps", "richard@test.com", 
									true, "ROLE_ADMIN");
	
	private PilotDetail detail1 = new PilotDetail(user1, "2016-06-02");
	private PilotDetail detail2 = new PilotDetail(user1, "2012-04-20");
	private PilotDetail detail3 = new PilotDetail(user1, "2010-10-15");
	private PilotDetail detail4 = new PilotDetail(user1, "2016-06-02");
	private PilotDetail detail5 = new PilotDetail(user1, "2012-04-20");
	private PilotDetail detail6 = new PilotDetail(user1, "2010-10-15");
	
	private PilotDetail detail7 = new PilotDetail(user2, "2016-06-02");
	private PilotDetail detail8 = new PilotDetail(user2, "2012-04-20");
	private PilotDetail detail9 = new PilotDetail(user2, "2010-10-15");
	
	private void addTestData() {
		usersDao.createOrUpdate(user1);
		usersDao.createOrUpdate(user2);
		
		detail1.setPilotLicense(PilotLicense.PRIVATE);
		detail1.setCategoryRating(CategoryRating.AIRPLANE);
		detail1.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsDao.createOrUpdate(detail1);
		
		detail2.setPilotMedical(PilotMedical.FIRST_CLASS);
		pilotDetailsDao.createOrUpdate(detail2);
		
		detail3.setEndorsement("Complex");
		pilotDetailsDao.createOrUpdate(detail3);
		
		detail4.setPilotLicense(PilotLicense.INSTRUMENT);
		detail4.setCategoryRating(CategoryRating.AIRPLANE);
		pilotDetailsDao.createOrUpdate(detail4);
		
		detail5.setPilotLicense(PilotLicense.COMMERCIAL);
		detail5.setCategoryRating(CategoryRating.AIRPLANE);
		detail5.setClassRating(ClassRating.MULTILAND);
		pilotDetailsDao.createOrUpdate(detail5);
		
		detail6.setTypeRating("CL600");
		pilotDetailsDao.createOrUpdate(detail6);
		
		detail7.setPilotExamination(PilotExamination.CHECKRIDE);
		pilotDetailsDao.createOrUpdate(detail7);
		
		detail8.setPilotLicense(PilotLicense.PRIVATE);
		detail8.setCategoryRating(CategoryRating.AIRPLANE);
		detail8.setClassRating(ClassRating.SINGLESEA);
		pilotDetailsDao.createOrUpdate(detail8);
		
		detail9.setPilotMedical(PilotMedical.THIRD_CLASS);
		pilotDetailsDao.createOrUpdate(detail9);
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
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsDao.getPilotDetails(user1.getUsername());
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsDao.getPilotDetails(user2.getUsername());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList2.size());
		
		List<PilotDetail> pilotDetailsList3 = pilotDetailsDao.getPilotLicenseDetails(user1.getUsername());
		assertEquals("Three pilot license entries should exist", 3, pilotDetailsList3.size());
		
		List<PilotDetail> pilotDetailsList4 = pilotDetailsDao.getPilotMedicalDetails(user1.getUsername());
		assertEquals("One pilot medical entry should exist", 1, pilotDetailsList4.size());
		
		List<PilotDetail> pilotDetailsList5 = pilotDetailsDao.getPilotTypeRatingDetails(user1.getUsername());
		assertEquals("One type rating entry should exist", 1, pilotDetailsList5.size());
		
		List<PilotDetail> pilotDetailsList6 = pilotDetailsDao.getPilotEndorsementDetails(user1.getUsername());
		assertEquals("One endorsement entry should exist", 1, pilotDetailsList6.size());
		
		List<PilotDetail> pilotDetailsList7 = pilotDetailsDao.getPilotExamDetails(user2.getUsername());
		assertEquals("One pilot exam entry should exist", 1, pilotDetailsList7.size());
	}
	
	@Test
	public void testUpdate() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsDao.getPilotDetails(user2.getUsername());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList1.size());
		
		detail8.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsDao.createOrUpdate(detail8);
		PilotDetail updatedDetail8 = pilotDetailsDao.getPilotDetail(user2.getUsername(), detail8.getId());
		
		assertEquals("Pilot details should be equal", detail8, updatedDetail8);
		
		detail6.setDate("2016-06-04");
		pilotDetailsDao.createOrUpdate(detail6);
		PilotDetail updatedDetail6 = pilotDetailsDao.getPilotDetail(user1.getUsername(), detail6.getId());
		
		assertEquals("Pilot details should be equal", detail6, updatedDetail6);
	}
	
	@Test
	public void testDelete() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsDao.getPilotDetails(user1.getUsername());
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		assertTrue("Detail belonging to user 1 should be deleted from database", pilotDetailsDao.delete(user1.getUsername(), detail3.getId()));
		assertFalse("Detail not belonging to user 1 should not be deleted from database", pilotDetailsDao.delete(user1.getUsername(), detail8.getId()));
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsDao.getPilotDetails(user1.getUsername());
		List<PilotDetail> pilotDetailsList3 = pilotDetailsDao.getPilotDetails(user2.getUsername());
		
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList2.size());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList3.size());
	}
}

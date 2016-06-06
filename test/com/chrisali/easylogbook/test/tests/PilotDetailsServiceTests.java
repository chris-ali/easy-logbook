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

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.beans.enums.CategoryRating;
import com.chrisali.easylogbook.beans.enums.ClassRating;
import com.chrisali.easylogbook.beans.enums.PilotExamination;
import com.chrisali.easylogbook.beans.enums.PilotLicense;
import com.chrisali.easylogbook.beans.enums.PilotMedical;
import com.chrisali.easylogbook.services.PilotDetailsService;
import com.chrisali.easylogbook.services.PilotDetailsService.PilotDetailsType;
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
public class PilotDetailsServiceTests {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PilotDetailsService pilotDetailsService;
	
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
		usersService.createOrUpdate(user1);
		usersService.createOrUpdate(user2);
		
		detail1.setPilotLicense(PilotLicense.PRIVATE);
		detail1.setCategoryRating(CategoryRating.AIRPLANE);
		detail1.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsService.createOrUpdate(detail1);
		
		detail2.setPilotMedical(PilotMedical.FIRST_CLASS);
		pilotDetailsService.createOrUpdate(detail2);
		
		detail3.setEndorsement("Complex");
		pilotDetailsService.createOrUpdate(detail3);
		
		detail4.setPilotLicense(PilotLicense.INSTRUMENT);
		detail4.setCategoryRating(CategoryRating.AIRPLANE);
		pilotDetailsService.createOrUpdate(detail4);
		
		detail5.setPilotLicense(PilotLicense.COMMERCIAL);
		detail5.setCategoryRating(CategoryRating.AIRPLANE);
		detail5.setClassRating(ClassRating.MULTILAND);
		pilotDetailsService.createOrUpdate(detail5);
		
		detail6.setTypeRating("CL600");
		pilotDetailsService.createOrUpdate(detail6);
		
		detail7.setPilotExamination(PilotExamination.CHECKRIDE);
		pilotDetailsService.createOrUpdate(detail7);
		
		detail8.setPilotLicense(PilotLicense.PRIVATE);
		detail8.setCategoryRating(CategoryRating.AIRPLANE);
		detail8.setClassRating(ClassRating.SINGLESEA);
		pilotDetailsService.createOrUpdate(detail8);
		
		detail9.setPilotMedical(PilotMedical.THIRD_CLASS);
		pilotDetailsService.createOrUpdate(detail9);
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
	@WithMockUser(username="user", roles={"USER"})
	public void testCreateRetrieve() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList2.size());
		
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.LICENSES);
		assertEquals("Three pilot license entries should exist", 3, pilotDetailsList3.size());
		
		List<PilotDetail> pilotDetailsList4 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.MEDICALS);
		assertEquals("One pilot medical entry should exist", 1, pilotDetailsList4.size());
		
		List<PilotDetail> pilotDetailsList5 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.TYPERATINGS);
		assertEquals("One type rating entry should exist", 1, pilotDetailsList5.size());
		
		List<PilotDetail> pilotDetailsList6 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ENDORSEMENTS);
		assertEquals("One endorsement entry should exist", 1, pilotDetailsList6.size());
		
		List<PilotDetail> pilotDetailsList7 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.EXAMINATIONS);
		assertEquals("One pilot exam entry should exist", 1, pilotDetailsList7.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthCreateRetrieve() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList2.size());
		
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.LICENSES);
		assertEquals("Three pilot license entries should exist", 3, pilotDetailsList3.size());
		
		List<PilotDetail> pilotDetailsList4 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.MEDICALS);
		assertEquals("One pilot medical entry should exist", 1, pilotDetailsList4.size());
		
		List<PilotDetail> pilotDetailsList5 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.TYPERATINGS);
		assertEquals("One type rating entry should exist", 1, pilotDetailsList5.size());
		
		List<PilotDetail> pilotDetailsList6 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ENDORSEMENTS);
		assertEquals("One endorsement entry should exist", 1, pilotDetailsList6.size());
		
		List<PilotDetail> pilotDetailsList7 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.EXAMINATIONS);
		assertEquals("One pilot exam entry should exist", 1, pilotDetailsList7.size());
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	public void testUpdate() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList1.size());
		
		detail8.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsService.createOrUpdate(detail8);
		PilotDetail updatedDetail8 = pilotDetailsService.getPilotDetail(user2.getUsername(), detail8.getId());
		
		assertEquals("Pilot details should be equal", detail8, updatedDetail8);
		
		detail6.setDate("2016-06-04");
		pilotDetailsService.createOrUpdate(detail6);
		PilotDetail updatedDetail6 = pilotDetailsService.getPilotDetail(user1.getUsername(), detail6.getId());
		
		assertEquals("Pilot details should be equal", detail6, updatedDetail6);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthUpdate() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList1.size());
		
		detail8.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsService.createOrUpdate(detail8);
		PilotDetail updatedDetail8 = pilotDetailsService.getPilotDetail(user2.getUsername(), detail8.getId());
		
		assertEquals("Pilot details should be equal", detail8, updatedDetail8);
		
		detail6.setDate("2016-06-04");
		pilotDetailsService.createOrUpdate(detail6);
		PilotDetail updatedDetail6 = pilotDetailsService.getPilotDetail(user1.getUsername(), detail6.getId());
		
		assertEquals("Pilot details should be equal", detail6, updatedDetail6);
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	public void testDelete() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		assertTrue("Detail belonging to user 1 should be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail3.getId()));
		assertFalse("Detail not belonging to user 1 should not be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail8.getId()));
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList2.size());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList3.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoAuthDelete() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		assertTrue("Detail belonging to user 1 should be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail3.getId()));
		assertFalse("Detail not belonging to user 1 should not be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail8.getId()));
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList2.size());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList3.size());
	}
}

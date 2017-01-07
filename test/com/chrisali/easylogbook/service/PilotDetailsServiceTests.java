package com.chrisali.easylogbook.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.chrisali.easylogbook.beans.enums.ClassRating;
import com.chrisali.easylogbook.beans.enums.PilotExamination;
import com.chrisali.easylogbook.services.PilotDetailsService.PilotDetailsType;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/service-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/config/datasource.xml" })
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
							       DependencyInjectionTestExecutionListener.class,
							       DirtiesContextTestExecutionListener.class,
							       TransactionalTestExecutionListener.class,
							       WithSecurityContextTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PilotDetailsServiceTests extends ServiceTestData implements ServiceTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	@Override
	public void testCreateRetrieve() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList2.size());
		
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.LICENSES);
		assertEquals("Three pilot license entries should exist", 3, pilotDetailsList3.size());
		
		List<PilotDetail> pilotDetailsList4 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.MEDICALS);
		assertEquals("One pilot medical entry should exist", 1, pilotDetailsList4.size());
		
		List<PilotDetail> pilotDetailsList5 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.TYPERATINGS);
		assertEquals("One type rating entry should exist", 1, pilotDetailsList5.size());
		
		List<PilotDetail> pilotDetailsList6 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ENDORSEMENTS);
		assertEquals("One endorsement entry should exist", 1, pilotDetailsList6.size());
		
		List<PilotDetail> pilotDetailsList7 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.EXAMINATIONS);
		assertEquals("Three pilot exam entries should exist", 3, pilotDetailsList7.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testCreateRetrieveNoAuth() {
		testCreateRetrieve();
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	@Override
	public void testUpdate() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList1.size());
		
		detail8.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsService.createOrUpdate(detail8);
		PilotDetail updatedDetail8 = pilotDetailsService.getPilotDetail(user2.getUsername(), detail8.getId());
		
		assertEquals("Pilot details should be equal", detail8, updatedDetail8);
		
		detail6.setDate(LocalDate.of(2016, 6, 4));
		pilotDetailsService.createOrUpdate(detail6);
		PilotDetail updatedDetail6 = pilotDetailsService.getPilotDetail(user1.getUsername(), detail6.getId());
		
		assertEquals("Pilot details should be equal", detail6, updatedDetail6);
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testUpdateNoAuth() {
		testUpdate();
	}
	
	@Override
	public void testExists() {}
	
	@Override
	public void testExistsNoAuth() {}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	@Override
	public void testDelete() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		assertEquals("Six pilot details entries should exist", 6, pilotDetailsList1.size());
		
		assertTrue("Detail belonging to user 1 should be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail3.getId()));
		assertFalse("Detail not belonging to user 1 should not be deleted from database", pilotDetailsService.delete(user1.getUsername(), detail8.getId()));
		
		List<PilotDetail> pilotDetailsList2 = pilotDetailsService.getPilotDetails(user1.getUsername(), PilotDetailsType.ALL);
		List<PilotDetail> pilotDetailsList3 = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.ALL);
		
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList2.size());
		assertEquals("Five pilot details entries should exist", 5, pilotDetailsList3.size());
	}
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	@Override
	public void testDeleteNoAuth() {
		testDelete();
	}
	
	@Test
	@WithMockUser(username="user", roles={"USER"})
	public void testUpcomingExpirations() {
		addTestData();
		
		List<PilotDetail> pilotExaminationDetails = pilotDetailsService.getPilotDetails(user2.getUsername(), PilotDetailsType.EXAMINATIONS);
		Set<PilotExamination> upcomingExpirations = pilotDetailsService.getUpcomingExpirations(pilotExaminationDetails, LocalDate.of(2016, 6, 2));
		
		assertTrue("Set of upcoming expirations contains BFR", upcomingExpirations.contains(PilotExamination.BFR));
		assertTrue("Set of upcoming expirations contains PIC", upcomingExpirations.contains(PilotExamination.PIC));
	}
}

package com.chrisali.easylogbook.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.enums.ClassRating;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:com/chrisali/easylogbook/configs/dao-context.xml",
									"classpath:com/chrisali/easylogbook/configs/security-context.xml",
									"classpath:com/chrisali/easylogbook/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PilotDetailsDaoTests extends DaoTestData implements DaoTests {
	
	@Before
	public void init() {
		clearDatabase();
	}
	
	@Test
	@Override
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
	@Override
	public void testExists() {};
	
	@Test
	@Override
	public void testUpdate() {
		addTestData();
		
		List<PilotDetail> pilotDetailsList1 = pilotDetailsDao.getPilotDetails(user2.getUsername());
		assertEquals("Three pilot details entries should exist", 3, pilotDetailsList1.size());
		
		detail8.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsDao.createOrUpdateIntoDb(detail8);
		PilotDetail updatedDetail8 = pilotDetailsDao.getPilotDetail(user2.getUsername(), detail8.getId());
		
		assertEquals("Pilot details should be equal", detail8, updatedDetail8);
		
		detail6.setDate(LocalDate.of(2016, 6, 4));
		pilotDetailsDao.createOrUpdateIntoDb(detail6);
		PilotDetail updatedDetail6 = pilotDetailsDao.getPilotDetail(user1.getUsername(), detail6.getId());
		
		assertEquals("Pilot details should be equal", detail6, updatedDetail6);
	}
	
	@Test
	@Override
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

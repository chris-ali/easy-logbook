package com.chrisali.easylogbook.test.tests;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.beans.enums.CategoryRatings;
import com.chrisali.easylogbook.beans.enums.ClassRatings;
import com.chrisali.easylogbook.beans.enums.PilotExaminations;
import com.chrisali.easylogbook.beans.enums.PilotLicenses;
import com.chrisali.easylogbook.beans.enums.PilotMedicals;
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
		
		detail1.setPilotLicenses(PilotLicenses.PRIVATE);
		detail1.setCategoryRatings(CategoryRatings.AIRPLANE);
		detail1.setClassRatings(ClassRatings.SINGLELAND);
		pilotDetailsDao.createOrUpdate(detail1);
		
		detail2.setPilotMedicals(PilotMedicals.FIRST_CLASS);
		pilotDetailsDao.createOrUpdate(detail2);
		
		detail3.setEndorsement("Complex");
		pilotDetailsDao.createOrUpdate(detail3);
		
		detail4.setPilotLicenses(PilotLicenses.INSTRUMENT);
		detail4.setCategoryRatings(CategoryRatings.AIRPLANE);
		pilotDetailsDao.createOrUpdate(detail4);
		
		detail5.setPilotLicenses(PilotLicenses.COMMERCIAL);
		detail5.setCategoryRatings(CategoryRatings.AIRPLANE);
		detail5.setClassRatings(ClassRatings.MULTILAND);
		pilotDetailsDao.createOrUpdate(detail5);
		
		detail6.setTypeRating("CL600");
		pilotDetailsDao.createOrUpdate(detail6);
		
		detail7.setPilotExaminations(PilotExaminations.CHECKRIDE);
		pilotDetailsDao.createOrUpdate(detail7);
		
		detail8.setPilotLicenses(PilotLicenses.PRIVATE);
		detail8.setCategoryRatings(CategoryRatings.AIRPLANE);
		detail8.setClassRatings(ClassRatings.SINGLESEA);
		pilotDetailsDao.createOrUpdate(detail8);
		
		detail9.setPilotMedicals(PilotMedicals.THIRD_CLASS);
		pilotDetailsDao.createOrUpdate(detail9);
	}
	
	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		
		jdbc.execute("delete from users");
		jdbc.execute("delete from pilot_details");
	}
	
	
}

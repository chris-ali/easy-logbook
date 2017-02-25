package com.chrisali.easylogbook.service;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.chrisali.easylogbook.model.Aircraft;
import com.chrisali.easylogbook.model.Logbook;
import com.chrisali.easylogbook.model.LogbookEntry;
import com.chrisali.easylogbook.model.PilotDetail;
import com.chrisali.easylogbook.model.User;
import com.chrisali.easylogbook.model.enums.CategoryRating;
import com.chrisali.easylogbook.model.enums.ClassRating;
import com.chrisali.easylogbook.model.enums.PilotExamination;
import com.chrisali.easylogbook.model.enums.PilotLicense;
import com.chrisali.easylogbook.model.enums.PilotMedical;
import com.chrisali.easylogbook.model.utilities.DateUtilities;

/**
 * Contains all Service and dataSource beans, and contains preparation methods for DAO unit testing  
 * 
 * @author Christopher Ali
 *
 */
public class ServiceTestData {
	
	@Autowired
	@Qualifier("testDataSource")
	protected DataSource dataSource;

	@Autowired
	protected UsersService usersService;
	
	@Autowired
	protected AircraftService aircraftService;
	
	@Autowired
	protected LogbookService logbookService;
	
	@Autowired
	protected LogbookEntryService logbookEntryService;
	
	@Autowired
	protected PilotDetailsService pilotDetailsService;
		
	protected User user1 = new User("johnwpurcell", "John Purcell", "hellothere", "john@test.com", 
									true, "ROLE_USER");
	protected User user2 = new User("richardhannay", "Richard Hannay", "the39steps", "richard@test.com", 
									true, "ROLE_ADMIN");
	protected User user3 = new User("iloveviolins", "Sue Black", "suetheviolinist", "sue@test.com", 
									true, "ROLE_USER");
	protected User user4 = new User("liberator", "Rog Blake", "rogerblake", "rog@test.com", 
									true, "user");
	
	protected Logbook logbook1 = new Logbook(user1, "MyLogbook");
	protected Logbook logbook2 = new Logbook(user2, "MyLogbook");
	protected Logbook logbook3 = new Logbook(user2, "MyLogbook");
	protected Logbook logbook4 = new Logbook(user3, "MyLogbook");
	protected Logbook logbook5 = new Logbook(user4, "MyLogbook");
	
	protected Aircraft aircraft1 = new Aircraft(user1, "Cessna", "152", "N89061");
	protected Aircraft aircraft2 = new Aircraft(user2, "Boeing", "777", "JA6055");
	protected Aircraft aircraft3 = new Aircraft(user1, "Piper", "Seneca", "D-EATH");
	protected Aircraft aircraft4 = new Aircraft(user3, "Piper", "Archer", "N12345");
	protected Aircraft aircraft5 = new Aircraft(user4, "Boeing", "777", "JA7065");
	protected Aircraft aircraft6 = new Aircraft(user4, "Airbus", "A321", "G-CLLD");
	protected Aircraft aircraft7 = new Aircraft(user4, "Piper", "Archer", "C-TOPS");
	
	protected LogbookEntry logbookEntry1 = new LogbookEntry(logbook1, aircraft1, DateUtilities.dateFromYearMonthDay(2016, 05, 04));
	protected LogbookEntry logbookEntry2 = new LogbookEntry(logbook2, aircraft2, DateUtilities.dateFromYearMonthDay(2011, 02, 26));
	protected LogbookEntry logbookEntry3 = new LogbookEntry(logbook1, aircraft3, DateUtilities.dateFromYearMonthDay(1995, 12, 18));
	
	protected PilotDetail detail1 = new PilotDetail(user1);
	protected PilotDetail detail2 = new PilotDetail(user1);
	protected PilotDetail detail3 = new PilotDetail(user1);
	protected PilotDetail detail4 = new PilotDetail(user1);
	protected PilotDetail detail5 = new PilotDetail(user1);
	protected PilotDetail detail6 = new PilotDetail(user1);
	protected PilotDetail detail7 = new PilotDetail(user2);
	protected PilotDetail detail8 = new PilotDetail(user2);
	protected PilotDetail detail9 = new PilotDetail(user2);
	protected PilotDetail detail10 = new PilotDetail(user2);
	protected PilotDetail detail11 = new PilotDetail(user2);
	
	protected LocalDate examDate;
	
	/**
	 * Adds all data to database needed for a test
	 */
	protected void addTestData() {
		usersService.createOrUpdate(user1);
		usersService.createOrUpdate(user2);
		usersService.createOrUpdate(user3);
		usersService.createOrUpdate(user4);
		
		aircraftService.createOrUpdate(aircraft1);
		aircraftService.createOrUpdate(aircraft2);
		aircraftService.createOrUpdate(aircraft3);
		aircraftService.createOrUpdate(aircraft4);
		aircraftService.createOrUpdate(aircraft5);
		aircraftService.createOrUpdate(aircraft6);
		aircraftService.createOrUpdate(aircraft7);
		
		logbookService.createOrUpdate(logbook1);
		logbookService.createOrUpdate(logbook2);
		logbookService.createOrUpdate(logbook3);
		logbookService.createOrUpdate(logbook4);
		logbookService.createOrUpdate(logbook5);
		
		logbookEntry1.setOrigin("KTTN");
		logbookEntry1.setDestination("KRDG");
		logbookEntry1.setDayLandings(1);
		logbookEntry1.setAirplaneSel(0.8f);
		logbookEntry1.setPilotInCommand(0.8f);
		logbookEntry1.setDualGiven(0.8f);
		logbookEntry1.setTotalDuration(0.8f);
		logbookEntryService.createOrUpdate(logbookEntry1);
		
		logbookEntry2.setOrigin("KEWR");
		logbookEntry2.setDestination("RJAA");
		logbookEntry2.setDayLandings(1);
		logbookEntry2.setAirplaneMel(12.8f);
		logbookEntry2.setTurbine(12.8f);
		logbookEntry2.setActualInstrument(12.8f);
		logbookEntry2.setPilotInCommand(12.8f);
		logbookEntry2.setTotalDuration(12.8f);
		logbookEntryService.createOrUpdate(logbookEntry2);
		
		logbookEntry3.setOrigin("N87");
		logbookEntry3.setDestination("CYTZ");
		logbookEntry3.setDayLandings(1);
		logbookEntry3.setAirplaneSel(2.8f);
		logbookEntry3.setActualInstrument(2.8f);
		logbookEntry3.setPilotInCommand(2.8f);
		logbookEntry3.setTotalDuration(2.8f);
		logbookEntryService.createOrUpdate(logbookEntry3);
		
		detail1.setDate(DateUtilities.dateFromYearMonthDay(2016, 6, 2));
		detail1.setPilotLicense(PilotLicense.PRIVATE);
		detail1.setCategoryRating(CategoryRating.AIRPLANE);
		detail1.setClassRating(ClassRating.SINGLELAND);
		pilotDetailsService.createOrUpdate(detail1);
		
		detail2.setDate(DateUtilities.dateFromYearMonthDay(2012, 4, 20));
		detail2.setPilotMedical(PilotMedical.FIRST_CLASS);
		pilotDetailsService.createOrUpdate(detail2);
		
		detail3.setDate(DateUtilities.dateFromYearMonthDay(2010, 10, 15));
		detail3.setEndorsement("Complex");
		pilotDetailsService.createOrUpdate(detail3);
		
		detail4.setDate(DateUtilities.dateFromYearMonthDay(2016, 6, 2));
		detail4.setPilotLicense(PilotLicense.INSTRUMENT);
		detail4.setCategoryRating(CategoryRating.AIRPLANE);
		pilotDetailsService.createOrUpdate(detail4);
		
		detail5.setDate(DateUtilities.dateFromYearMonthDay(2012, 4, 20));
		detail5.setPilotLicense(PilotLicense.COMMERCIAL);
		detail5.setCategoryRating(CategoryRating.AIRPLANE);
		detail5.setClassRating(ClassRating.MULTILAND);
		pilotDetailsService.createOrUpdate(detail5);
		
		detail6.setDate(DateUtilities.dateFromYearMonthDay(2010, 10, 15));
		detail6.setTypeRating("CL600");
		pilotDetailsService.createOrUpdate(detail6);
		
		detail7.setDate(DateUtilities.dateFromYearMonthDay(2016, 6, 2));
		detail7.setPilotExamination(PilotExamination.CHECKRIDE);
		pilotDetailsService.createOrUpdate(detail7);
		
		detail8.setDate(DateUtilities.dateFromYearMonthDay(2012, 4, 20));
		detail8.setPilotLicense(PilotLicense.PRIVATE);
		detail8.setCategoryRating(CategoryRating.AIRPLANE);
		detail8.setClassRating(ClassRating.SINGLESEA);
		pilotDetailsService.createOrUpdate(detail8);
		
		detail9.setDate(DateUtilities.dateFromYearMonthDay(2010, 6, 2));
		detail9.setPilotMedical(PilotMedical.THIRD_CLASS);
		pilotDetailsService.createOrUpdate(detail9);
		
		detail10.setDate(DateUtilities.dateFromYearMonthDay(2014, 6, 15));
		detail10.setPilotExamination(PilotExamination.BFR);
		pilotDetailsService.createOrUpdate(detail10);
		
		detail11.setDate(DateUtilities.dateFromYearMonthDay(2016, 3, 15));
		detail11.setPilotExamination(PilotExamination.PIC);
		pilotDetailsService.createOrUpdate(detail11);
	}
	
	/**
	 * Clears all data from test database 
	 */
	protected void clearDatabase() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		
		jdbc.execute("delete from users");
		jdbc.execute("delete from logbook_entries");
		jdbc.execute("delete from logbooks");
		jdbc.execute("delete from aircraft");
		jdbc.execute("delete from pilot_details");
	}
}

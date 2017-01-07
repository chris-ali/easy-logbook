package com.chrisali.easylogbook.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.PilotDetail;
import com.chrisali.easylogbook.beans.enums.PilotExamination;
import com.chrisali.easylogbook.dao.PilotDetailsDao;

@Service("pilotDetailsService")
public class PilotDetailsService {

	@Autowired
	private PilotDetailsDao pilotDetailsDao;
	
	/**
	 * Enums used as argument to {@link PilotDetailsService#getPilotDetails(String, PilotDetailsType)},
	 * which controls the switch to select the right DAO method to generate a list of {@link PilotDetail}
	 * objects
	 * 
	 * @author Christopher Ali
	 *
	 */
	public enum PilotDetailsType {
		LICENSES,
		MEDICALS,
		EXAMINATIONS,
		ENDORSEMENTS,
		TYPERATINGS,
		ALL;
	}
	
	/**
	 * Creates or updates {@link PilotDetail} object in database 
	 * 
	 * @param pilotDetail
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public void createOrUpdate(PilotDetail pilotDetail) {
		pilotDetailsDao.createOrUpdate(pilotDetail);
	}
	
	/**
	 * Returns a list of {@link PilotDetail} objects depending on the {@link PilotDetailsType}
	 * enum passed in for a specified user
	 * 
	 * @param username
	 * @param detailsType
	 * @return List of {@link PilotDetail} objects
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotDetails(String username, PilotDetailsType detailsType) {
		
		switch (detailsType) {
		case ENDORSEMENTS:
			return pilotDetailsDao.getPilotEndorsementDetails(username);
		case EXAMINATIONS:
			return pilotDetailsDao.getPilotExamDetails(username);
		case LICENSES:
			return pilotDetailsDao.getPilotLicenseDetails(username);
		case MEDICALS:
			return pilotDetailsDao.getPilotMedicalDetails(username);
		case TYPERATINGS:
			return pilotDetailsDao.getPilotTypeRatingDetails(username);
		case ALL:
			return pilotDetailsDao.getPilotDetails(username);
		default:
			return new ArrayList<PilotDetail>();
		}
	}
	
	/**
	 * @param username
	 * @param id of {@link PilotDetail}
	 * @return pilot detail object from database
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public PilotDetail getPilotDetail(String username, int id) {
		return pilotDetailsDao.getPilotDetail(username, id);
	}
	
	/**
	 * @param username
	 * @param id of {@link PilotDetail}
	 * @return if pilot detail was successfully deleted
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public boolean delete(String username, int id) {
		return pilotDetailsDao.delete(username, id);
	}
	
	/**
	 * Creates a Set of {@link PilotDetail} objects that are nearing expiration during the current calendar month.
	 * Compares calendar object argument with each pilot detail in the List argument. If the detail meets the expiration
	 * criteria, it is added to the set
	 * 
	 * @see CFR § 61.57 - Recent flight experience: Pilot in command
	 * @param pilotExaminationDetails
	 * @param currentCalendar
	 * @return Set of pilot detail objects nearing expiration 
	 */
	public Set<PilotExamination> getUpcomingExpirations(List<PilotDetail> pilotExaminationDetails, Calendar currentCalendar) {
		
		Set<PilotExamination> upcomingExpirations = new LinkedHashSet<>();
		int currentMonth = currentCalendar.get(Calendar.MONTH) + 1; // Calendar bitmasks are zero-based
		int currentYear = currentCalendar.get(Calendar.YEAR);
		
		int examMonth = 0;
		int examYear = 0;
		
		for (PilotDetail exam : pilotExaminationDetails) {
					
			if (exam != null) {
				examMonth = exam.getDate().getMonth().getValue();
				examYear = exam.getDate().getYear();
			}
			
			switch(exam.getPilotExamination()) {
			case BFR: // Expires after 24 calendar months
				if ((currentMonth == examMonth) && (currentYear-examYear == 2)) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case CFI_RENEWAL: // Expires after 24 calendar months
				if ((currentMonth == examMonth) && (currentYear-examYear == 2)) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case CHECKRIDE: // Expires after 24 calendar months
				if ((currentMonth == examMonth) && (currentYear-examYear == 2)) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case INSTRUMENT: // Expires after 6 calendar months
				if (((currentMonth+12) - (examMonth+12)) == 6)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case IPC: // Expires after 6 calendar months
				if (((currentMonth+12) - (examMonth+12)) == 6) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case NIGHT: // Expires after 3 calendar months
				if (((currentMonth+12) - (examMonth+12)) == 3)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case PIC: // Expires after 3 calendar months
				if (((currentMonth+12) - (examMonth+12)) == 3)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			default:
				break;
			}
		}
		
		return upcomingExpirations;
	}
}

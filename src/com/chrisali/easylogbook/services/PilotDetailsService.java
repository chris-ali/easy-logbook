package com.chrisali.easylogbook.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
		pilotDetailsDao.createOrUpdateIntoDb(pilotDetail);
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
	 * Compares current date with each pilot detail in the List argument. If the detail meets the expiration
	 * criteria, it is added to the set
	 * 
	 * @see CFR § 61.57 - Recent flight experience: Pilot in command
	 * @param pilotExaminationDetails
	 * @param currentDate
	 * @return Set of pilot detail objects nearing expiration 
	 */
	public Set<PilotExamination> getUpcomingExpirations(List<PilotDetail> pilotExaminationDetails, LocalDate currentDate) {
		
		Set<PilotExamination> upcomingExpirations = new LinkedHashSet<>();
		LocalDate examDate = LocalDate.now();
		Period period = Period.between(examDate, currentDate);

		int yearsBetween = 1;
		int monthsBetween = 1;
		
		for (PilotDetail exam : pilotExaminationDetails) {
			
			if (exam != null) {
				examDate = exam.getDate();
				period = Period.between(examDate, currentDate);

				yearsBetween = period.getYears();
				monthsBetween = period.getMonths();
			}
			
			switch(exam.getPilotExamination()) {
			case BFR: // Expires after 24 calendar months
				if (yearsBetween == 1 && monthsBetween >= 11) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case CFI_RENEWAL: // Expires after 24 calendar months
				if (yearsBetween == 1 && monthsBetween >= 11) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case CHECKRIDE: // Expires after 24 calendar months
				if (yearsBetween == 1 && monthsBetween >= 11) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case INSTRUMENT: // Expires after 6 calendar months
				if (monthsBetween <= 6 && monthsBetween >= 5)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case IPC: // Expires after 6 calendar months
				if (monthsBetween <= 6 && monthsBetween >= 5) 
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case NIGHT: // Expires after 90 days
				if (monthsBetween <= 3 && monthsBetween >= 2)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			case PIC: // Expires after 90 days
				if (monthsBetween <= 3 && monthsBetween >= 2)
					upcomingExpirations.add(exam.getPilotExamination());
				break;
			default:
				break;
			}
		}
		
		return upcomingExpirations;
	}
}

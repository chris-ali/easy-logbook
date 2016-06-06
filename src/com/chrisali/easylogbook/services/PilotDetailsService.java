package com.chrisali.easylogbook.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.PilotDetail;
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
	 * @return list of {@link PilotDetail} objects
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
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public PilotDetail getPilotDetail(String username, int id) {
		return pilotDetailsDao.getPilotDetail(username, id);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public boolean delete(String username, int id) {
		return pilotDetailsDao.delete(username, id);
	}
}

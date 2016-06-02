package com.chrisali.easylogbook.services;

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
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public void createOrUpdate(PilotDetail pilotDetail) {
		pilotDetailsDao.createOrUpdate(pilotDetail);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotDetails(String username) {
		return pilotDetailsDao.getPilotDetails(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotMedicalDetails(String username) {
		return pilotDetailsDao.getPilotMedicalDetails(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotExamDetails(String username) {
		return pilotDetailsDao.getPilotExamDetails(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotLicenseDetails(String username) {
		return pilotDetailsDao.getPilotLicenseDetails(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotTypeRatingDetails(String username) {
		return pilotDetailsDao.getPilotTypeRatingDetails(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<PilotDetail> getPilotEndorsementDetails(String username) {
		return pilotDetailsDao.getPilotEndorsementDetails(username);
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

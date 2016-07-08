package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.dao.AircraftDao;

@Service("aircraftService")
public class AircraftService {

	@Autowired
	private AircraftDao aircraftDao;
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public void createOrUpdate(Aircraft aircraft) {
		aircraftDao.createOrUpdate(aircraft);
	}
	
	@Secured("ROLE_ADMIN")
	public List<Aircraft> getAllAircraft() {
		return aircraftDao.getAircraft();
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<Aircraft> getAircraft(String username) {
		return aircraftDao.getAircraft(username);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, int id) {
		return aircraftDao.getAircraft(username, id);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, String tailNumber) {
		return aircraftDao.getAircraft(username, tailNumber);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public boolean delete(String username, int id) {
		return aircraftDao.delete(username, id);
	}
	
	public boolean exists(String username, String tailNumber) {
		return aircraftDao.exists(username, tailNumber);
	}
	
	/**
	 * Calculates the total time logged on a single aircraft across all aircraft
	 * 
	 * @param aircraftId
	 * @return float of totals for an aircraft
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public float loggedTimeAircraft(int aircraftId) {
		return aircraftDao.loggedTimeAircraft(aircraftId);
	}
}

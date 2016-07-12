package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.Aircraft;
import com.chrisali.easylogbook.beans.LogbookEntry;
import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.AircraftDao;

@Service("aircraftService")
public class AircraftService {

	@Autowired
	private AircraftDao aircraftDao;
	
	/**
	 * Creates or updates {@link Aircraft} object into database
	 * 
	 * @param aircraft
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public void createOrUpdate(Aircraft aircraft) {
		aircraftDao.createOrUpdate(aircraft);
	}
	
	/**
	 * @return List of all {@link Aircraft} in database
	 */
	@Secured("ROLE_ADMIN")
	public List<Aircraft> getAllAircraft() {
		return aircraftDao.getAircraft();
	}
	
	/**
	 * @param username
	 * @return List of {@link Aircraft} belonging to {@link User}
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public List<Aircraft> getAircraft(String username) {
		return aircraftDao.getAircraft(username);
	}
	
	/**
	 * @param username
	 * @param id
	 * @return {@link Aircraft} belonging to {@link User}
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, int id) {
		return aircraftDao.getAircraft(username, id);
	}
	
	/**
	 * @param username
	 * @param tailNumber
	 * @return {@link Aircraft} belonging to {@link User} corresponding to aircraft's tail number
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public Aircraft getAircraft(String username, String tailNumber) {
		return aircraftDao.getAircraft(username, tailNumber);
	}
	
	/**
	 * Deletes {@link Aircraft} object from database
	 * 
	 * @param username
	 * @param id of aircraft
	 * @return if aircraft was successfully deleted from database
	 */
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	public boolean delete(String username, int id) {
		return aircraftDao.delete(username, id);
	}
	
	/**
	 * @param username
	 * @param tailNumber of {@link Aircraft}
	 * @return if aircraft was deleted from database successfully
	 */
	public boolean exists(String username, String tailNumber) {
		return aircraftDao.exists(username, tailNumber);
	}
	
	/**
	 * Calculates the total time logged on a single {@link Aircraft} across all {@link LogbookEntry}
	 * 
	 * @param aircraftId
	 * @return float of totals for an aircraft
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public float loggedTimeAircraft(int aircraftId) {
		return aircraftDao.loggedTimeAircraft(aircraftId);
	}
}

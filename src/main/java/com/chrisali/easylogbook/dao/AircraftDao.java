package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.model.Aircraft;
import com.chrisali.easylogbook.model.LogbookEntry;
import com.chrisali.easylogbook.model.User;

/**
 * DAO that communicates with MySQL using Hibernate to perform CRUD operations on {@link Aircraft} objects
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Repository
@Component("aircraftDao")
public class AircraftDao extends AbstractDao {
	
	/**
	 * Implementation of {@link AbstractDao#createOrUpdate(Object)}
	 * 
	 * @param aircraft
	 */
	public void createOrUpdateIntoDb(Aircraft aircraft) {
		createOrUpdate(aircraft);
	}
		
	/**
	 * @return List of all {@link Aircraft} in database using Hibernate ORM Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<Aircraft> getAircraft() {
		Criteria criteria = getSession().createCriteria(Aircraft.class);
		
		List<Aircraft> aircraft = criteria.list();
		closeSession();
		
		return aircraft;
	}
	
	/**
	 * @param username
	 * @return List of {@link Aircraft} belonging to {@link User} using Hibernate ORM Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<Aircraft> getAircraft(String username) {
		Criteria criteria = getSession().createCriteria(Aircraft.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username));
		
		List<Aircraft> aircraft = criteria.list();
		closeSession();
		
		return aircraft;
	}
	
	/**
	 * @param username
	 * @param id of aircraft
	 * @return specific {@link Aircraft} belonging to {@link User} using Hibernate ORM Criteria
	 */
	public Aircraft getAircraft(String username, int id) {
		Criteria criteria = getSession().createCriteria(Aircraft.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("id", id));
		
		Aircraft aircraft = (Aircraft) criteria.uniqueResult();
		closeSession();
		
		return aircraft;
	}
	
	/**
	 * @param username
	 * @param tailNumber of aircraft
	 * @return specific {@link Aircraft} belonging to {@link User} using Hibernate ORM Criteria
	 */
	public Aircraft getAircraft(String username, String tailNumber) {
		Criteria criteria = getSession().createCriteria(Aircraft.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("tailNumber", tailNumber));
		
		Aircraft aircraft = (Aircraft) criteria.uniqueResult();
		closeSession();
		
		return aircraft;
	}

	/**
	 * @param username
	 * @param id of {@link Aircraft}
	 * @return if aircraft was successfully deleted from database using HQL
	 */
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from Aircraft where id=:id and username=:username");
		query.setInteger("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	/**
	 * @param username
	 * @param tailNumber of {@link Aircraft}
	 * @return if aircraft object exists in database
	 */
	public boolean exists(String username, String tailNumber) {
		return getAircraft(username, tailNumber) != null;
	}
	
	/**
	 * @param id of {@link Aircraft}
	 * @return the sum of totalDuration of all {@link LogbookEntry} tied to this aircraft
	 */
	public float loggedTimeAircraft(int id) {
		Query query = getSession().createQuery("select sum(totalDuration) from LogbookEntry where aircraft_id=:id");
		query.setInteger("id", id);
		
		double totalTime = 0.0;
		
		if(query.uniqueResult() != null)
			totalTime = (double)query.uniqueResult();
		
		closeSession();
		
		return (float) totalTime;
	}
}

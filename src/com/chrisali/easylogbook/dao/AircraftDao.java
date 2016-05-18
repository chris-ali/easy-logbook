package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.beans.Aircraft;

@Transactional
@Repository
@Component("aircraftDao")
public class AircraftDao extends AbstractDao {
	
	@SuppressWarnings("unchecked")
	public List<Aircraft> getAircraft() {
		Criteria criteria = getSession().createCriteria(Aircraft.class);
		
		List<Aircraft> aircraft = criteria.list();
		closeSession();
		
		return aircraft;
	}
	
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
	
	public void createOrUpdate(Aircraft aircraft) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(aircraft);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from Aircraft where id=:id and username=:username");
		query.setInteger("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	public boolean exists(String username, String tailNumber) {
		return getAircraft(username, tailNumber) != null;
	}
}

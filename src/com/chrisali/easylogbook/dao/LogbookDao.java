package com.chrisali.easylogbook.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.beans.Logbook;

@Transactional
@Repository
@Component("logbookDao")
public class LogbookDao extends AbstractDao {
	
	@SuppressWarnings("unchecked")
	public List<Logbook> getLogbooks() {
		Criteria criteria = getSession().createCriteria(Logbook.class);
		
		List<Logbook> Logbook = criteria.list();
		closeSession();
		
		return Logbook;
	}
	
	@SuppressWarnings("unchecked")
	public List<Logbook> getLogbooks(String username) {
		Criteria criteria = getSession().createCriteria(Logbook.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username));
		
		List<Logbook> Logbook = criteria.list();
		closeSession();
		
		return Logbook;
	}
	
	public Logbook getLogbook(String username, String name) {
		Criteria criteria = getSession().createCriteria(Logbook.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("name", name));
		
		Logbook Logbook = (Logbook) criteria.uniqueResult();
		closeSession();
		
		return Logbook;
	}
	
	public Logbook getLogbook(String username, int id) {
		Criteria criteria = getSession().createCriteria(Logbook.class);
		criteria.createAlias("user", "u")
				.add(Restrictions.eq("u.enabled", true))
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.eq("id", id));
		
		Logbook Logbook = (Logbook) criteria.uniqueResult();
		closeSession();
		
		return Logbook;
	}
	
	public void createOrUpdate(Logbook Logbook) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(Logbook);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from Logbook where id=:id and username=:username");
		query.setLong("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	public boolean exists(String username, String name) {
		return getLogbook(username, name) != null;
	}
}

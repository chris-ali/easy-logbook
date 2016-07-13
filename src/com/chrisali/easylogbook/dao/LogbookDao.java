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
import com.chrisali.easylogbook.beans.User;

/**
 * DAO that communicates with MySQL using Hibernate to perform CRUD operations on {@link Logbook} objects
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Repository
@Component("logbookDao")
public class LogbookDao extends AbstractDao {
	
	/**
	 * @return List of {@link Logbook} objects in database using Hibernate Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<Logbook> getLogbooks() {
		Criteria criteria = getSession().createCriteria(Logbook.class);
		
		List<Logbook> Logbook = criteria.list();
		closeSession();
		
		return Logbook;
	}
	
	/**
	 * @param username
	 * @return List of {@link Logbook} belonging to {@link User} using Hibernate Criteria
	 */
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
	
	/**
	 * @param username
	 * @param name of {@link Logbook}
	 * @return logbook belonging to {@link User} using Hibernate Criteria
	 */
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
	
	/**
	 * @param username
	 * @param id of {@link Logbook}
	 * @return logbook belonging to {@link User} using Hibernate Criteria
	 */
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
	
	/**
	 * Creates or updates {@link Logbook} in database using saveOrUpdate() from Session object. 
	 * beginTransaction() starts the process, flush() is called after saveOrUpdate(), then the Transaction
	 * is committed as long as no exception is thrown, in which case the transaction is rolled back, ensuring
	 * ACID behavior of the database
	 * 
	 * @param logbook
	 */
	public void createOrUpdate(Logbook Logbook) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(Logbook);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * @param username
	 * @param id of {@link Logbook}
	 * @return if logbook was successfully deleted from database using HQL
	 */
	public boolean delete(String username, int id) {
		Query query = getSession().createQuery("delete from Logbook where id=:id and username=:username");
		query.setLong("id", id);
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	/**
	 * @param username
	 * @param name of {@link Logbook}
	 * @return if logbook belonging to user exists in database
	 */
	public boolean exists(String username, String name) {
		return getLogbook(username, name) != null;
	}
}

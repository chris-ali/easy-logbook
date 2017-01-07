package com.chrisali.easylogbook.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO superclass that contains Hibernate Session and SessionFactory objects and common methods to
 * open and close database connection secssions
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Component("abstractDao")
@Repository
public abstract class AbstractDao {
	
	@Autowired
	protected SessionFactory sessionFactory;
	protected Session session;
	
	/**
	 * Establishes database connection with Hibernate SessionFactory for subclass DAO objects
	 * 
	 * @return Hibernate session object
	 */
	protected Session getSession() {
		session = null;
		
		try {session = sessionFactory.getCurrentSession();} 
		catch (HibernateException e) {session = sessionFactory.openSession();}
		
		return session;
	}
	
	/**
	 * Closes Hibernate session object
	 */
	protected void closeSession() {session.close();}
	
	/**
	 * Creates or updates Object in database using saveOrUpdate() from Session object. 
	 * beginTransaction() starts the process, flush() is called after saveOrUpdate(), then the Transaction
	 * is committed as long as no exception is thrown, in which case the transaction is rolled back, ensuring
	 * ACID behavior of the database
	 * 
	 * @param obj
	 */
	protected void createOrUpdate(Object obj) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
}

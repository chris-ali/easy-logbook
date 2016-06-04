package com.chrisali.easylogbook.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component("abstractDao")
@Repository
public abstract class AbstractDao {
	
	@Autowired
	protected SessionFactory sessionFactory;
	protected Session session;
	
	protected Session getSession() {
		session = null;
		
		try {session = sessionFactory.getCurrentSession();} 
		catch (HibernateException e) {session = sessionFactory.openSession();}
		
		return session;
	}
	
	protected void closeSession() {session.close();}
}

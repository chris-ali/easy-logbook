package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.beans.User;

@Transactional
@Component("usersDao")
@Repository
public class UsersDao extends AbstractDao {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void createOrUpdate(User user) {
		Transaction tx = null;
		session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			session.saveOrUpdate(user);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		int pageNumber = 0, resultsSize = 10; //Delete me later
		Criteria criteria = getSession().createCriteria(User.class)
				.setMaxResults(resultsSize)
				.setFirstResult(pageNumber * resultsSize)
				.addOrder(Order.asc("username"));
		
		List<User> users = criteria.list();
		
		closeSession();
		
		return users;
	}
	
	public User getUser(String username) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.idEq(username));
		User user = (User) criteria.uniqueResult();
		
		closeSession();
		
		return user;
	}
	
	public boolean delete(String username) {
		Query query = getSession().createQuery("delete from User where username=:username");
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	public boolean exists(String username) {
		return getUser(username) != null;
	}
}

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

/**
 * DAO that communicates with MySQL using Hibernate to perform CRUD operations on {@link User} objects
 * 
 * @author Christopher Ali
 *
 */
@Transactional
@Component("usersDao")
@Repository
public class UsersDao extends AbstractDao {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Creates or updates {@link User} in database using saveOrUpdate() from Session object. Password is encoded using PasswordEncoder.
	 * beginTransaction() starts the process, flush() is called after saveOrUpdate(), then the Transaction
	 * is committed as long as no exception is thrown, in which case the transaction is rolled back, ensuring
	 * ACID behavior of the database
	 * 
	 * @param user
	 */
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
	
	/**
	 * @param pageNumber
	 * @param resultsSize
	 * @return paginated List of all {@link User} in database using Hibernate Criteria 
	 */
	@SuppressWarnings("unchecked")
	public List<User> getPaginatedUsers(int pageNumber, int resultsSize) {
		Criteria criteria = getSession().createCriteria(User.class)
				.setMaxResults(resultsSize)
				.setFirstResult(pageNumber * resultsSize)
				.addOrder(Order.asc("username"));
		
		List<User> users = criteria.list();
		
		closeSession();
		
		return users;
	}
	
	/**
	 * @return total number of {@link User} in database using HQL
	 */
	public Long getTotalNumberUsers() {
		Query criteria = getSession().createQuery("Select count (username) from User");
		Long count = (Long)criteria.uniqueResult();
		
		closeSession();
		
		return count;
	}
	
	/**
	 * @param username
	 * @return specific {@link User} using Hibernate Criteria
	 */
	public User getUser(String username) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.idEq(username));
		User user = (User) criteria.uniqueResult();
		
		closeSession();
		
		return user;
	}
	
	/**
	 * @param username
	 * @return if {@link User} was deleted successfully from database using HQL
	 */
	public boolean delete(String username) {
		Query query = getSession().createQuery("delete from User where username=:username");
		query.setString("username", username);
		
		boolean isDeleted = (query.executeUpdate() == 1);
		closeSession();
		
		return isDeleted;
	}
	
	/**
	 * @param username
	 * @return if {@link User} exists in database
	 */
	public boolean exists(String username) {
		return getUser(username) != null;
	}
}

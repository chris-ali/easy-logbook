package com.chrisali.easylogbook.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chrisali.easylogbook.model.User;

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
	 * Implementation of {@link AbstractDao#createOrUpdate(Object)}. Password is encoded using Spring's PasswordEncoder.
	 * 
	 * @param user
	 */
	public void createOrUpdateIntoDb(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		createOrUpdate(user);
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

package com.chrisali.easylogbook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.dao.UsersDao;

@Service("usersService")
public class UsersService {
	
	@Autowired
	private UsersDao usersDao;
	
	/**
	 * Creates or updates {@link User} in database
	 * 
	 * @param user
	 */
	public void createOrUpdate(User user) {
		usersDao.createOrUpdateIntoDb(user);
	}
	
	/**
	 * @param username
	 * @return if {@link User} with username exists in database
	 */
	public boolean exists(String username) {
		return usersDao.exists(username);
	}
	
	/**
	 * @param pageNumber
	 * @param resultsSize
	 * @return list of users paginated by pageNumber and resultsSize
	 */
	@Secured("ROLE_ADMIN")
	public List<User> getPaginatedUsers(int pageNumber, int resultsSize) {
		return usersDao.getPaginatedUsers(pageNumber, resultsSize);
	}
	
	/**
	 * @return total number of {@link User} in database
	 */
	@Secured("ROLE_ADMIN")
	public Long getTotalNumberUsers() {
		return usersDao.getTotalNumberUsers();
	}
	
	/**
	 * @param username
	 * @return {@link User} object from database using username
	 */
	public User getUser(String username) {
		return usersDao.getUser(username);
	}
	
	/**
	 * Deletes {@link User} from database
	 * 
	 * @param username
	 * @return if user was deleted successfully
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(String username) {
		return usersDao.delete(username);
	}
}

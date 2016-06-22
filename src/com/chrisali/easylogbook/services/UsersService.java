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
	
	public void createOrUpdate(User user) {
		usersDao.createOrUpdate(user);
	}
	
	public boolean exists(String username) {
		return usersDao.exists(username);
	}
	
	@Secured("ROLE_ADMIN")
	public List<User> getPaginatedUsers(int pageNumber, int resultsSize) {
		return usersDao.getPaginatedUsers(pageNumber, resultsSize);
	}
	
	@Secured("ROLE_ADMIN")
	public Long getTotalNumberUsers() {
		return usersDao.getTotalNumberUsers();
	}
	
	public User getUser(String username) {
		return usersDao.getUser(username);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean delete(String username) {
		return usersDao.delete(username);
	}
}

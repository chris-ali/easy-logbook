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
	
	public void createOrUpdateUser(User user) {
		usersDao.createOrUpdate(user);
	}
	
	public boolean userExists(String username) {
		return usersDao.exists(username);
	}
	
	@Secured("ROLE_ADMIN")
	public List<User> getAllUsers() {
		return usersDao.getAllUsers();
	}
	
	public User getUser(String username) {
		return usersDao.getUser(username);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	public boolean deleteUser(String username) {
		return usersDao.delete(username);
	}
}

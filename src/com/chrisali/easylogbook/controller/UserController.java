package com.chrisali.easylogbook.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chrisali.easylogbook.model.User;
import com.chrisali.easylogbook.service.UsersService;

@Controller
public class UserController {
	
	@Autowired
	private UsersService usersService;
	
	/**
	 * Shows {@link User} creation page. Adds new user object to model
	 * 
	 * @param model
	 * @return path to createaccount.html
	 */
	@RequestMapping("user/create")
	public String showCreateAccount(Model model) {
		model.addAttribute("user", new User());
		
		return "user/createaccount";
	}
	
	/**
	 * Does creation of {@link User}. Rejects creation and shows form validation errors if binding result finds errors, username
	 * already exists or DuplicateKeyException is thrown. User is added to Model
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return path to accountcreated.html
	 */
	@RequestMapping(value="user/docreate", method=RequestMethod.POST)
	public String doCreateAccount(@Validated User user, BindingResult result, Model model) {
		if (result.hasErrors())
			return "user/createaccount";
		
		user.setEnabled(true);
		user.setAuthority("ROLE_USER");
		
		if (usersService.exists(user.getUsername())) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "user/createaccount";
		}
		
		try {
			usersService.createOrUpdate(user);
		} catch (DuplicateKeyException e) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "user/createaccount";
		}
		
		model.addAttribute("user", user);
		
		return "user/accountcreated";
	}

	/**
	 * Does deletion of {@link User} from database and all data tied to account
	 * 
	 * @param principal
	 * @return path to home.html
	 */
	@RequestMapping("user/close")
	public String doCloseAccount(Principal principal) {
		usersService.delete(principal.getName());
		
		return "home/home";
	}
	
	/**
	 * @return path to login.html
	 */
	@RequestMapping("user/login")
	public String showLogin() {
		return "user/login";
	}
	
	/**
	 * @return path to logout.html
	 */
	@RequestMapping("user/logout")
	public String showLoggedOut() {
		return "user/logout";
	}
}
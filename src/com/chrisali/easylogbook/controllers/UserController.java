package com.chrisali.easylogbook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;
import com.chrisali.easylogbook.validation.FormValidationGroup;

@Controller
public class UserController {
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/createuser")
	public String showCreateUser() {
		return "createuser";
	}
	
	@RequestMapping(value="/accountcreated", method=RequestMethod.POST)
	public String doCreateAccount(@Validated(FormValidationGroup.class) User user, BindingResult result, Model model) {
		if (result.hasErrors())
			return "createaccount";
		
		user.setEnabled(true);
		user.setAuthority("ROLE_USER");
		
		if (usersService.exists(user.getUsername())) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "createaccount";
		}
		
		try {
			usersService.createOrUpdate(user);
		} catch (DuplicateKeyException e) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return "createaccount";
		}
		
		model.addAttribute("user", user);
		
		return "accountcreated";
	}
	
	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@RequestMapping("/loggedout")
	public String showLoggedOut() {
		return "loggedout";
	}
}

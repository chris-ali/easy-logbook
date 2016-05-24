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
	
	@RequestMapping("user/create")
	public String showCreateAccount(Model model) {
		model.addAttribute("user", new User());
		
		return "user/createaccount";
	}
	
	@RequestMapping(value="user/docreate", method=RequestMethod.POST)
	public String doCreateAccount(@Validated(FormValidationGroup.class) User user, BindingResult result, Model model) {
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
	
	@RequestMapping("user/login")
	public String showLogin() {
		return "user/login";
	}
	
	@RequestMapping("user/logout")
	public String showLoggedOut() {
		return "user/logout";
	}
}

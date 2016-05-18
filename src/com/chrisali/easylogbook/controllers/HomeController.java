package com.chrisali.easylogbook.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;

@Controller
public class HomeController {
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/")
	public String showHome(Principal principal, Model model) {
		if (principal != null) {
			User user = usersService.getUser(principal.getName());
			model.addAttribute("user", user);
		}
		
		return "home/home";
	}
	
	@RequestMapping(value = "/features")
	public String showFeatures() {
		return "home/features";
	}
}

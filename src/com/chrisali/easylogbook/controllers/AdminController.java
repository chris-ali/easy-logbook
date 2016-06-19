package com.chrisali.easylogbook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chrisali.easylogbook.beans.User;
import com.chrisali.easylogbook.services.UsersService;

@Controller
public class AdminController {
	
	@Autowired
	private UsersService usersService;

	@RequestMapping("/admin")
	public String showAdmin(Model model, 
							@RequestParam(value="page", required=false) int page,
							@RequestParam(value="results", required=false) int resultsSize) {
		List<User> usersList = usersService.getAllUsers();
		model.addAttribute("usersList", usersList);
		
		// Active class used on header fragment
		model.addAttribute("activeClassAdmin", "active");
		
		return "admin/admin";
	}
}

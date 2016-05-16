package com.chrisali.easylogbook.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome(Principal principal) {
		return "home/home";
	}
	
	@RequestMapping(value = "/features")
	public String showFeatures() {
		return "home/features";
	}
}

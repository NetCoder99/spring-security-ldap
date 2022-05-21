package com.john.spring_security_ldap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int verCounter = 0;

	@GetMapping("/")
	public String index() {
		return "Welcome to the Spring Security LDAP Authentication Guide!";
	}

	@RequestMapping(value = "/version", method = { RequestMethod.GET, RequestMethod.POST })
	public String version() {
		String version = "Version: 1.0.2::" + verCounter++;
		logger.info(version);
		return version;
	}

	@RequestMapping(value = "/versionSecure", method = { RequestMethod.GET, RequestMethod.POST })
	public String versionSecure() {
		String version = "Version: 1.0.2::" + verCounter++;
		logger.info(version);
		return version;
	}
}

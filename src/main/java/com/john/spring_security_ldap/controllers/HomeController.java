package com.john.spring_security_ldap.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.john.spring_security_ldap.security.LdapSearch;
import com.john.spring_security_ldap.security.SecurityConfig;

@RestController
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private int verCounter = 0;

	@Autowired LdapSearch ldapSearch;
	
	@GetMapping("/")
	public String index() {
		return "Welcome to the Spring Security LDAP Authentication Guide!";
	}

	@RequestMapping(value = "/version", method = { RequestMethod.GET, RequestMethod.POST })
	public String version() {
		String version = "Version: 1.1.2::" + verCounter++;
		logger.info(version);
		return version;
	}

	@RequestMapping(value = "/versionSecure", method = { RequestMethod.GET, RequestMethod.POST })
	public String versionSecure() {
		String version = "Version: 1.1.2::" + verCounter++;
		logger.info(version);
		return version;
	}

	@RequestMapping(value = "/listUsers", method = { RequestMethod.GET, RequestMethod.POST })
	public List<String> listUsers() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		return ldapSearch.getAll(authentication.getName(),  authentication.getCredentials().toString());
	}
	
}

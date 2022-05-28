package com.john.spring_security_ldap.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
//import javax.naming.Context;
//import javax.naming.NamingException;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;

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
	public List<String> listUsers(Authentication authentication) throws Exception {
		DirContext connection = getLdapConnection();	
		List<String> rtnList = getAllUsers(connection);
		return rtnList;
	}
	
	private String ldapUrl = "ldap://192.168.0.6:389";	
	private DirContext getLdapConnection() {
		Properties env = new Properties();
		DirContext connection = null;		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_PRINCIPAL, "CN=aabrev,OU=_USERS,DC=mydomain,DC=com");
		env.put(Context.SECURITY_CREDENTIALS, "Password1");
		
		try {
			connection = new InitialDirContext(env);
			System.out.println("Hello World!" + connection);
		} catch (AuthenticationException ex) {
			System.out.println(ex.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return connection;
		
	}

	@SuppressWarnings("rawtypes")
	public List<String> getAllUsers(DirContext connection) throws Exception {
		List<String> rtnList = new ArrayList<>();
		String searchFilter  = "(objectClass=*)";
		String[] reqAtt      = new String[]{"*", "+"};

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);

		NamingEnumeration users = connection.search("OU=_USERS,DC=mydomain,DC=com", searchFilter, controls);
		SearchResult result = null;
		int loopCntr = 0;
		while (users.hasMore()) {
			loopCntr += 1;
			if (loopCntr > 500) { break; }
			result = (SearchResult) users.next();
			Attributes attr = result.getAttributes();
			//listAttributes(attr);
			if (attr.get("sAMAccountName") != null ) {
				rtnList.add(attr.get("sAMAccountName").toString());
			} 
		}
		return rtnList;
	}

	
//	private void listAttributes(Attributes attrList) throws Exception {
//		List<String> ignoreList = new ArrayList<>(List.of("objectSid", "objectGUID"));
//		logger.info("-----------------------------------------------------");
//		NamingEnumeration<?> e = attrList.getAll();
//		while (e.hasMore()) 
//		{
//			Attribute attr = (Attribute) e.next();
//			//logger.info(attr.getID());	
//			if (!ignoreList.contains(attr.getID())) {
//				logger.info(attr.toString());	
//			}
//			
//		}
//	}
	
	
}

package com.john.spring_security_ldap.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component()
public class LdapSearch {
	private static final Logger logger = LoggerFactory.getLogger(LdapSearch.class);

	@Value("${ldap.url:ldap_url_property_not_found}")
	private String ldapUrl;	

	private final String srchBase       = "OU=_USERS,DC=mydomain,DC=com";
	private final String sAMAccountName = "sAMAccountName";
	private final HashMap<String, String> srchMap = new HashMap<>();
	
	// ---------------------------------------------------------------------------------------
	public LdapSearch() {
		srchMap.put("admins", "(memberOf=CN=workbench_admin,OU=_USERS,DC=mydomain,DC=com)");
		srchMap.put("users", "(memberOf=CN=workbench_user,OU=_USERS,DC=mydomain,DC=com)");
	}

	// ---------------------------------------------------------------------------------------
	public List<String> getAdmins(String userName, String passWord) throws Exception {
		String searchFilter  = srchMap.get("admins");
		return searchLdap(userName, passWord, searchFilter);
	}
	
	// ---------------------------------------------------------------------------------------
	public List<String> getUsers(String userName, String passWord) throws Exception {
		String searchFilter  = srchMap.get("users");
		return searchLdap(userName, passWord, searchFilter);
	}

	// ---------------------------------------------------------------------------------------
	public List<String> getAll(String userName, String passWord) throws Exception {
		String searchFilter  = String.format("(|%s%s)", srchMap.get("admins"), srchMap.get("users"));
		return searchLdap(userName, passWord, searchFilter);
	}

	// ---------------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public List<String> searchLdap(String userName, String passWord, String searchFilter) throws Exception {
		DirContext connection = getLdapConnection(userName, passWord);
		List<String> rtnList = new ArrayList<>();
		String[] reqAtt      = new String[]{"*"};
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);

		NamingEnumeration users = connection.search(srchBase, searchFilter, controls);
		SearchResult result = null;
		int loopCntr = 0;
		while (users.hasMore()) {
			loopCntr += 1;
			if (loopCntr > 500) { break; }
			result = (SearchResult) users.next();
			Attributes attr = result.getAttributes();
			//listAttributes(attr);
			if (attr.get(sAMAccountName) != null ) {
				rtnList.add(attr.get(sAMAccountName).toString());
			} 
		}
		return rtnList;
	}

	// ---------------------------------------------------------------------------------------
	private DirContext getLdapConnection(String userName, String passWord) throws Exception {
		Properties env = new Properties();
		DirContext connection = null;		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_PRINCIPAL, String.format("CN=%s,OU=_USERS,DC=mydomain,DC=com", userName));
		env.put(Context.SECURITY_CREDENTIALS, passWord);
		
		try {
			connection = new InitialDirContext(env);
		} catch (AuthenticationException ex) {
			logger.error("LDAP Failed to connect, AuthenticationException error: %s", ex.getLocalizedMessage());
			throw ex;
		} catch (NamingException ex) {
			logger.error("LDAP Failed to connect, NamingException error: %s", ex.getLocalizedMessage());
			throw ex;
		}	
		return connection;
		
	}

	// ---------------------------------------------------------------------------------------
//	List<String> ignoreAttrList = new ArrayList<>(List.of("objectSid", "objectGUID"));
//	private void listAttributes(Attributes attrList) throws Exception {
//		List<String> ignoreList = new ArrayList<>(List.of("objectSid", "objectGUID"));
//		logger.info("-----------------------------------------------------");
//		NamingEnumeration<?> e = attrList.getAll();
//		while (e.hasMore()) 
//		{
//			Attribute attr = (Attribute) e.next();
//			//logger.info(attr.getID());	
//			if (!ignoreAttrList.contains(attr.getID())) {
//				logger.info(attr.toString());	
//			}
//			
//		}
//	}

}

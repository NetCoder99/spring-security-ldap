package com.john.spring_security_ldap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthEntryPoint authEntryPoint;
    
	private String ldapUrl = "ldap://192.168.0.6:389";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/version").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/login/**").permitAll()
				.antMatchers("/versionSecure").authenticated()
				.anyRequest().fullyAuthenticated()
				.and().httpBasic()
				.authenticationEntryPoint(authEntryPoint);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		String customSearchFilter = "(&(objectClass=user)(sAMAccountName={1}))";
	    ActiveDirectoryLdapAuthenticationProvider adProvider =
	            new ActiveDirectoryLdapAuthenticationProvider("mydomain.com", ldapUrl);
	    adProvider.setSearchFilter(customSearchFilter);
	    adProvider.setConvertSubErrorCodesToExceptions(true);
	    adProvider.setUseAuthenticationRequestCredentials(true);
	    auth.authenticationProvider(adProvider);
	}

}
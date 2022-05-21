package com.john.spring_security_ldap.security;

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

	// ldap://100.36.224.125:389/dc=springframework,dc=org

	private String ldapUrl = "ldap://192.168.0.6:389";
	// private String ldapPort = "389";
	// private String baseDn = "DC=mydomain,DC=com";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/version").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/login/**").permitAll()
				.anyRequest().fullyAuthenticated()
				.and().httpBasic();
				//.defaultSuccessUrl("/versionSecure", true);
	}

	@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider activeDir = new ActiveDirectoryLdapAuthenticationProvider(null, ldapUrl);
		return activeDir;
	}
	
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//      .authorizeRequests()
//        .anyRequest().fullyAuthenticated()
//        .and()
//      .formLogin();
//  }
//
//  @Override
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth
//      .ldapAuthentication()
//        .userDnPatterns("uid={0},ou=people")
//        .groupSearchBase("ou=groups")
//        .contextSource()
//          .url("ldap://localhost:8389/dc=springframework,dc=org")
//          .and()
//        .passwordCompare()
//          //.passwordEncoder(new BCryptPasswordEncoder())
//          .passwordAttribute("userPassword");
//  }
//  
//  

  

  
  
  
  
  
  
  
  
  
}
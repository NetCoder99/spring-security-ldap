package com.john.spring_security_ldap.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MyCryptPasswordEncoder implements PasswordEncoder{

	private final int strength;
	
	public MyCryptPasswordEncoder() {
		this(-1);
	}
	
	public MyCryptPasswordEncoder(int strength) {
		this.strength = strength;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// TODO Auto-generated method stub
		return true;
	}

}

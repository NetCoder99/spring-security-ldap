package com.john.spring_security_ldap.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecurityFilter implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		logger.info("+++ init +++");
	}

	@Override
	public void destroy() {
		logger.info("+++ destroy +++");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) 
			throws IOException, ServletException {
		logger.info("+++ Remote Host    :" + request.getRemoteHost());
		logger.info("+++ Remote Address :" + request.getRemoteAddr());
		filterchain.doFilter(request, response);
	}


}

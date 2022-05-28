package com.john.spring_security_ldap.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.john.spring_security_ldap.controllers.HomeController;

@Component("AuthEntryPoint")
public class AuthEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.info("AuthEntryPoint.handle");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		//logger.info("AuthEntryPoint.commence:" + authException.getMessage());
		if (authException instanceof InsufficientAuthenticationException) {
			logger.info("AuthEntryPoint.commence:" + authException.getMessage());
		    //response.sendError(400, authException.getMessage());
		}
		if (authException instanceof BadCredentialsException) {
			logger.info("AuthEntryPoint.commence:" + authException.getMessage());
			logger.info("AuthEntryPoint.commence:" + authException.getCause().getMessage());
		    //response.sendError(400, authException.getMessage());
		}

		//Throwable cause = authException.getCause();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

}



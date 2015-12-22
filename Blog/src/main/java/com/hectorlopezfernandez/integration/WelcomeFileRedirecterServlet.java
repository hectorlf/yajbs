package com.hectorlopezfernandez.integration;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns={"/index.html"},loadOnStartup=1)
public class WelcomeFileRedirecterServlet implements Servlet {

	private final static Logger logger = LoggerFactory.getLogger(WelcomeFileRedirecterServlet.class);

	private ServletConfig servletConfig;
	
	@Override
	public void destroy() {
	}

	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public String getServletInfo() {
		return "WelcomeFileRedirecterServlet";
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		logger.debug("Entrando a WelcomeFileRedirecterServlet.service");
		if (!(arg1 instanceof HttpServletResponse)) throw new ServletException("Se ha recibido un ServletResponse que no es http, y no se puede responder la peticion.");
		HttpServletResponse response = (HttpServletResponse)arg1;
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.sendRedirect("index.action");
	}

}
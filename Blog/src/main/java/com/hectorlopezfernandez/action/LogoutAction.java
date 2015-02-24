package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.integration.BlogActionBeanContext;

@UrlBinding("/logout.action")
public class LogoutAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(LogoutAction.class);

	private BlogActionBeanContext ctx;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a LogoutAction.execute");
		Subject s = SecurityUtils.getSubject();
		if (s != null) s.logout();
		return new RedirectResolution(IndexAction.class);
	}
	
	// Getters y setters

	@Override
	public BlogActionBeanContext getContext() {
		return ctx;
	}
	@Override
	public void setContext(ActionBeanContext ctx) {
		this.ctx = (BlogActionBeanContext)ctx;
	}

}
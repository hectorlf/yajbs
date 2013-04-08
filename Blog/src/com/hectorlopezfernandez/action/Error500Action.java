package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.integration.BlogActionBeanContext;

public class Error500Action implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(Error500Action.class);

	private BlogActionBeanContext ctx;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a Error500Action.execute");
		return new ErrorResolution(500);
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
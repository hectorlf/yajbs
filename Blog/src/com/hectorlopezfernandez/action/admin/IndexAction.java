package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/index.action")
public class IndexAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(IndexAction.class);

	private BlogActionBeanContext ctx;
	
	// campos que guarda el actionbean
	
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a IndexAction.execute");
		return new ForwardResolution("/WEB-INF/jsp/admin/index.jsp");
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
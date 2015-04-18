package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.integration.BlogActionBeanContext;

@UrlBinding("/admin/newPage.action")
public class NewPageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(NewPageAction.class);

	private BlogActionBeanContext ctx;
	
	// campos que guarda el actionbean
	
	private boolean editing = false; // es nueva p�gina
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewPageAction.execute");
		return new ForwardResolution("/WEB-INF/jsp/admin/page-form.jsp");
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

	public boolean isEditing() {
		return editing;
	}

	// HACK! getters necesarios para evitar un null pointer en el EL de JSP
	public String getTitle() { return null;	}
	public String getTitleUrl() { return null; }
	public String getMetaDescription() { return null; }
	public String getContent() { return null; }

}
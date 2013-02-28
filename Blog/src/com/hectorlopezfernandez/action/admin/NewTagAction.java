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

@UrlBinding("/admin/newTag.action")
public class NewTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(NewTagAction.class);

	private BlogActionBeanContext ctx;
	
	// campos que guarda el actionbean
	
	private boolean editing = false; // es nueva etiqueta
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewTagAction.execute");
		return new ForwardResolution("/WEB-INF/jsp/admin/tag-form.jsp");
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
	public String getName() { return null;	}
	public String getNameUrl() { return null; }

}
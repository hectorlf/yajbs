package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.SearchService;

@UrlBinding("/admin/autocomplete.action")
public class AutocompleteAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(AutocompleteAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private SearchService searchService;
	
	// campos que guarda el actionbean
	private String q;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a AutocompleteAction.execute");
		searchService.autocomplete(q);
		StreamingResolution sr = new StreamingResolution("application/json", "{}");
		return sr;
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

	public void setQ(String q) {
		this.q = q;
	}

}
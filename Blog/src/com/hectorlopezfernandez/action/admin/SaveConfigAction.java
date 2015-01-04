package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.AdminPostService;
import com.hectorlopezfernandez.service.SearchService;

@UrlBinding("/admin/saveConfig.action")
public class SaveConfigAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SaveConfigAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject
	private SearchService searchService;

	@Inject
	private AdminPostService adminPostService;

	// campos que guarda el actionbean
	

	@HandlesEvent("reindexLucene")
	public Resolution reindexLucene() {
		logger.debug("Entrando a SaveConfigAction.reindexLucene");
		searchService.reindex();
		return new RedirectResolution(ConfigAction.class);
	}

	@HandlesEvent("reprocessFeeds")
	public Resolution reprocessFeeds() {
		logger.debug("Entrando a SaveConfigAction.reprocessFeeds");
		adminPostService.reprocessFeeds();
		return new RedirectResolution(ConfigAction.class);
	}

	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SaveConfigAction.execute");
		logger.warn("Se ha entrado al handler por defecto del SaveConfigAction. Revisa esto, anda.");
		return new RedirectResolution(ConfigAction.class);
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

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setAdminPostService(AdminPostService adminPostService) {
		this.adminPostService = adminPostService;
	}

}
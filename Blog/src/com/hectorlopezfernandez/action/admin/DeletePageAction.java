package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.PageService;

@UrlBinding("/admin/deletePage.action")
public class DeletePageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(DeletePageAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private PageService pageService;

	// campos que guarda el actionbean
	
	private Long id;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a DeletePageAction.execute");
		pageService.deletePage(id);
		return new RedirectResolution(ListPagesAction.class);
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

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
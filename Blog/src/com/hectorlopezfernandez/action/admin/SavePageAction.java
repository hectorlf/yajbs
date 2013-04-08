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
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.service.PageService;

@UrlBinding("/admin/savePage.action")
public class SavePageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SavePageAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private PageService pageService;

	// campos que guarda el actionbean
	
	private Long id;
	private String title;
	private String titleUrl;
	private String metaDescription;
	private String content;
	private Long hostId;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SavePageAction.execute");
		Page p = new Page();
		p.setContent(content);
		p.setId(id);
		p.setMetaDescription(metaDescription);
		p.setTitle(title);
		p.setTitleUrl(titleUrl);
		if (id == null) pageService.savePage(p, hostId);
		else pageService.modifyPage(p, hostId);
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

}
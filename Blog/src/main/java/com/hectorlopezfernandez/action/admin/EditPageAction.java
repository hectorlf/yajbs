package com.hectorlopezfernandez.action.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.service.PageService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/admin/editPage.action")
public class EditPageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(EditPageAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PageService pageService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private String title;
	private String titleUrl;
	private String metaDescription;
	private String content;
	private boolean editing = true; // es modificacion
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a EditPageAction.execute");
		Page p = pageService.getPage(id);
		title = p.getTitle();
		titleUrl = p.getTitleUrl();
		metaDescription = p.getMetaDescription();
		content = p.getContent();
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public String getContent() {
		return content;
	}

	public boolean isEditing() {
		return editing;
	}

}
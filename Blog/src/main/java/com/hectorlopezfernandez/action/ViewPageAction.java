package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PageService;

@UrlBinding("/viewPage.action")
public class ViewPageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ViewPageAction.class);
	public final static String PARAM_ID = "id";

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject	private PageService pageService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private Page page;

	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ViewPageAction.execute");
		if (id == null) return new ForwardResolution(Error404Action.class);
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se carga la p�gina a mostrar
		page = pageService.getPage(id);
		// si no existe, 404
		if (page == null) return new ForwardResolution(Error404Action.class);
		return new ForwardResolution("/WEB-INF/jsp/page.jsp");
	}
	
	// Getters y setters

	public Page getPage() {
		return page;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	// contexto y servicios

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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}
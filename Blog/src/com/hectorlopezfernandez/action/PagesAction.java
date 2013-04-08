package com.hectorlopezfernandez.action;

import javax.inject.Inject;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.PageService;

@UrlBinding("/pages/{name}/{overhead}")
public class PagesAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(PagesAction.class);

	private BlogActionBeanContext ctx;
	@Inject	private PageService pageService;
	
	// campos que guarda el actionbean
	
	private String name;
	private String overhead;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a PagesAction.execute");
		logger.debug("name: {}", name);
		logger.debug("overhead: {}", overhead);
		// si overhead contiene algo, la url no puede ser válida y se manda un 404
		if (overhead != null && overhead.length() > 0) return new ForwardResolution(Error404Action.class);
		// si no se ha especificado un nombre de pagina, se manda un 404
		if (name == null || name.length() == 0) return new ForwardResolution(Error404Action.class);
		// se busca una página que tenga el nombre indicado y, si no existe, se envía un 404
		Long pageId = pageService.findPageId(name);
		if (pageId == null) return new ForwardResolution(Error404Action.class);
		// se carga la página por id
		ForwardResolution fr = new ForwardResolution(ViewPageAction.class);
		fr.addParameter(ViewPageAction.PARAM_ID, pageId);
		return fr;
	}
	
	// Getters y setters

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public void setName(String name) {
		this.name = name;
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

}
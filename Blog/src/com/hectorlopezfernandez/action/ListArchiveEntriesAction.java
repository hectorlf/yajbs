package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.service.PostService;

public class ListArchiveEntriesAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListArchiveEntriesAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<ArchiveEntry> entries;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListArchiveEntriesAction.execute");
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se recupera la lista completa de tags
		entries = postService.getAllArchiveEntriesWithPublishedPostCount();
		return new ForwardResolution("/WEB-INF/jsp/archive-entry-list.jsp");
	}
	
	// Getters y setters

	public List<ArchiveEntry> getEntries() {
		return entries;
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

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

}
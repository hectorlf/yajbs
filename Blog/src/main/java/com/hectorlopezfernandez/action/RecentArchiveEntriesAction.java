package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.service.PostService;

public class RecentArchiveEntriesAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(RecentArchiveEntriesAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<ArchiveEntry> entries;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a RecentArchiveEntriesAction.execute");
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		entries = postService.getRecentArchiveEntries(prefs.getArchiveEntriesPerIndexPage());
		return null;
	}
	
	// Getters y setters

	public List<ArchiveEntry> getEntries() {
		return entries;
	}

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
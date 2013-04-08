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
import com.hectorlopezfernandez.model.Comment;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.service.PostService;

public class RecentCommentsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(RecentCommentsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<Comment> comments;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a RecentCommentsAction.execute");
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		comments = postService.getRecentComments(prefs.getRecentCommentsPerIndexPage());
		return null;
	}
	
	// Getters y setters

	public List<Comment> getComments() {
		return comments;
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
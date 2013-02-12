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
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

public class ListTagsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListTagsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<Tag> tags;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListTagsAction.execute");
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se recupera la lista completa de tags
		tags = postService.getAllTags();
		return new ForwardResolution("/WEB-INF/jsp/tag-list.jsp");
	}
	
	// Getters y setters

	public List<Tag> getTags() {
		return tags;
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
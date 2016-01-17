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
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.TagService;

public class ListTagsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListTagsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private TagService tagService;
	
	// campos que guarda el actionbean
	
	private List<Tag> tags;
	private String alt;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListTagsAction.execute");
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se recupera la lista completa de tags
		tags = tagService.getAllTags();
		boolean alternateTemplate = Boolean.parseBoolean(alt);
		if (alternateTemplate) return new ForwardResolution("/WEB-INF/jsp/tag-list.jsp");
		else return new ForwardResolution("/WEB-INF/pebble/tag-list.pebble");
	}
	
	// Getters y setters

	public List<Tag> getTags() {
		return tags;
	}

	public void setAlt(String alt) {
		this.alt = alt;
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

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}
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
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/saveTag.action")
public class SaveTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SaveTagAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private PostService postService;

	// campos que guarda el actionbean
	
	private Long id;
	private String name;
	private String nameUrl;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SaveTagAction.execute");
		Tag t = new Tag();
		t.setId(id);
		t.setName(name);
		t.setNameUrl(nameUrl);
		if (id == null) postService.saveTag(t);
		else postService.modifyTag(t);
		return new RedirectResolution(ListTagsAction.class);
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

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameUrl(String nameUrl) {
		this.nameUrl = nameUrl;
	}

}
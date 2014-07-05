package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.TagService;

@UrlBinding("/admin/saveTag.action")
public class SaveTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SaveTagAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private TagService tagService;

	// campos que guarda el actionbean
	
	private Long id;
	private String name;
	private String nameUrl;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SaveTagAction.execute");
		Tag t = new Tag();
		t.setId(id);
		t.setName(StringEscapeUtils.escapeHtml4(name));
		t.setNameUrl(nameUrl);
		if (id == null) tagService.saveTag(t);
		else tagService.modifyTag(t);
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

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
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
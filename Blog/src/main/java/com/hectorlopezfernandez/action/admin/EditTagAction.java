package com.hectorlopezfernandez.action.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.TagService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/admin/editTag.action")
public class EditTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(EditTagAction.class);

	private BlogActionBeanContext ctx;
	@Inject private TagService tagService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private String name;
	private String nameUrl;
	private boolean editing = true; // es modificacion
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a EditTagAction.execute");
		Tag t = tagService.getTag(id);
		name = t.getName();
		nameUrl = t.getNameUrl();
		return new ForwardResolution("/WEB-INF/jsp/admin/tag-form.jsp");
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getNameUrl() {
		return nameUrl;
	}

	public boolean isEditing() {
		return editing;
	}

}
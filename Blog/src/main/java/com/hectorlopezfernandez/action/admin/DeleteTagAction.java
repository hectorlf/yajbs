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
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.TagService;

@UrlBinding("/admin/deleteTag.action")
public class DeleteTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(DeleteTagAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private TagService tagService;

	// campos que guarda el actionbean
	
	private Long id;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a DeleteTagAction.execute");
		tagService.deleteTag(id);
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

}
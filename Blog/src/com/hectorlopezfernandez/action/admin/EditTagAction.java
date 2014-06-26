package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/admin/editTag.action")
public class EditTagAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(EditTagAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private String name;
	private String nameUrl;
	private boolean editing = true; // es modificación
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a EditTagAction.execute");
		Tag t = postService.getTag(id);
		name = StringEscapeUtils.unescapeHtml4(t.getName());
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

	public void setPostService(PostService postService) {
		this.postService = postService;
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
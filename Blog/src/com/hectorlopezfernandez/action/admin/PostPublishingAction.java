package com.hectorlopezfernandez.action.admin;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.AdminPostService;

@UrlBinding("/admin/postPublishing.action")
public class PostPublishingAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(PostPublishingAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private AdminPostService postService;

	// campos que guarda el actionbean
	
	private Long id;

	
	@HandlesEvent(value="publish")
	public Resolution publish() {
		logger.debug("Entrando a PostPublishingAction.publish");
		postService.publishPost(id);
		return new RedirectResolution(ListPostsAction.class);
	}
	
	@HandlesEvent(value="unpublish")
	public Resolution unpublish() {
		logger.debug("Entrando a PostPublishingAction.unpublish");
		postService.unpublishPost(id);
		return new RedirectResolution(ListPostsAction.class);
	}
	
	@HandlesEvent(value="changePublicationDate")
	public Resolution changePublicationDate() {
		logger.debug("Entrando a PostPublishingAction.changePublicationDate");
		postService.changePostPublicationDate(id, new DateTime());
		return new RedirectResolution(ListPostsAction.class);
	}
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a PostPublishingAction.execute");
		throw new UnsupportedOperationException("Es necesario un evento para saber que hacer con el post");
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

	public void setPostService(AdminPostService postService) {
		this.postService = postService;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
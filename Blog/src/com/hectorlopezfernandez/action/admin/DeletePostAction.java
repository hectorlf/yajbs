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
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/admin/deletePost.action")
public class DeletePostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(DeletePostAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private PostService postService;
	@Inject private PostDao postDao;

	// campos que guarda el actionbean
	
	private Long id;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a DeletePostAction.execute");
		postService.deletePost(id);
		// TODO encontrar otra forma de actualizar la cuenta de posts etiquetados en un tag
		postDao.updateTagRefCounts();
		return new RedirectResolution(ListPostsAction.class);
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

}
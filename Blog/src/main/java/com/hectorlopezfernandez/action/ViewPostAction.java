package com.hectorlopezfernandez.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/viewPost.action")
public class ViewPostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ViewPostAction.class);
	public final static String PARAM_ID = "id";

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private Post post;

	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ViewPostAction.execute");
		if (id == null) return new ErrorResolution(404);
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se carga la pagina a mostrar
		post = postService.getDetailedPost(id);
		// si no existe, 404
		if (post == null) return new ErrorResolution(404);
		return new ForwardResolution("/WEB-INF/pebble/post.pebble");
	}
	
	// Getters y setters

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}
package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/index.action")
public class IndexAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(IndexAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<Post> posts;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a IndexAction.execute");
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		posts = postService.getNewestPosts(prefs.getPostsPerIndexPage());
		return new ForwardResolution("/WEB-INF/jsp/index.jsp");
	}
	
	// Getters y setters

	public List<Post> getPosts() {
		return posts;
	}

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
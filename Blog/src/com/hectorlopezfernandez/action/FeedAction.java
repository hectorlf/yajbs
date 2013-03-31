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
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/feed.atom")
public class FeedAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(FeedAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private List<SimplifiedPost> posts;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a FeedAction.execute");
		Alias alias = ctx.getAlias();
		ctx.setAttribute("alias", alias);
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		int maxPostAge = prefs.getMaxPostAgeInDaysForFeeds() == null ? 0 : prefs.getMaxPostAgeInDaysForFeeds().intValue();
		posts = postService.getNewestPostsForFeed(maxPostAge);
		return new ForwardResolution("/WEB-INF/jsp/feed.jsp");
	}
	
	// Getters y setters

	public List<SimplifiedPost> getPosts() {
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
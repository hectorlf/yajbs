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
import com.hectorlopezfernandez.dto.SimplifiedPage;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PageService;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/sitemap.xml")
public class SitemapAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SitemapAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	@Inject private PageService pageService;
	
	// campos que guarda el actionbean
	
	private List<SimplifiedPost> posts;
	private List<SimplifiedPage> pages;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SitemapAction.execute");
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		posts = postService.getPostsForSitemap();
		pages = pageService.getPagesForSitemap();
		return new ForwardResolution("/WEB-INF/jsp/sitemap.jsp");
	}
	
	// Getters y setters

	public List<SimplifiedPost> getPosts() {
		return posts;
	}

	public List<SimplifiedPage> getPages() {
		return pages;
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

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}
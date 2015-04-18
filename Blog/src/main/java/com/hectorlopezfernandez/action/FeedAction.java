package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.SimplifiedPost;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/feed.atom")
public class FeedAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(FeedAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private DateTime lastModificationDate;
	private List<SimplifiedPost> posts;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a FeedAction.execute");
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		int maxPostAge = prefs.getMaxPostAgeInDaysForFeeds() == null ? 0 : prefs.getMaxPostAgeInDaysForFeeds().intValue();
		posts = postService.getNewestPostsForFeed(maxPostAge);
		// se comparan las fechas para obtener la m�s reciente y mostrarla como fecha de �ltima actualizaci�n
		lastModificationDate = new DateTime(0);
		if (posts.size() > 0) {
			for (SimplifiedPost sp : posts) {
				if (lastModificationDate.isBefore(sp.getPublicationDate())) lastModificationDate = sp.getPublicationDate();
			}
		}
		return new ForwardResolution("/WEB-INF/jsp/feed.jsp");
	}
	
	// Getters y setters

	public List<SimplifiedPost> getPosts() {
		return posts;
	}

	public DateTime getLastModificationDate() {
		return lastModificationDate;
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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}